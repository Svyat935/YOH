import json
import psycopg2
from prettytable import PrettyTable
from datetime import datetime, date
from flask import Blueprint, make_response, request, session, abort, render_template
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
        'incorrect_answer': 5,
        'clicks_info': 6
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
            'AnswerNumber': event_params['answer_number'],
            'Details': {
                'time_start': event_params.get('time_start')
            }
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

    if 'clicks' in data:
        clicks_info_rec = {
            'DateAction': data['endTime'],
            'Type': event_type['clicks_info'],
            'Details': {
                'window_size': data['window_info'],
                'clicks': data['clicks']
            }
        }
        response_params['records'].append(clicks_info_rec)

    headers = {
        'Content-Type': 'application/json',
        'token': session['user'],
        'game': '8e520e72-def0-4f05-8173-691e687e8931'  # session['current_game']
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/sending'

    post(send_url, data=json.dumps(response_params, default=json_serial), headers=headers)

    return make_response(json.dumps({'message': 'Success'}))


@api_bp.route('/statistics')
def statistics_route():
    conn = psycopg2.connect(
        host="yoh-db",
        port="5432",
        database="yoh",
        user="postgres",
        password="postgres")
    cursor = conn.cursor()
    cursor.execute('select * from game_statistics')
    th = ['id', 'answer_number', 'date_action', 'details', 'type', 'game_id', 'patient_id']
    td = cursor.fetchall()
    columns = len(th)

    table = PrettyTable(th)

    for td_data in td:
        table.add_row(td_data[:columns])
    return make_response(table.get_html_string())


@api_bp.route('/heatmap')
def heatmap_route():
    conn = psycopg2.connect(
        host="yoh-db",
        port="5432",
        database="yoh",
        user="postgres",
        password="postgres")
    cursor = conn.cursor()
    cursor.execute('select "details" from game_statistics where "type" = 6 order by "id" desc limit 1')
    data = cursor.fetchone()
    if data:
        data = data[0]
        points_data = []
        values = []
        for point, value in data['clicks'].items():
            values.append(value)
            x, y = point.split('.')
            points_data.append({"x": int(x), "y": int(y), "value": value})
        params_to_render = {
            'points': points_data,
            'window': {
                'height': data['window_size']['height'],
                'width': data['window_size']['width']
            },
            'max_point': max(values)
        }
        return render_template(f'test_heatmap/index.html', **params_to_render)
    else:
        abort(404)
