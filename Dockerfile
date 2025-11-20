# Build stage
FROM gradle:7.4.2-jdk11 AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle clean
RUN gradle build --no-daemon -x test

# Package stage
FROM tomcat:9.0-jdk11-openjdk
COPY --from=build /home/gradle/src/build/libs/*.war /usr/local/tomcat/webapps/legacy-java-app.war
EXPOSE 8080
CMD ["catalina.sh", "run"]