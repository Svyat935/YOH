from flask import Blueprint, make_response, render_template, request, session, abort, send_from_directory
from jinja2.exceptions import TemplateNotFound
from requests import get

game_bp = Blueprint('Game', __name__, template_folder='../games', static_folder='../games', url_prefix='/games')


@game_bp.route('/<game>/<path:path>')
def static_route(game, path):
    return game_bp.send_static_file(f'{game}/{path}')


@game_bp.route('/<game>/')
def game_route(game):
    if game.count('.'):
        return send_from_directory('./', game)

    if not request.args.get('token'):
        abort(401)

    session['user'] = request.args.get('token')

    headers = {
        'Content-Type': 'application/json',
        'token': session['user'],
        'game': session['current_game']
    }
    send_url = 'http://yoh-backend:8080/patient/games/statistics/additional_fields'
    session['additional_fields'] = get(send_url, headers=headers)

    try:
        resp = make_response(render_template(f'{game}/index.html'))
        session['current_game'] = game
    except TemplateNotFound:
        abort(404)

    return resp
