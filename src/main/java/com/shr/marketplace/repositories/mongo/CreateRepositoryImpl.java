package com.shr.marketplace.repositories.mongo;

import com.shr.marketplace.exceptions.http.DuplicateEntityException;
import com.shr.marketplace.models.BaseDocument;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

/**
 *
 * @author shruti.mishra
 * @param <T>
 */
public class CreateRepositoryImpl<T extends BaseDocument> implements CreateRepository<T> {
    private final MongoTemplate mongoTemplate;

    public CreateRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public T create(T document) {
        try {
            final var currentDate = new Date();
            return (T)this.mongoTemplate.insert(document);
        } catch (DuplicateKeyException exception) {
            throw new DuplicateEntityException(exception.getMessage(), exception);
        }
    }
}
