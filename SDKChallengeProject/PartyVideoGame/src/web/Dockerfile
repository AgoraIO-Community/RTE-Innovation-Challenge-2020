FROM nginx:1.16.0-alpine
COPY ./pipeline/nginx.conf /etc/nginx/nginx.conf
COPY ./build /usr/share/nginx/html
COPY ./pipeline/entrypoint.sh /usr/share/nginx/html
EXPOSE 80
WORKDIR /usr/share/nginx/html
RUN chmod 777 ./entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]