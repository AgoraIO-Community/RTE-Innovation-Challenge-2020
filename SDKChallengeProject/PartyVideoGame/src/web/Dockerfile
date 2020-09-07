FROM node:14.0.0-alpine3.11 AS build
WORKDIR /app
COPY . /app
RUN npm install 
RUN npm rebuild node-sass
RUN npm run build

FROM nginx:1.16.0-alpine
COPY ./pipeline/nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/build /usr/share/nginx/html
COPY ./pipeline/entrypoint.sh /usr/share/nginx/html
EXPOSE 80
WORKDIR /usr/share/nginx/html
RUN chmod 777 ./entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]