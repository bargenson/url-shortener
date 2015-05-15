package com.bargenson.urlshortener.service;

import com.bargenson.urlshortener.UnknownShortenedUrlException;
import com.bargenson.urlshortener.UrlShorteningException;
import com.bargenson.urlshortener.dao.RegisteredUrlDao;
import com.bargenson.urlshortener.generator.UrlIdentifierGenerator;
import com.bargenson.urlshortener.model.RegisteredUrl;
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
    public void should_generateShortenedUrl_when_urlIsValidAndDoesNotExist() throws MalformedURLException {
        // Given
        URL urlToShorten = new URL("http://google.fr");
        String simpleId = "qwerty1234";
        when(urlIdentifierGenerator.generate()).thenReturn(simpleId);
        when(registeredUrlDao.findRegisteredUrl(urlToShorten)).thenThrow(UnknownShortenedUrlException.class);

        // When
        URL shortenedUrl = service.shortenUrl(urlToShorten.toString());

        // Then
        assertThat(shortenedUrl).isNotNull();
        assertThat(shortenedUrl.toString()).isEqualTo(baseUrl + "/" + simpleId);
    }

    @Test
    public void should_notGenerateNewShortenedUrl_when_urlAlreadyExists() throws MalformedURLException {
        // Given
        RegisteredUrl registeredUrl = new RegisteredUrl("qwerty1234", new URL("http://google.fr"));
        when(registeredUrlDao.findRegisteredUrl(registeredUrl.getUrl())).thenReturn(registeredUrl);

        // When
        URL shortenedUrl = service.shortenUrl(registeredUrl.getUrl().toString());

        // Then
        assertThat(shortenedUrl).isNotNull();
        assertThat(shortenedUrl.getPath()).isEqualTo("/" + registeredUrl.getId());
    }

    @Test(expected = UrlShorteningException.class)
    public void should_throwException_when_urlIsNotValid() throws MalformedURLException {
        // Given
        String urlToShorten = "BadProtocol:/~B@dH0st]::BadPort";

        // When
        service.shortenUrl(urlToShorten);
    }

    @Test
    public void should_resolveShortenedUrl_when_urlIdExists() throws MalformedURLException {
        // Given
        String urlId = "qwerty1234";
        URL url = new URL("http://google.fr");
        when(registeredUrlDao.getRegisteredUrlById(urlId)).thenReturn(new RegisteredUrl(urlId, url));

        // When
        URL originalUrl = service.resolveUrl(urlId);

        // Then
        assertThat(originalUrl).isNotNull().isEqualTo(url);
    }

}