package com.bargenson.urlshortener.model;

import java.net.URL;

/**
 * Created by bargenson on 2015-05-14.
 */
public class RegisteredUrl {

    private final String id;
    private final URL value;

    public RegisteredUrl(String id, URL value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public URL getUrl() {
        return value;
    }

}
