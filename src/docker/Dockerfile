FROM java:8
MAINTAINER sergej
VOLUME /tmp
ADD target/intellijob-1.2.0.jar intellijob-1.2.0.jar
RUN bash -c 'touch /intellijob-1.2.0.jar'
CMD ["java", "-jar", "intellijob-1.2.0.jar"]
EXPOSE 8080
