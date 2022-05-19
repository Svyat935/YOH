import os
import configparser

config = configparser.ConfigParser()


def init_config(path):
    config.optionxform = str
    config.read(path)
