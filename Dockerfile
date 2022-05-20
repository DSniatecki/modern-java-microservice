FROM openjdk:17

ARG JVM_FLAGS="-XX:+UseZGC"

ENV JVM_FLAGS ${JVM_FLAGS}
ENV EXECUTABLE="run/application.jar"

COPY target/dependency-jars /run/dependency-jars
ADD target/application.jar ${EXECUTABLE}

ENTRYPOINT java ${JVM_FLAGS} -jar ${EXECUTABLE}