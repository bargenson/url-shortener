package com.bargenson.urlshortener.service;

import com.bargenson.urlshortener.UrlShorteningException;
import com.bargenson.urlshortener.dao.RegisteredUrlDao;
import com.bargenson.urlshortener.generator.UrlIdentifierGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by bargenson on 2015-05-14.
 */
public class ShorteningServiceTest {

    @Mock
    private UrlIdentifierGenerator urlIdentifierGenerator;

    @Mock
    private RegisteredUrlDao registeredUrlDao;

    private URL baseUrl;

    @InjectMocks
    private ShorteningService service;

    @Before
    public void setUp() throws Exception {
        service = new ShorteningService();
        baseUrl = new URL("http://cl.ip");
        service.baseUrl = baseUrl;
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_generateShortenedUrl_when_urlIsValid() throws MalformedURLException {
        // Given
        String urlToShorten = "http://google.fr";
        String simpleId = "qwerty1234";
        when(urlIdentifierGenerator.generate()).thenReturn(simpleId);

        // When
        URL shortenUrl = service.shortenUrl(urlToShorten);

        // Then
        assertThat(shortenUrl).isNotNull();
        assertThat(shortenUrl.toString()).isEqualTo(baseUrl + "/" + simpleId);
    }

    @Test(expected = UrlShorteningException.class)
    public void should_throwException_when_urlIsNotValid() throws MalformedURLException {
        // Given
        String urlToShorten = "BadProtocol:/~B@dH0st]::BadPort";

        // When
        service.shortenUrl(urlToShorten);
    }

}