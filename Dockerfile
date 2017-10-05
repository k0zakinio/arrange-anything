FROM        openjdk:8

ENV         KOTLIN_VERSION=1.1.51 \
            KOTLIN_HOME=/usr/local/kotlin

ADD         . /code

COPY        build/libs/arrange-anything-all-1.0-SNAPSHOT.jar /code/runnable.jar

RUN         update-ca-certificates -f

EXPOSE      8080

CMD         java -jar /code/runnable.jar