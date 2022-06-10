from flask import Blueprint, make_response, render_template, request, session, abort, send_from_directory
from jinja2.exceptions import TemplateNotFound
from .service_page import render_template_wo_statistics

# Инициализируем модуль Game
game_bp = Blueprint('Game', __name__, template_folder='../games', static_folder='../games', url_prefix='/games')


@game_bp.route('/<game>/<path:path>')
def static_route(game, path):
    """
    Выполняет отправку статических файлов из нужной директории

    Принимает на вход:
        game - uuid - идентификатор игры
        path - str - путь до файла. Он может быть многоуровневым, например "assets/script.js"
    """
    response = make_response(game_bp.send_static_file(f'{game}/{path}'))
    if path.split('.')[-1] == 'gz':
        response.headers['Content-Encoding'] = 'gzip'
    return response


@game_bp.route('/<game>/')
def game_route(game):
    """
    Основной метод для получения игры

    Принимает на вход:
        game - uuid - идентификатор игры

        args:
            token - str - токен пользователя
            *use_statistics - str - флаг использования класса по сбору статистики

    * - опционально
    """
    if game.count('.'):
        return send_from_directory('./', game)

    if not request.args.get('token'):
        abort(401)

    session['user'] = request.args.get('token')
    template = None

    # Если игра не использует класс по сбору статистики, то отобразим сначала пустой шаблон со скриптом для корректного
    # выхода из игры. Внутри этого шаблона запустим игру в iframe уже без флага use_statistics
    use_statistics = request.args.get('use_statistics')
    if str(use_statistics).lower() == 'false':
        url = f'/games/{game}/?token={request.args["token"]}'
        resp = make_response(render_template_wo_statistics(url))
        resp.set_cookie('EndGame', '', expires=0)
        return resp

    try:
        template = render_template(f'{game}/index.html')
    except TemplateNotFound:
        abort(404)

    session['current_game'] = game

    resp = make_response(template)
    resp.set_cookie('EndGame', '', expires=0)

    return resp
