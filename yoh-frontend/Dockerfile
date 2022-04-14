FROM node:12-alpine as builder
WORKDIR /app
COPY package.json /app/package.json
RUN npm i --silent
RUN mkdir ./src
RUN mkdir ./public
COPY ./public ./public
COPY ./src ./src
CMD npm start

