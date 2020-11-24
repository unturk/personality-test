FROM openjdk:8-jre
MAINTAINER galaksiya

COPY target/personality-api*.jar	/home/personality-api.jar
COPY target/lib/*	/home/lib/

RUN rm /etc/localtime
RUN echo "Europe/Istanbul" > /etc/timezone
RUN dpkg-reconfigure -f noninteractive tzdata

ENV JAVA_TOOL_OPTIONS "-Xms128m -Xmx1024m"

CMD ["java","-Dfile.encoding=UTF-8","-jar","/home/personality-api.jar"]
