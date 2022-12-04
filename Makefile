build-ui:
	./gradlew buildUi

# builds artefact into phonebook-api/build/libs/
build-jar-dev: build-ui
	cp phonebook-api/src/main/resources/application.properties_ phonebook-api/src/main/resources/application.properties && \
	./gradlew bootJarDev

build-docker-image-dev: build-jar-dev
	docker build -t phonebook .
