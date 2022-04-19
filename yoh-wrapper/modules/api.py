import json
from datetime import datetime, date
from flask import Blueprint, make_response, request, session, abort
from werkzeug.exceptions import HTTPException
from requests import post

api_bp = Blueprint('API', __name__, url_prefix='/api')


def json_serial(obj):
    """JSON serializer for objects not serializable by default json code"""

    if isinstance(obj, (datetime, date)):
        return obj.isoformat()
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


@api_bp.route('/send_statistic', methods=['POST'])
def send_statistic_route():
    data = json.loads(request.data)
    if not session.get('user') or not session.get('current_game'):
        abort(401)

    event_type = {
        'game_start': 1,
        'game_end': 2,
        'missclick': 3,
        'correct_answer': 4,
        'incorrect_answer': 5
    }
    response_params = {
        'records': []
    }

    # Обрабатываем миссклики
    for event_date, event_params in data['missClicks']:
        record = {
            'DateAction': event_date,
            'Type': event_type['missclick'],
            'AnswerNumber': event_params['answer_number']
        }
        response_params['records'].append(record)

    # Обрабатываем ответы
    for event_date, event_params in data['answers']:
        record = {
            'DateAction': event_date,
            'Type': event_type['correct_answer' if event_params['correct'] else 'incorrect_answer'],
            'AnswerNumber': event_params['answer_number']
        }
        response_params['records'].append(record)

    start_rec = {
        'DateAction': data['startTime'],
        'Type': event_type['game_start']
    }
    response_params['records'].append(start_rec)

    end_rec = {
        'DateAction': data['endTime'],
        'Type': event_type['game_end']
    }
    response_params['records'].append(end_rec)

    headers = {
        'Content-Type': 'application/json',
        'user': session['user'],
        'game': session['current_game']
    }
    send_url = 'http://localhost:5000/patient/games/statistics/sending'

    post(send_url, data=json.dumps(response_params, default=json_serial), headers=headers)

    return make_response(json.dumps({'message': 'Success'}))
