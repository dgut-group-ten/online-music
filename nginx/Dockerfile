FROM nginx
#对外暴露端口
RUN rm /etc/nginx/conf.d/default.conf
ADD nginx.conf  /etc/nginx/conf.d/
ADD ./cert/ /etc/nginx/cert/

