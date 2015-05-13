package com.bargenson.urlshortener.rest;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.*;

/**
 * Created by bargenson on 2015-05-13.
 */
@Path("/")
public class UrlResource {

    @GET
    @Path("{id: [a-zA-Z0-9]+}")
    public String resolve(@PathParam("id") String id) {
        throw new NotImplementedException();
    }

    @POST
    @Path("create")
    @Consumes("application/x-www-form-urlencoded")
    public String create(@FormParam("url") String originalUrl) {
        throw new NotImplementedException();
    }

}
