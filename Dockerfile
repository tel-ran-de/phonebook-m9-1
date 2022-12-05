#
# Build stage
#
FROM gradle:6.4.1-jdk14 AS build
ADD phonebook-ui /home/phonebook-ui
ADD phonebook-api /home/phonebook-api
ADD gradle /home/gradle
ADD gradlew /home
ADD gradlew.bat /home
ADD settings.gradle /home
ADD build.gradle /home
WORKDIR /home
RUN /home/gradlew buildUi
RUN /home/gradlew bootJarDev



#
# Package stage
#
FROM openjdk:14
COPY --from=build  /home/phonebook-api/build/libs/*.jar phonebook.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "phonebook.jar"]
