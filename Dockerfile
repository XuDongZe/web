# First stage: complete build environment
FROM maven:3.5.0-jdk-8-alpine AS builder
# add pom.xml and source code
ADD ./src src/
ADD ./pom.xml pom.xml
ADD ./maven-settings.xml maven-settings.xml
# package jar
RUN mvn clean package --settings maven-settings.xml