package com.shr.marketplace.repositories.mongo;

import com.shr.marketplace.exceptions.http.EntityNotFoundException;
import com.shr.marketplace.models.BaseDocument;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

/**
 *
 *
 * @author shruti.mishra
 * @param <T>
 */
public class UpdateRepositoryImpl<T extends BaseDocument> implements UpdateRepository<T> {
    private final MongoTemplate mongoTemplate;

    public UpdateRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public T update(T updatedEntity) {
        BaseDocument existingDocument = (BaseDocument)this.mongoTemplate.findById(updatedEntity.getId(), updatedEntity.getClass());
        if (existingDocument != null) {
            updatedEntity.assignId(existingDocument.getId());
            updatedEntity.setUpdatedAt(new Date());
            return (T) this.mongoTemplate.save(updatedEntity);
        } else {
            throw new EntityNotFoundException("Could not find entity [" + updatedEntity.getId() + "] for update");
        }
    }
}
