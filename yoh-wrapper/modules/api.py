import json
import psycopg2
import psycopg2.extras
import uuid
from datetime import datetime, date, timedelta
from urllib import parse
from flask import Blueprint, make_response, request, session, abort
from flask_cors import CORS
from werkzeug.exceptions import HTTPException
from requests import post, get
from .templates import GET_ATTEMPT_PAGINATION, GET_ALL_TIME_WIDGET, CLICKS_WIDGET, ANSWERS_WIDGET, TIMELINE_WIDGET


# Инициализируем модуль API
api_bp = Blueprint('API', __name__, url_prefix='/api')
# Указываем, что по этому адресу разрешаются кросс-доменные запросы
CORS(api_bp, resources={r"/api/*": {"origins": "*"}})
# Сообщаем драйверу, что мы в своей БД используем UUID, чтобы он подтянул нужные расширения
psycopg2.extras.register_uuid()

# Параметры подключения к БД
CONNECT_PARAMS = {
    "host": "yoh-db",
    "port": "5432",
    "database": "yoh",
    "user": "postgres",
    "password": "postgres"
}


def json_serial(obj):
    """
    JSON сериализация для объектов, которые не сериализуются в стандартном JSON
    """
    if isinstance(obj, (datetime, date)):
        return obj.isoformat()
    if isinstance(obj, uuid.UUID):
        return obj.hex
    raise TypeError("Type %s not serializable" % type(obj))


@api_bp.errorhandler(HTTPException)
def error_handler_route(error):
    """
    Обработчик ошибок для API.
    Возвращает JSON, а не стандартную страницу с ошибкой
    """
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


def url_parse(url):
    """
    Достает из переданного URL игру и токен
    """
    parsed_url = parse.urlsplit(url)
    game = parsed_url.path.split('/')[-2]
    token = parse.parse_qs(parsed_url.query)['token'][0]
    return game, token


