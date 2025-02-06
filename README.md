# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.2/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.2/reference/web/servlet.html)
* [Ollama](https://docs.spring.io/spring-ai/reference/api/chat/ollama-chat.html)
* [PDF Document Reader](https://docs.spring.io/spring-ai/reference/api/etl-pipeline.html#_pdf_page)
* [PGvector Vector Database](https://docs.spring.io/spring-ai/reference/api/vectordbs/pgvector.html)
* [Docker Compose Support](https://docs.spring.io/spring-boot/3.4.2/reference/features/dev-services.html#features.dev-services.docker-compose)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* pgvector: [`pgvector/pgvector:pg16`](https://hub.docker.com/r/pgvector/pgvector)

Please review the tags of the used images and set them to the same as you're running in production.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### The steps that should be taken to run this project.
* install ollama and docker desktop on your local computer.
* pull model llama3.2:1b using ollama cli; the command is `ollama pull llama3.2:1b`.
* then run command `ollama serve` to serve ollama before running its model.
* then open another cmd terminal and run the model using `ollama run llama3.2:1b` command.
* clone this project into your local repository.  
* make the necessary adjustments to be able to run a spring boot project with java version 23 and spring version 3.3.4.
* also depending on the type of llm you want to choose, you should:
* Set the following in `application.properties`:
```properties
spring.profiles.active=ollama/openai 
```
* Depending on what you chose the llm you are going to use will differ and so will the other configurations you should make:
* for openai you should set the api key for your account to have access to your api.
* i have set the compose file for both llms the same (`compose.yaml`) which you can build and run using `docker-compose -f compose.yaml -d` , but if you want to, perhaps, run ollama on a container, you can use the compose-ollama.yaml file for your dockerized project and run `docker-compose -f compose-ollama.yaml -d`. In which case, you may use the following commands to pull the necessary models into your container's ollama:
```bash
docker exec -it ollama ollama pull nomic-embed-text llama3.2:1b
```
* and further verify the models' existence using the following command:
```bash
docker exec -it ollama ollama list
```
* Of course, you can choose other models for embedding vectors or chatting with according to your taste.
* Finally, start the application and use apis `/query` and `/upload` to communicate with spring project server and llms connected to it.
* When you are done, don't forget to stop the project and the container both.
* Have fun playing. ;) 
