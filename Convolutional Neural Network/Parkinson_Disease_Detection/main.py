from flask import Flask, request
from flask_restful import Api, Resource

from src.classifier import Classifier

app = Flask(__name__)
api = Api(app)


class Predict(Resource):
    def get(self) -> str:
        id = request.args.get('id')
        name = request.args.get('name')
        token = request.args.get('token')
        classifier = Classifier(id, name, token)
        result = classifier.predict()
        return result


api.add_resource(Predict, '/predict/')

if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0')
