from flask import Blueprint, render_template, send_from_directory, make_response
from werkzeug.exceptions import HTTPException
from flask_cors import CORS, cross_origin

# Инициализируем модуль служебных страниц
service_bp = Blueprint('Service', __name__, template_folder='../service_pages', static_folder='../service_pages')
# Указываем, что по этому адресу разрешаются кросс-доменные запросы
CORS(service_bp, resources={r"../service_pages/staticfiles/*": {"origins": "*"}})


@service_bp.app_errorhandler(HTTPException)
def error_handler_route(error):
    """
    Общий обработчик ошибок. Отображает на интерфейсе страницу с кодом ошибки
    """
    return make_response(render_template(f'error_page/index.html', error=error), error.code)


@service_bp.route('/staticfiles/<file>')
@cross_origin()
def get_files(file):
    """
    Возвращает статические файлы, расположенные в service_pages/staticfiles
    """
    return send_from_directory('../service_pages/staticfiles', file)


@service_bp.route('/<path:path>')
def static_service_route(path):
    """
    Возвращает статические файлы для демо страницы со статистикой
    """
    return send_from_directory('../service_pages/dashboard', path)


@service_bp.route('/statistics')
def statistics_route():
    """
    Возвращает демо страницу со статистикой
    """
    return make_response(render_template('dashboard/index.html'))


def render_template_wo_statistics(url):
    """
    Выполняет рендер страницы для игр без класса по сбору статистики

    Принимает на вход:
        url - str - адрес игры
    """
    return render_template('template_wo_statistics/index.html', url=url)