@api_bp.route('/game_start', methods=['POST'])
def send_game_start_route():
    """
    Отправка информации о начале игры.
    На бэкенде выполняется вставка в таблицу "started_games" с пустой датой конца
    """
    data = json.loads(request.data)

    token = session.get('user')
    game = session.get('current_game')
    if not token or not game:
        game, token = url_parse(request.referrer)
        if not token or not game:
            abort(401)

    headers = {
        'Content-Type': 'application/json',
        'token': token,
        'game': game
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/game_start'

    data['details'] = json.dumps(data.get('details')) if data.get('details') else '{}'
    post(send_url, json=data, headers=headers)

    return make_response(json.dumps({'message': 'Success'}))


@api_bp.route('/game_end', methods=['POST'])
def send_game_end_route():
    """
    Отправка информации о конце игры.
    На бэкенде выполняется обновление таблицы "started_games" с записью датой конца
    """
    data = json.loads(request.data)

    token = session.get('user')
    game = session.get('current_game')
    if not token or not game:
        game, token = url_parse(request.referrer)
        if not token or not game:
            abort(401)

    headers = {
        'Content-Type': 'application/json',
        'token': token,
        'game': game
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/game_end'

    data['details'] = json.dumps(data.get('details')) if data.get('details') else '{}'
    post(send_url, json=data, headers=headers)

    resp = make_response(json.dumps({'message': 'Success'}))

    return resp


@api_bp.route('/statistics', methods=['POST'])
def statistics_route():
    """
    Основной метод отправки статистики в процессе прохождения игры
    На бэкенде выполняется вставка в таблицу "game_statistics"
    """
    data = json.loads(request.data)

    token = session.get('user')
    game = session.get('current_game')
    if not token or not game:
        game, token = url_parse(request.referrer)
        if not token or not game:
            abort(401)

    headers = {
        'Content-Type': 'application/json',
        'token': token,
        'game': game
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/send_statistic'

    data['details'] = json.dumps(data.get('details')) if data.get('details') else '{}'
    data['additional_fields'] = json.dumps(data.get('additional_fields')) if data.get('additional_fields') else '{}'
    post(send_url, json=data, headers=headers)

    return make_response(json.dumps({'message': 'Success'}))


@api_bp.route('/additional_fields', methods=['GET'])
def additional_fields_route():
    """
    Метод получения дополнительных полей для игры
    """
    token = session.get('user')
    game = session.get('current_game')
    if not token or not game:
        game, token = url_parse(request.referrer)
        if not token or not game:
            abort(401)

    headers = {
        'Content-Type': 'application/json',
        'token': token,
        'game': game
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/additional_fields'
    result = get(send_url, headers=headers).json()

    result = result['result'] if result['result'] else '{}'

    return make_response(result)


@api_bp.route('/additional_fields', methods=['POST'])
def additional_fields_post_route():
    """
    Метод принудительной отправки дополнительных полей для игры
    """
    token = session.get('user')
    game = session.get('current_game')
    if not token or not game:
        game, token = url_parse(request.referrer)
        if not token or not game:
            abort(401)

    headers = {
        'Content-Type': 'application/json',
        'token': token,
        'game': game
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/additional_fields'

    data = json.loads(request.data)
    data['details'] = json.dumps(data.get('details')) if data.get('details') else '{}'
    result = post(send_url, json=data, headers=headers)

    return make_response(json.dumps({'message': 'Success'}))


@api_bp.route('/statistic_pagination', methods=['GET'])
def statistic_pagination_route():
    """
    Возвращает идентификаторы попыток в порядке возрастания

    Принимает на вход в аргументах:
        gp_id - uuid - идентификатор game_patient_id
    """
    parameters = request.args

    with psycopg2.connect(**CONNECT_PARAMS) as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cursor:
            cursor.execute(GET_ATTEMPT_PAGINATION, {'gp_id': parameters['gp_id']})
            result = cursor.fetchone()

    return make_response(json.dumps(result, default=json_serial))


def format_response_json(result, source, fields):
    """
    Выполняет форматирование результата для разных получателей

    Принимает на вход:
        result - dict - Результат из БД
        source - str - источник запроса, для кого выполняется форматирование
        fields - list[str] - Список итерируемых полей
    """
    if source == 'mobile':
        result = {
            'format': fields,
            'values': [{key: result[key][i] for key in fields} for i in range(len(result[fields[0]]))],
            'other': {key: result[key] for key in (set(result.keys()) - set(fields))}
        }

    return result


@api_bp.route('/all_time_widget', methods=['GET'])
def all_time_widget_route():
    """
    Возвращает данные для виджета потраченного времени

    Принимает на вход в аргументах:
        sg_id - uuid - идентификатор started_game_id(попытки, полученной в пагинации)
        *source - str - источник запроса для нужного формата возвращаемого значения

    * - опционально
    """
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
    """
    Возвращает данные для виджета кликов

    Принимает на вход в аргументах:
        sg_id - uuid - идентификатор started_game_id(попытки, полученной в пагинации)
        *source - str - источник запроса для нужного формата возвращаемого значения

    * - опционально
    """
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
    """
    Возвращает данные для виджета ответов

    Принимает на вход в аргументах:
        sg_id - uuid - идентификатор started_game_id(попытки, полученной в пагинации)
        *source - str - источник запроса для нужного формата возвращаемого значения

    * - опционально
    """
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
    """
    Возвращает данные для виджета затраченного времени по каждому уровню

    Принимает на вход в аргументах:
        sg_id - uuid - идентификатор started_game_id(попытки, полученной в пагинации)
        *source - str - источник запроса для нужного формата возвращаемого значения

    * - опционально
    """
    parameters = request.args

    with psycopg2.connect(**CONNECT_PARAMS) as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cursor:
            cursor.execute(TIMELINE_WIDGET, {'sg_id': parameters['sg_id']})
            result = cursor.fetchall()

    return make_response(json.dumps(result, default=json_serial))


@api_bp.route('/leave_game_event', methods=['POST'])
def leave_game_event_route():
    """
    Сигнализирует серверу о завершении игры для приложений, не использующих класс по сбору статистики.

    Метод записывает в куки флаг о завершении игры. Через 10 секунд этот куки будет очищен.

    Интерфейс каждую секунду делает проверку на этот флаг.
    При обнаружении выполняет отправку события для мобильного приложения или браузеру.
    """
    resp = make_response(json.dumps({'message': 'Success'}), 200)
    # samesite='None', secure=True нужен для кросс-доменных сохранений cookie
    # Без этих флагов сохранение куки не работает, так как интерфейс находится в другом домене, а игра запущена в iframe
    resp.set_cookie('EndGame', 'true', samesite='None', secure=True, path='/')
    return resp
