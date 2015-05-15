package com.bargenson.urlshortener.dao.mongodb;

import com.bargenson.urlshortener.UnknownShortenedUrlException;
import com.bargenson.urlshortener.dao.RegisteredUrlDao;
import com.bargenson.urlshortener.model.RegisteredUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.net.URL;

/**
 * Created by bargenson on 2015-05-14.
 */
@Repository
public class MongoRegisteredUrlDao implements RegisteredUrlDao {

    @Autowired
    private MongoOperations mongoOps;

    public RegisteredUrl getRegisteredUrlById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        RegisteredUrl result = this.mongoOps.findOne(query, RegisteredUrl.class);
        if (result == null) {
            throw new UnknownShortenedUrlException(id);
        }
        return result;
    }

    public void createRegisteredUrl(RegisteredUrl registeredUrl) {
        mongoOps.save(registeredUrl);
    }

    public RegisteredUrl findRegisteredUrl(URL url) {
        Query query = new Query(Criteria.where("url").is(url.toString()));
        RegisteredUrl result = this.mongoOps.findOne(query, RegisteredUrl.class);
        if (result == null) {
            throw new UnknownShortenedUrlException(url);
        }
        return result;
    }

}
