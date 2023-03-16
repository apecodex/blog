#!/bin/bash

echo '                                    __   ';
echo '   ____ _____  ___  _________  ____/ /__ ';
echo '  / __ `/ __ \/ _ \/ ___/ __ \/ __  / _ \';
echo ' / /_/ / /_/ /  __/ /__/ /_/ / /_/ /  __/';
echo ' \__,_/ .___/\___/\___/\____/\__,_/\___/ ';
echo '     /_/                                 ';

# 项目目录
folder="/blog"

if [ -d $folder ]; then
	read -p "检测到项目文件夹已存在，是否将其删除后重新创建？y/n: " isDeleteFolder;
  if [ $isDeleteFolder = "y" ]; then
    rm -rf $folder;
  else
    echo "exit";
    exit;
  fi
fi


mkdir -p ${folder}/redis/conf ${folder}/redis/data ${folder}/rabbitmq ${folder}/mariadb/sql ${folder}/mariadb/db ${folder}/nginx/cert ${folder}/nginx/logs ${folder}/nginx/upload ${folder}/nginx/www ${folder}/nginx/www/admin ${folder}/nginx/www/blog;
cat << EOF > ${folder}/redis/conf/redis.conf
# bind 0.0.0.0
protected-mode yes
port 6379
dir /data
daemonize no
requirepass "密码"
logfile "redis.log"
dbfilename redis.rdb
appendfilename "appendonly.aof"
appendonly yes
activedefrag yes
active-defrag-ignore-bytes 100mb
active-defrag-cycle-min 1
active-defrag-cycle-max 25
EOF

cat << EOF > ${folder}/nginx/nginx.conf
worker_processes  1;
events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    keepalive_timeout  65;

    client_max_body_size     50m;
    client_body_buffer_size  10m; 
    client_header_timeout    1m;
    client_body_timeout      1m;


    gzip on;
    gzip_buffers 16 8k;
    gzip_comp_level 6;
    gzip_http_version 1.1;
    gzip_min_length 256;
    gzip_proxied any;
    gzip_vary on;
    gzip_types 
              text/xml application/xml application/atom+xml application/rss+xml application/xhtml+xml image/svg+xml text
              text/javascript application/javascript application/x-javascript
              text/x-json application/json application/x-web-app-manifest+json
              text/css text/plain text/x-component
              font/opentype application/x-font-ttf application/vnd.ms-fontobject 
              image/x-icon;


    server {
        listen       443 ssl;
        server_name  前台域名;
        
        ssl_certificate           /usr/local/nginx/cert/xxx.pem;
        ssl_certificate_key       /usr/local/nginx/cert/xxx.key;
        ssl_session_cache  builtin:1000  shared:SSL:10m;
        ssl_session_timeout 5m;
        ssl_prefer_server_ciphers on;
        ssl_protocols  TLSv1.1 TLSv1.2 TLSv1.3;
        ssl_ciphers HIGH:!aNULL:!eNULL:!EXPORT:!CAMELLIA:!DES:!MD5:!PSK:!RC4:!RSA;
        
        add_header Strict-Transport-Security "max-age=31536000; includeSubDomains； preload" always;
        add_header 'X-XSS-Protection' '1; mode=block' always;
        
        location / {
            root   /www/blog;
            index  index.html index.htm;
            try_files \$uri \$uri/ /index.html;	
        }

        location ^~/api/ {
            proxy_ignore_client_abort   on; 
            proxy_pass http://ip:8081/;
            proxy_set_header   Host             \$host;
            proxy_set_header   X-Real-IP        \$remote_addr;						
            proxy_set_header   X-Forwarded-For  \$proxy_add_x_forwarded_for;
        }

    }

    server {
        listen       443 ssl;
        server_name  后台域名;

        ssl_certificate           /usr/local/nginx/cert/xxx.pem;
        ssl_certificate_key       /usr/local/nginx/cert/xxx.key;
        ssl_session_cache  builtin:1000  shared:SSL:10m;
        ssl_session_timeout 5m;
        ssl_prefer_server_ciphers on;
        ssl_protocols  TLSv1.1 TLSv1.2 TLSv1.3;
        ssl_ciphers HIGH:!aNULL:!eNULL:!EXPORT:!CAMELLIA:!DES:!MD5:!PSK:!RC4:!RSA;
        
        add_header Strict-Transport-Security "max-age=31536000; includeSubDomains； preload" always;
        add_header 'X-XSS-Protection' '1; mode=block' always;
        
        location / {
            root   /www/admin;
            index  index.html index.htm;
            try_files \$uri \$uri/ /index.html;	
        }

        location ^~/api/ {
            proxy_ignore_client_abort   on; 
            proxy_pass http://ip:8081/;
            proxy_set_header   Host             \$host;
            proxy_set_header   X-Real-IP        \$remote_addr;						
            proxy_set_header   X-Forwarded-For  \$proxy_add_x_forwarded_for;
        }
    }

    server {
        listen       443 ssl;
        server_name  文件子域名;

        ssl_certificate           /usr/local/nginx/cert/xxx.pem;
        ssl_certificate_key       /usr/local/nginx/cert/xxx.key;
        ssl_session_cache  builtin:1000  shared:SSL:10m;
        ssl_session_timeout 5m;
        ssl_prefer_server_ciphers on;
        ssl_protocols  TLSv1.1 TLSv1.2 TLSv1.3;
        ssl_ciphers HIGH:!aNULL:!eNULL:!EXPORT:!CAMELLIA:!DES:!MD5:!PSK:!RC4:!RSA;
        
        add_header Strict-Transport-Security "max-age=31536000; includeSubDomains； preload" always;
        add_header 'X-XSS-Protection' '1; mode=block' always;
        
        location / {
            root   /usr/local/upload/;
        }
    }
}
EOF

