FROM openjdk:8-alpine
VOLUME /tmp
EXPOSE 61100
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/emerald-jwt-auth-1.0.0.jar /usr/share/app.jar
ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/app.jar"]