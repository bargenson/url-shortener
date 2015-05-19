# URL Shortener

A URL shortener server in Java using Embedded Jetty, Spring, Spring Data MongoDB and packaged as a Docker image.


## Getting Started

To build the application:

    mvn clean install

This command will compile, execute the tests, package an executable Jar file and generate a Docker image for you.


To run the application without Docker:
 
    cd target/
    java -jar url-shortener-1.0-SNAPSHOT.jar
    
or with Docker (including a MongoDB container):
    
    docker-compose up
    
