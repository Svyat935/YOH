import json
import psycopg2
import psycopg2.extras
import uuid
from datetime import datetime, date
from flask import Blueprint, make_response, request, session, abort
from flask_cors import CORS, cross_origin
from werkzeug.exceptions import HTTPException
from requests import post
from itertools import zip_longest
from .templates import GET_ATTEMPT_PAGINATION, GET_ALL_TIME_WIDGET, CLICKS_WIDGET, ANSWERS_WIDGET, TIMELINE_WIDGET

api_bp = Blueprint('API', __name__, url_prefix='/api')
CORS(api_bp, resources={r"/api/*": {"origins": "*"}})
psycopg2.extras.register_uuid()

CONNECT_PARAMS = {
    "host": "yoh-db",
    "port": "5432",
    "database": "yoh",
    "user": "postgres",
    "password": "postgres"
}


def json_serial(obj):
    """JSON serializer for objects not serializable by default json code"""

    if isinstance(obj, (datetime, date)):
        return obj.isoformat()
    if isinstance(obj, uuid.UUID):
        return obj.hex
    raise TypeError("Type %s not serializable" % type(obj))


@api_bp.errorhandler(HTTPException)
def error_handler_route(error):
    if error.code == 401:
        message = 'Session ID or Game ID not passed'
    else:
        message = error.description
    response = make_response(
        json.dumps({
           'message': message
        }),
        error.code
    )
    response.headers["Content-Type"] = "application/json"
    return response


@api_bp.route('/game_start', methods=['POST'])
def send_game_start_route():
    data = json.loads(request.data)
    if not session.get('user') or not session.get('current_game'):
        abort(401)

    headers = {
        'Content-Type': 'application/json',
        'token': session['user'],
        'game': session['current_game']
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/game_start'

    data['details'] = json.dumps(data.get('details')) if data.get('details') else '{}'
    post(send_url, json=data, headers=headers)

    return make_response(json.dumps({'message': 'Success'}))


@api_bp.route('/game_end', methods=['POST'])
def send_game_end_route():
    data = json.loads(request.data)
    if not session.get('user') or not session.get('current_game'):
        abort(401)

    headers = {
        'Content-Type': 'application/json',
        'token': session['user'],
        'game': session['current_game']
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/game_end'

    data['details'] = json.dumps(data.get('details')) if data.get('details') else '{}'
    post(send_url, json=data, headers=headers)

    return make_response(json.dumps({'message': 'Success'}))


@api_bp.route('/statistics', methods=['POST'])
def statistics_route():
    data = json.loads(request.data)
    if not session.get('user') or not session.get('current_game'):
        abort(401)

    # statistic_types = {
    #     Решили вопрос полностью правильно: 1
    #     Решили вопрос полностью неправильно: 2
    #     Просто переключили уровень: 3
    # }

    headers = {
        'Content-Type': 'application/json',
        'token': session['user'],
        'game': session['current_game']
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/send_statistic'

    data['details'] = json.dumps(data.get('details')) if data.get('details') else '{}'
    data['additional_fields'] = json.dumps(data.get('additional_fields')) if data.get('additional_fields') else '{}'
    post(send_url, json=data, headers=headers)

    return make_response(json.dumps({'message': 'Success'}))


@api_bp.route('/additional_fields', methods=['GET'])
def additional_fields_route():
    add_fields = session.get('additional_fields')
    return make_response(add_fields)


@api_bp.route('/statistic_pagination', methods=['GET'])
def statistic_pagination_route():
    # gp_id
    parameters = request.args

    with psycopg2.connect(**CONNECT_PARAMS) as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cursor:
            cursor.execute(GET_ATTEMPT_PAGINATION, {'gp_id': parameters['gp_id']})
            result = cursor.fetchone()

    return make_response(json.dumps(result, default=json_serial))


def format_response_json(result, source, fields):
    if source == 'mobile':
        result = {
            'format': fields,
            'values': zip_longest(*[result[field] for field in fields]),
            'other': {key: result[key] for key in (set(result.keys()) - set(fields))}
        }

    return result


@api_bp.route('/all_time_widget', methods=['GET'])
def all_time_widget_route():
    # sg_id, [source]
    parameters = request.args

    with psycopg2.connect(**CONNECT_PARAMS) as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cursor:
            cursor.execute(GET_ALL_TIME_WIDGET, {'sg_id': parameters['sg_id']})
            result = cursor.fetchone()

    iterated_result_fields = ['level_names', 'spend_time']
    result = format_response_json(result, parameters.get('source'), iterated_result_fields)

    return make_response(json.dumps(result, default=json_serial))


@api_bp.route('/clicks_widget', methods=['GET'])
def clicks_widget_route():
    # sg_id, [source]
    parameters = request.args

    with psycopg2.connect(**CONNECT_PARAMS) as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cursor:
            cursor.execute(CLICKS_WIDGET, {'sg_id': parameters['sg_id']})
            result = cursor.fetchone()

    iterated_result_fields = ['level_names', 'clicks', 'missclicks']
    result = format_response_json(result, parameters.get('source'), iterated_result_fields)

    return make_response(json.dumps(result, default=json_serial))


@api_bp.route('/answers_widget', methods=['GET'])
def answers_widget_route():
    # sg_id, [source]
    parameters = request.args

    with psycopg2.connect(**CONNECT_PARAMS) as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cursor:
            cursor.execute(ANSWERS_WIDGET, {'sg_id': parameters['sg_id']})
            result = cursor.fetchone()

    iterated_result_fields = ['level_names', 'correct', 'incorrect']
    result = format_response_json(result, parameters.get('source'), iterated_result_fields)

    return make_response(json.dumps(result, default=json_serial))


@api_bp.route('/timeline_widget', methods=['GET'])
def timeline_widget_route():
    # sg_id, [source]
    parameters = request.args

    with psycopg2.connect(**CONNECT_PARAMS) as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cursor:
            cursor.execute(TIMELINE_WIDGET, {'sg_id': parameters['sg_id']})
            result = cursor.fetchall()

    return make_response(json.dumps(result, default=json_serial))
