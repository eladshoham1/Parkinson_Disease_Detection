import cv2
import requests
import numpy as np
from keras.models import load_model


class Classifier():
    IMAGE_PATH = './src/spiral_tests_images/spiral_test.png'
    MODEL_PATH = './src/models/parkinson_disease_detection_model.h5'

    def __init__(self, id: str, name: str, token: str) -> None:
        self.set_uri(id, name, token)
        self.set_model()

    def set_uri(self, id: str, name: str, token: str) -> None:
        self.__uri = f'https://firebasestorage.googleapis.com/v0/b/parkinson-disease-detection.appspot.com/o/Images%{id}%{name}?alt=media&token={token}'
        self.load_image_to_file()

    def load_image_to_file(self) -> None:
        print('uri:',self.__uri)
        response = requests.get(self.__uri)
        image = response.content
        with open(self.IMAGE_PATH, 'wb') as image_file:
            image_file.write(image)

    def set_model(self) -> None:
        self.__model = load_model(self.MODEL_PATH)

    def prepare_image(self):
        image = cv2.imread(self.IMAGE_PATH)
        image = cv2.resize(image, (128, 128))
        image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        image = np.array(image)
        image = np.expand_dims(image, axis=0)
        image = np.expand_dims(image, axis=-1)
        return image

    def predict(self) -> str:
        labels = ['Healthy', 'Parkinson']
        image = self.prepare_image()
        prediction = self.__model.predict(image)
        return labels[np.argmax(prediction[0], axis=0)]
