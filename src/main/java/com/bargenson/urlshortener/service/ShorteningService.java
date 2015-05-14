package com.bargenson.urlshortener.service;

import com.bargenson.urlshortener.UrlShorteningException;
import com.bargenson.urlshortener.generator.UrlIdentifierGenerator;
import com.bargenson.urlshortener.model.RegisteredUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bargenson on 2015-05-14.
 */
@Service
public class ShorteningService {

    @Autowired
    protected URL baseUrl;

    @Autowired
    private UrlIdentifierGenerator urlIdentifierGenerator;


    public URL shortenUrl(String urlToShorten) {
        try {
            final RegisteredUrl registeredUrl = new RegisteredUrl(
                    urlIdentifierGenerator.generate(),
                    new URL(urlToShorten)
            );
            return new URL(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPort(), "/" + registeredUrl.getId());
        } catch (MalformedURLException e) {
            throw new UrlShorteningException("The URL to shorten is invalid: " + urlToShorten, e);
        }
    }

}
