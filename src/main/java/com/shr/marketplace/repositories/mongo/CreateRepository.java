package com.shr.marketplace.repositories.mongo;

import com.shr.marketplace.models.BaseDocument;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateRepository<T extends BaseDocument> {
    T create(T var1) throws DuplicateKeyException;
}
