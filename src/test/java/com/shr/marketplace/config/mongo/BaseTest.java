package com.shr.marketplace.config.mongo;

import org.jeasy.random.EasyRandom;


/**
 * Contains the boilerplate code for
 * each test class
 *
 */
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
