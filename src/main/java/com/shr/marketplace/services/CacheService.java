package com.shr.marketplace.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author shruti.mishra
 */
@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> template;

    private final Logger logger = LoggerFactory.getLogger(CacheService.class);

    public Object getValue(final String key) {
        return template.opsForValue().get( key );
    }

    public void setValue(final String key, final Object value, Date expiryDate) {

        final var currentDate = new Date();
        final var milliSeconds = expiryDate.getTime() - currentDate.getTime();
        logger.info("current="+currentDate);
        logger.info("expiryDate="+expiryDate);
        logger.info("milliSeconds="+milliSeconds);
        template.opsForValue().set(key, value);
        template.expire( key, milliSeconds, TimeUnit.MILLISECONDS);

        logger.info("value in cache");
        logger.info(getValue(key).toString());
    }
}
