package com.bargenson.urlshortener.integration;

import com.bargenson.urlshortener.ApplicationServer;
import com.bargenson.urlshortener.model.RegisteredUrl;
import com.jayway.restassured.RestAssured;
import com.mongodb.*;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.RedirectConfig.redirectConfig;
import static com.jayway.restassured.config.RestAssuredConfig.newConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;


/**
 * Created by bargenson on 2015-05-16.
 */
public class UrlShortenerIntegrationTest {

    public static final String DB_NAME = "clip";
    public static final String COLLECTION_NAME = "registeredUrl";
    public static final String HOST = "localhost";

    private Properties properties;

    private MongodExecutable mongodExecutable;
    private ApplicationServer applicationServer;

    @Before
    public void setUp() throws Exception {
        startMongo();
        startApplicationServer();
        RestAssured.config = newConfig().redirect(redirectConfig().followRedirects(false));
    }

    @After
    public void tearDown() {
        stopApplicationServer();
        stopMango();
    }

    @Test
    public void should_createNewShortenedUrl() throws IOException {
        // Given
        String url = "http://www.google.fr?plop=*&truc=$";
        given().param("url", url)

                // When
                .when().post("/")

                // Then
                .then().statusCode(200);
        final DBObject urlDocument = getUrlDocumentByUrl(url);
        assertThat(urlDocument).isNotNull();
        assertThat(urlDocument.get("url")).isEqualTo(url);
    }

    @Test
    public void should_reuseExistingShortenedUrl() throws IOException {
        // Given
        final RegisteredUrl existingUrl = new RegisteredUrl("1234", new URL("http://www.google.fr"));
        createUrlDocument(existingUrl);
        given().param("url", existingUrl.getUrl().toString())

                // When
                .when().post("/")

                // Then
                .then().statusCode(200).body(equalTo(getApplicationBaseUrl() + existingUrl.getId()));
    }

    @Test
    public void should_redirectOnTheOriginalUrl() throws IOException {
        // Given
        final RegisteredUrl existingUrl = new RegisteredUrl("1234", new URL("http://www.google.fr"));
        createUrlDocument(existingUrl);
        given()

                // When
                .when().get("/" + existingUrl.getId())

                // Then
                .then().statusCode(303).header("Location", is(existingUrl.getUrl().toString()));
    }

    @Test
    public void should_return404IfUrlDoesNotExist() throws IOException {
        // Given
        given()

                // When
                .when().get("/1234")

                // Then
                .then().statusCode(404);
    }

    private void startMongo() throws IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(getMongoPort(), Network.localhostIsIPv6()))
                .build();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
    }

    private void createUrlDocument(RegisteredUrl registeredUrl) throws IOException {
        DBCollection col = getDbCollection();
        col.save(
                BasicDBObjectBuilder.start()
                        .add("_id", registeredUrl.getId())
                        .add("url", registeredUrl.getUrl().toString())
                        .get()
        );
    }

    private DBObject getUrlDocumentByUrl(String url) throws IOException {
        DBCollection col = getDbCollection();
        return col.findOne(
                BasicDBObjectBuilder.start().add("url", url).get()
        );
    }

    private DBCollection getDbCollection() throws IOException {
        MongoClient mongo = new MongoClient(getMongoHost(), getMongoPort());
        DB db = mongo.getDB(DB_NAME);
        return db.createCollection(COLLECTION_NAME, null);
    }

    private String getMongoHost() throws IOException {
        return getApplicationProperty("mongodb.host");
    }

    private String getApplicationBaseUrl() throws IOException {
        return getApplicationProperty("baseUrl");
    }

    private int getMongoPort() throws IOException {
        return Integer.parseInt(getApplicationProperty("mongodb.port"));
    }

    private void stopMango() {
        mongodExecutable.stop();
    }

    private void startApplicationServer() throws Exception {
        applicationServer = new ApplicationServer();
        applicationServer.start();
    }

    private void stopApplicationServer() {
        applicationServer.stop();
    }

    private String getApplicationProperty(String name) throws IOException {
        if (properties == null) {
            properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        }
        return (String) properties.get(name);
    }

}
