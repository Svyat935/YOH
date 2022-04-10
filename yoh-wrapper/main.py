'''

from flask import Flask, make_response, render_template, request, session, abort, send_from_directory
from jinja2.exceptions import TemplateNotFound
from werkzeug.exceptions import HTTPException
from requests import post
from datetime import datetime, date
import json

app = Flask(__name__, template_folder='games', static_folder='games')
app.secret_key = 'rIVIE9]}e`ep9OhGf;ZTan/I~V86fRBKf:mY^U;C4d-Si7+)ElW.h2xeJXX=~zH'


def json_serial(obj):
    """JSON serializer for objects not serializable by default json code"""

    if isinstance(obj, (datetime, date)):
        return obj.isoformat()
    raise TypeError("Type %s not serializable" % type(obj))


@app.route('/<game>/<path:path>')
def static_route(game, path):
    return app.send_static_file(f'{game}/{path}')


@app.route('/<game>/')
def game_route(game):
    if game.count('.'):
        return send_from_directory('./', game)

    if not request.args.get('token'):
        abort(401)

    session['user'] = request.args.get('token')

    try:
        resp = make_response(render_template(f'{game}/index.html'))
    except TemplateNotFound:
        abort(404)

    return resp


@app.errorhandler(HTTPException)
def error_handler_route(error):
    return render_template(f'error_page/index.html', error=error)


@app.route('/post_statistic', methods=['POST'])
def test_post2():
    data = json.loads(request.data)
    headers = {
        'Content-Type': 'application/json'
    }
    url = 'https://a2d8-188-120-52-188.ngrok.io'

    post(url + '/insert_statistics', data=json.dumps({
        'token': session['user'],
        'date': datetime.strptime(data['startTime'], "%d.%m.%Y, %H:%M:%S"),
        'action': 'game_start',
        'game': 'a9a65ebe-fe9b-4563-8455-7fac72ab2515'
    }, default=json_serial), headers=headers)

    for _ in range(len(data['missClicks'])):
        post(url + '/insert_statistics', data=json.dumps({
            'token': session['user'],
            'date': datetime.strptime(data['endTime'], "%d.%m.%Y, %H:%M:%S"),
            'action': 'game_missclick',
            'game': 'a9a65ebe-fe9b-4563-8455-7fac72ab2515'
        }, default=json_serial), headers=headers)

    if data['selectedOption'] != '4':
        post(url + '/insert_statistics', data=json.dumps({
            'token': session['user'],
            'date': datetime.strptime(data['endTime'], "%d.%m.%Y, %H:%M:%S"),
            'action': 'game_wrong_ans',
            'game': 'a9a65ebe-fe9b-4563-8455-7fac72ab2515'
        }, default=json_serial), headers=headers)

    post(url + '/insert_statistics', data=json.dumps({
        'token': session['user'],
        'date': datetime.strptime(data['endTime'], "%d.%m.%Y, %H:%M:%S"),
        'action': 'game_end',
        'game': 'a9a65ebe-fe9b-4563-8455-7fac72ab2515'
    }, default=json_serial), headers=headers)
    # print('типо отправлено')
    return make_response()


if __name__ == '__main__':
    app.run(host='0.0.0.0')

'''
from app import create_flask_app

if __name__ == "__main__":
    create_flask_app().run(host='0.0.0.0')
