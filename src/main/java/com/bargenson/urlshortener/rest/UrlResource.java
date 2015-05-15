package com.bargenson.urlshortener.rest;

import com.bargenson.urlshortener.UnknownShortenedUrlException;
import com.bargenson.urlshortener.UrlShorteningException;
import com.bargenson.urlshortener.service.ShorteningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by bargenson on 2015-05-13.
 */
@Path("/")
public class UrlResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlResource.class);

    @Autowired
    private ShorteningService shorteningService;

    @GET
    @Path("{id: [a-zA-Z0-9]+}")
    public Response resolve(@PathParam("id") String id) throws URISyntaxException {
        try {
            final URL url = shorteningService.resolveUrl(id);
            return Response.seeOther(new URI(url.toString())).build();
        } catch (UnknownShortenedUrlException e) {
            LOGGER.debug("Impossible to resolve an URL", e);
            throw new WebApplicationException("URL doesn't exist", e, 404);
        }
    }

    @POST
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    public String create(@FormParam("url") String originalUrl) {
        try {
            return shorteningService.shortenUrl(originalUrl).toString();
        } catch (UrlShorteningException e) {
            LOGGER.debug("Bad request", e);
            throw new WebApplicationException("URL is not valid", e, 400);
        }
    }

}
