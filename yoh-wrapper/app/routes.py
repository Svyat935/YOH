from modules.game import game_bp
from modules.service_page import service_bp
from modules.api import api_bp
from modules.event import event_bp


def route(app):
    app.register_blueprint(game_bp)
    app.register_blueprint(service_bp)
    app.register_blueprint(api_bp)
    app.register_blueprint(event_bp)
