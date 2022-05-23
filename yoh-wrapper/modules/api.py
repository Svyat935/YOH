import json
import psycopg2
import uuid
from datetime import datetime, date
from flask import Blueprint, make_response, request, session, abort
from werkzeug.exceptions import HTTPException
from requests import post

api_bp = Blueprint('API', __name__, url_prefix='/api')


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

    post(send_url, data=json.dumps(data, default=json_serial), headers=headers)

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

    post(send_url, data=json.dumps(data, default=json_serial), headers=headers)

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

    post(send_url, data=json.dumps(data, default=json_serial), headers=headers)

    return make_response(json.dumps({'message': 'Success'}))


@api_bp.route('/additional_fields', methods=['GET'])
def additional_fields_route():
    add_fields = session.get('additional_fields')
    return make_response(add_fields)


@api_bp.route('/statistic_pagination', methods=['GET'])
def statistic_pagination_route():
    parameters = request.args
    conn = psycopg2.connect(
        host="yoh-db",
        port="5432",
        database="yoh",
        user="postgres",
        password="postgres")
    cursor = conn.cursor()

    query = """
        select array(
            select
                "id" as "ids" 
            from "started_games" 
            where 
                "game_patient_id" = %(gp_id)s and 
                "date_end" is not null 
            order by "date_end"
        );
    """
    cursor.execute(query, {'gp_id': parameters['gp_id']})
    result = cursor.fetchone()
    return make_response(json.dumps(result, default=json_serial))


"""
-- Идентификаторы попыток:
-- $1 - uuid игры, $2 - uuid пациента
select array(
    select
        "id" as "ids" 
    from "started_games" 
    where 
        "game_patient_id" = $1 and 
        "date_end" is not null 
    order by "date_end"
);

-- Виджет времени прохождения уровня
-- $1 - id запущенной игры, $2 - uuid пациента
with check_rights as (
    select EXISTS(select null from "started_game" where "id" = $1 and "patient" = $2)
)
select
    null::text as "level_name",
    EXTRACT(epoch FROM ("date_end" - "date_start")) as "spend_time"
from "started_game"
where
    (TABLE check_rights)::boolean and
    "id" = $1

UNION ALL 

select 
    "level_name",
    sum(EXTRACT(epoch FROM ("date_end" - "date_start"))) as "spend_time"
from "game_statistics" 
where 
    (TABLE check_rights)::boolean and
    "sg_id" = $1
group by "level", "level_name"
order by "level";


-- Виджет кликов
-- $1 - id запущенной игры, $2 - uuid пациента
with check_rights as (
    select EXISTS(select null from "started_game" where "id" = $1 and "patient" = $2)
)
select 
    "level_name",
    sum("clicks") as "clicks",
    sum("missclicks") as "missclicks"
from "game_statistics" 
where 
    (TABLE check_rights)::boolean and
    "sg_id" = $1
group by "level", "level_name"
order by "level";


-- Виджет правильных и неправильных ответов
-- $1 - id запущенной игры, $2 - uuid пациента
with check_rights as (
    select EXISTS(select null from "started_game" where "id" = $1 and "patient" = $2)
)
select 
    "level_name",
    sum(*) filter (where "Type" = 1) as "correct",
    sum(*) filter (where "Type" = 2) as "incorrect"
from "game_statistics" 
where 
    (TABLE check_rights)::boolean and
    "sg_id" = $1
group by "level", "level_name"
order by "level";


-- Виджет игрового времени
-- $1 - id запущенной игры, $2 - uuid пациента
with check_rights as (
    select EXISTS(select null from "started_game" where "id" = $1 and "patient" = $2)
)
select 
    "level_name",
    "date_start",
    "date_end"
from "game_statistics" 
where 
    (TABLE check_rights)::boolean and
    "sg_id" = $1
order by "level", "date_start";


"""