from app import create_flask_app

if __name__ == "__main__":
    # Указываем host именно такой, чтобы Flask сервер корректно работал в Docker окружении
    create_flask_app().run(host='0.0.0.0')
