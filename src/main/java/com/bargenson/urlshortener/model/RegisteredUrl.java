package com.bargenson.urlshortener.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;

/**
 * Created by bargenson on 2015-05-14.
 */
@Document
public class RegisteredUrl {

    private final String id;
    private final URL url;

    public RegisteredUrl(String id, URL url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public URL getUrl() {
        return url;
    }

}
