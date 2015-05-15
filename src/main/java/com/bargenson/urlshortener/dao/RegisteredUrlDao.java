package com.bargenson.urlshortener.dao;

import com.bargenson.urlshortener.model.RegisteredUrl;

import java.net.URL;

/**
 * Created by bargenson on 2015-05-14.
 */
public interface RegisteredUrlDao {

    RegisteredUrl getRegisteredUrlById(String id);

    void createRegisteredUrl(RegisteredUrl registeredUrl);

    RegisteredUrl findRegisteredUrl(URL url);
}
