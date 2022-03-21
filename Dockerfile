FROM amazoncorretto:17-alpine-jdk

HEALTHCHECK --interval=1m --timeout=30s --start-period=5m --retries=5 \
  CMD curl --fail http://localhost:8080/v1/health || exit 1

RUN apk --update --no-cache add curl

COPY target/icblog-backend-*.jar icblog.jar

EXPOSE 8080
EXPOSE 5005

ENTRYPOINT ["java", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "icblog.jar"]