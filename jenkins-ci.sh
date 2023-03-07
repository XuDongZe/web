docker build . -t xudongze/tomcat-springmvc-web:latest && \
docker container stop web
docker container rm web && \

docker run -d \
--name web \
--port "8010:8010" \
--network server-web \
xudongze/tomcat-springmvc-web:latest