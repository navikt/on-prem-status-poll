FROM maven:3.6.3-openjdk-17 as maven

COPY pom.xml pom.xml

COPY . .

RUN mvn clean install

RUN mvn dependency:go-offline -B

RUN mvn package

FROM openjdk:17

#RUN dir #Added

WORKDIR /adevguide


#RUN dir #Added


COPY --from=maven target/on-prem-status-poll-0.0.1-SNAPSHOT.jar ./on-prem-status-poll.jar

CMD ["java", "-jar", "./on-prem-status-poll.jar"]