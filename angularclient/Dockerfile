### STAGE 1: Build ###
FROM node:12.7-alpine AS build
ENV PATH /app/frontend/node_modules/.bin:$PATH

WORKDIR /app/frontend
COPY package.json /app/frontend/package.json
RUN npm install
RUN npm install -g @angular/cli
COPY . /app/frontend
EXPOSE 4200
CMD ng serve --host 0.0.0.0

