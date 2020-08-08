package com.shr.marketplace.repositories.mongo;

import com.shr.marketplace.exceptions.http.EntityNotFoundException;
import com.shr.marketplace.models.BaseDocument;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Field;
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

    @Override
    public T update(T updatedEntity) {
        BaseDocument existingDocument = (BaseDocument)this.mongoTemplate.findById(updatedEntity.getId(), updatedEntity.getClass());
        if (existingDocument != null) {
            updatedEntity.assignId(existingDocument.getId());
            return updateTheDocument(updatedEntity);
        } else {
            throw new EntityNotFoundException("Could not find entity [" + updatedEntity.getId() + "] for update");
        }
    }

    @Override
    public T merge(T updatedEntity) throws IllegalAccessException, NoSuchFieldException {

            final var existingDocument = (T) this.mongoTemplate.findById(updatedEntity.getId(), updatedEntity.getClass());
            if (existingDocument != null) {
                copyDiff(existingDocument, updatedEntity);
                return update(existingDocument);
            } else {
                throw new EntityNotFoundException("Could not find entity [" + updatedEntity.getId() + "] for update");
            }

    }

    private T updateTheDocument(T updatedEntity) {
        updatedEntity.setUpdatedAt(new Date());
        return (T) this.mongoTemplate.save(updatedEntity);
    }

    private void copyDiff(T destination, T source) throws
            IllegalAccessException, NoSuchFieldException {
        for (Field field : source.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = field.get(source);
            //If it is a non null value copy to destination
            if (null != value) {
                Field destField = destination.getClass().getDeclaredField(name);
                destField.setAccessible(true);
                destField.set(destination, value);
            }
        }
    }
}
