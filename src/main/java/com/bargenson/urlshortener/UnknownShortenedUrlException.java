package com.bargenson.urlshortener;

import java.net.URL;

/**
 * Created by bargenson on 2015-05-15.
 */
public class UnknownShortenedUrlException extends RuntimeException {

    public UnknownShortenedUrlException(String id) {
        super("Unknown URL with ID: " + id);
    }

    public UnknownShortenedUrlException(URL url) {
        super("Unknown URL: " + url);
    }
}
