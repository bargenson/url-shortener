package com.bargenson.urlshortener.generator;

/**
 * Created by bargenson on 2015-05-14.
 */
public class Base36TimestampUrlIdentifierGenerator implements UrlIdentifierGenerator {

    public String generate() {
        final long timestamp = System.currentTimeMillis();
        return Long.toString(timestamp, 36);
    }

}
