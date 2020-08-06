package com.shr.marketplace.repositories.mongo;

import com.shr.marketplace.models.BaseDocument;

public interface UpdateRepository<T extends BaseDocument> {
    T update(T var1);
    T merge(T var1) throws NoSuchFieldException, IllegalAccessException;
}
