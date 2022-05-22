from flask import Blueprint, render_template, send_from_directory, make_response
from werkzeug.exceptions import HTTPException
from flask_cors import CORS, cross_origin


service_bp = Blueprint('Service', __name__, template_folder='../service_pages', static_folder='../service_pages')
CORS(service_bp, resources={r"../service_pages/staticfiles/*": {"origins": "*"}})


@service_bp.app_errorhandler(HTTPException)
def error_handler_route(error):
    return render_template(f'error_page/index.html', error=error)


@service_bp.route('/staticfiles/<file>')
@cross_origin()
def get_files(file):
    return send_from_directory('../service_pages/staticfiles', file)


@service_bp.route('/<path:path>')
def static_service_route(path):
    return send_from_directory('../service_pages/dashboard', path)


@service_bp.route('/statistics')
def statistics_route():
    return make_response(render_template('dashboard/index.html'))
