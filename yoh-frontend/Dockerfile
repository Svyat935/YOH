FROM node:12-alpine as builder
WORKDIR /app
COPY package.json /app/package.json
RUN npm i --silent
RUN mkdir ./src
RUN mkdir ./public
COPY ./public ./public
COPY ./src ./src

RUN npm config set proxy http://yoh-backend:8080
CMD npm start

