from modules.game import game_bp
from modules.service_page import service_bp


def route(app):
    app.register_blueprint(game_bp)
    app.register_blueprint(service_bp)