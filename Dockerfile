FROM openjdk:8-alpine
COPY ./build/libs/*.jar birthfit.jar
ENTRYPOINT ["java", "-Xmx200m", "-jar", "-Duser.timezone=Asia/Seoul", "/birthfit.jar"]
EXPOSE 3000
