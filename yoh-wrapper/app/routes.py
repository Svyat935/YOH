from modules.game import game_bp
from modules.service_page import service_bp
from modules.api import api_bp


def route(app):
    """
    Регистрируем модули Flask сервера
    """
    app.register_blueprint(game_bp)
    app.register_blueprint(service_bp)
    app.register_blueprint(api_bp)
