package com.bargenson.urlshortener.service;

import com.bargenson.urlshortener.UnknownShortenedUrlException;
import com.bargenson.urlshortener.UrlShorteningException;
import com.bargenson.urlshortener.dao.RegisteredUrlDao;
import com.bargenson.urlshortener.generator.UrlIdentifierGenerator;
import com.bargenson.urlshortener.model.RegisteredUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by bargenson on 2015-05-14.
 */
@Service
public class ShorteningService {

    @Autowired
    protected URL baseUrl;

    @Autowired
    private UrlIdentifierGenerator urlIdentifierGenerator;

    @Autowired
    private RegisteredUrlDao registeredUrlDao;

    public URL shortenUrl(String url) {
        try {
            final URL urlToShorten = new URL(url);
            return retrieveOrCreateRegisteredUrl(urlToShorten);
        } catch (MalformedURLException e) {
            throw new UrlShorteningException("The URL to shorten is invalid: " + url, e);
        }
    }

    private URL retrieveOrCreateRegisteredUrl(URL urlToShorten) throws MalformedURLException {
        try {
            return buildCompleteShortenedUrl(registeredUrlDao.findRegisteredUrl(urlToShorten));
        } catch (UnknownShortenedUrlException e) {
            final RegisteredUrl registeredUrl = new RegisteredUrl(
                    urlIdentifierGenerator.generate(),
                    urlToShorten
            );
            registeredUrlDao.createRegisteredUrl(registeredUrl);
            return buildCompleteShortenedUrl(registeredUrl);
        }
    }

    private URL buildCompleteShortenedUrl(RegisteredUrl registeredUrl) throws MalformedURLException {
        return new URL(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPort(), "/" + registeredUrl.getId());
    }

    public URL resolveUrl(String urlId) {
        final RegisteredUrl registeredUrl = registeredUrlDao.getRegisteredUrlById(urlId);
        return registeredUrl.getUrl();
    }
}
