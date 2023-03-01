cd /opt
rm -rf web
git clone -b main https://github.com./XuDongZe/web.git/
cd web

docker build . -t xudongze/tomcat-springmvc-web:latest
docker container stop myweb && docker container rm myweb
docker run -d --name myweb --network server-web xudongze/tomcat-springmvc-web:latest