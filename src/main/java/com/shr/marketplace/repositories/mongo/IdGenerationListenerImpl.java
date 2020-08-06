package com.shr.marketplace.repositories.mongo;

import com.shr.marketplace.models.BaseDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.util.Objects;
import java.util.UUID;

/**
 * @author shruti.mishra
 */
public class IdGenerationListenerImpl<T extends BaseDocument> implements IdGenerationListener<T> {
    public IdGenerationListenerImpl() {
    }

    @Bean
    public AbstractMongoEventListener<T> idGenerationListener() {
        return new AbstractMongoEventListener<T>() {
            public void onBeforeConvert(BeforeConvertEvent<T> event) {
                T source = (T) event.getSource();
                if (Objects.isNull(source.getId())) {
                    source.assignId(UUID.randomUUID().toString());
                }

                super.onBeforeConvert(event);
            }
        };
    }
}
