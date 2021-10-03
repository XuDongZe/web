# First stage: complete build environment
FROM maven:3.5.0-jdk-8-alpine AS builder
# add pom.xml and source code
WORKDIR /opt/tomcat/webapps/
ADD ./src src/
ADD ./pom.xml pom.xml
ADD ./maven-settings.xml maven-settings.xml
# package jar
RUN mvn clean package --settings maven-settings.xml

# Second stage: applaction containerd
FROM tomcat:8.5.49-jdk8-openjdk
WORKDIR /usr/local/tomcat/
COPY --from=builder /opt/tomcat/webapps/target/*.war webapps/
# rename war
RUN mv /usr/local/tomcat/webapps/*war /usr/local/tomcat/webapps/web.war
# change tomcat port: 8010
RUN sed -i 's|"8080"|"8010"|' server.xml
CMD ["catalina.sh", "run"]