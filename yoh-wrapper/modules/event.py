import uuid
from queue import Queue
from flask import Blueprint, make_response, request, session, Response


event_bp = Blueprint('Event', __name__, url_prefix='/event')


class EventAnnouncer:
    def __init__(self, queue_maxsize):
        self.listeners = {}
        self.queue_maxsize = queue_maxsize

    def listen(self, user):
        q = Queue(maxsize=self.queue_maxsize)
        self.listeners[user] = q
        return q

    def announce(self, msg, user):
        if user in self.listeners:
            self.listeners[user].put_nowait(msg)

    def kill_listener(self, user):
        if user in self.listeners:
            del self.listeners[user]


EVENT_CHANNELS = {
    'endgame': EventAnnouncer(1)
}


def event_stream(channel, user, kill_after=None):
    messages = EVENT_CHANNELS[channel].listen(user)
    if kill_after:
        for _ in range(kill_after):
            msg = messages.get()
            yield f'id: {uuid.uuid4()}\ndata: {msg}\n\n'
    else:
        while True:
            msg = messages.get()
            yield f'id: {uuid.uuid4()}\ndata: {msg}\n\n'

    EVENT_CHANNELS[channel].kill_listener(user)


def event_post(channel, user, message):
    if channel not in EVENT_CHANNELS:
        return False

    EVENT_CHANNELS[channel].announce(message, user)
    return True


@event_bp.route('/stream')
def stream_route():
    channel = request.args['channel']
    response = make_response('', 404)
    if channel == 'endgame':
        response = Response(event_stream(channel, session['user'], None), mimetype="text/event-stream")
        response.headers['X-Accel-Buffering'] = 'no'
        response.headers['Cache-Control'] = 'no-cache'
        response.headers['Connection'] = 'keep-alive'
    return response
