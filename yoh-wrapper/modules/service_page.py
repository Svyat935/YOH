from flask import Blueprint, render_template
from werkzeug.exceptions import HTTPException


service_bp = Blueprint('Service', __name__, template_folder='../service_pages', static_folder='../service_pages')


@service_bp.app_errorhandler(HTTPException)
def error_handler_route(error):
    return render_template(f'error_page/index.html', error=error)
