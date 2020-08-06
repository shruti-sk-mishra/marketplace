package com.shr.marketplace.repositories.mongo;

import com.shr.marketplace.models.BaseDocument;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

/**
 * Listener for creating the String IDs
 *
 * @author shruti.mishra
 */
public interface IdGenerationListener<T extends BaseDocument> {
    AbstractMongoEventListener<T> idGenerationListener();
}
