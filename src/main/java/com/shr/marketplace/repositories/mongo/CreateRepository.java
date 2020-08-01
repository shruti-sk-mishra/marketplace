package com.shr.marketplace.repositories.mongo;

import com.shr.marketplace.models.BaseDocument;

public interface CreateRepository<T extends BaseDocument> {
    T create(T var1);
}
