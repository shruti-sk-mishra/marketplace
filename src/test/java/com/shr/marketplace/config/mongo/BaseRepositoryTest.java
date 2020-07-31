package com.shr.marketplace.config.mongo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * Contains the boilerplate for
 * each repository test class
 *
 * @author shruti.mishra
 */
@ExtendWith({SpringExtension.class})
public class BaseRepositoryTest extends BaseTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    public BaseRepositoryTest() {
    }

    @BeforeEach
    public void setUp() {
        this.mongoTemplate.getCollectionNames().forEach((collection) -> {
            this.mongoTemplate.remove(new Query(), collection);
        });
    }
}