cat << EOF > ${folder}/docker-compose.yml
version: '3.9'
services:
  redis:
    image: redis:6.2.7
    container_name: redis_blog
    ports:
      - 6371:6379
    volumes:
      - ${folder}/redis/conf/redis.conf:/etc/redis/redis.conf
      - ${folder}/redis/data:/data
    networks:
      - blog
    command: redis-server /etc/redis/redis.conf

  mariadb:
    image: mariadb:10.6.8
    container_name: mariadb_blog
    ports:
      - 3306:3306
    volumes:
      - ${folder}/mariadb/db/:/var/lib/mysql
      - ${folder}/mariadb/sql/:/docker-entrypoint-initdb.d/
    environment:
      - MARIADB_ROOT_PASSWORD=数据库密码
    networks:
      - blog

  rabbitmq:
    image: rabbitmq:3.9.18-management
    container_name: rabbitmq_blog
    ports:
      - 5672:5672
      - 15672:15672
    volumes:
      - ${folder}/rabbitmq/:/var/lib/rabbitmq
    environment:
      - RABBITMQ_DEFAULT_USER=账号
      - RABBITMQ_DEFAULT_PASS=密码
    networks:
      - blog

  myblog:
    image: myblog:latest
    container_name: myblog
    build: 
      context: ${folder}/
      dockerfile: Dockerfile-BlogJar
    ports:
      - 8081:8081
    networks:
      - blog
    depends_on:
      - redis
      - mariadb
      - rabbitmq

  nginx:
    image: nginx:1.23.3
    container_name: nginx_blog
    ports:
      - 80:80
      - 443:443
    volumes:
      - ${folder}/nginx/nginx.conf:/etc/nginx/nginx.conf
      - ${folder}/nginx/logs:/var/log/nginx
      - ${folder}/nginx/www:/www
      - ${folder}/nginx/cert:/usr/local/nginx/cert
      - ${folder}/nginx/upload/:/usr/local/upload/
    environment:
      - TZ=Asia/Shanghai
    networks:
      - blog
    depends_on:
      - myblog

networks:
  blog:
    name: blog
EOF

cat << EOF > ${folder}/Dockerfile-BlogJar
FROM openjdk:8-jre
MAINTAINER apecode<apecode@qq.com>
RUN echo "Asia/Shanghai" > /etc/timezone
EXPOSE 8081
ADD blog-api.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
EOF

echo
echo "项目文件初始化完成，请根据文档修改相应配置";
echo ">>> cd ${folder}"
echo ">>> ls"
echo