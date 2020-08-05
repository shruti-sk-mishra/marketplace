package com.shr.marketplace.config.mongo;

import org.jeasy.random.EasyRandom;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


/**
 * Contains the boilerplate code for
 * each test class
 *
 */
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class BaseTest {
    private final EasyRandom easyRandom = new EasyRandom();

    public BaseTest() {
    }

    /** @deprecated */
    @Deprecated
    protected <T> T createDummy(Class<T> clazz) {
        return this.easyRandom.nextObject(clazz);
    }
}
