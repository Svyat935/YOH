import json
from flask import Blueprint, make_response, render_template, request, session, abort, send_from_directory
from jinja2.exceptions import TemplateNotFound
from requests import get
from .service_page import render_template_wo_statistics

game_bp = Blueprint('Game', __name__, template_folder='../games', static_folder='../games', url_prefix='/games')


@game_bp.route('/<game>/<path:path>')
def static_route(game, path):
    response = make_response(game_bp.send_static_file(f'{game}/{path}'))
    if path.split('.')[-1] == 'gz':
        response.headers['Content-Encoding'] = 'gzip'
    return response


@game_bp.route('/<game>/')
def game_route(game):
    if game.count('.'):
        return send_from_directory('./', game)

    if not request.args.get('token'):
        abort(401)

    session['user'] = request.args.get('token')
    template = None

    use_statistics = request.args.get('use_statistics')
    if str(use_statistics).lower() == 'false':
        url = f'/games/{game}/?token={request.args["token"]}'
        return make_response(render_template_wo_statistics(url))

    try:
        template = render_template(f'{game}/index.html')
    except TemplateNotFound:
        abort(404)

    session['current_game'] = game

    resp = make_response(template)

    headers = {
        'Content-Type': 'application/json',
        'token': session['user'],
        'game': session['current_game']
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/additional_fields'
    result = get(send_url, headers=headers).json()

    session['additional_fields'] = json.dumps(result.get('jsonObject', {}).get('result'))

    return resp
