# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.example.rag-server' is invalid and this project uses 'com.example.rag_server' instead.

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
* install ollama on your local computer.
* pull model llama3.2:1b using ollama cli; the command is `ollama pull llama3.2:1b`.
* then run the model using `ollama run llama3.2:1b` command.
* clone this project into your local repository.  
* make the necessary adjustments to be able to run a spring boot project with java version 23 and spring version 3.4.2.
* finally, just run the project.
  * The pgvector container will be run automatically, its database created as it is set in `compose.yml` file's configurations, 
  * and then, the project will ingest vector embeddings of the pdf in the `Docs/` directory into the database.
  * the connection is made to the llama3.2:1b model as well.
  * and then you can send your queries using curl or other tools such as postman or the http request services implemented in intellij idea.
    * one sample of the structure of the request to be sent to the project is as follows:
      * `POST http://localhost:8080/rag/query?
              prompt={{$random.alphanumeric(8)}}
              Content-Type: application/x-www-form-urlencoded`
  * after sending your prompt about the pdf file's data ingested in pgvector database, wait for a couple seconds for the llm to process your answer.
  * after processing, you will get the closest answer the llm has got based on the information provided from the database.
* When you are done, don't forget to stop the project and the container both.
* Have fun playing. ;) 