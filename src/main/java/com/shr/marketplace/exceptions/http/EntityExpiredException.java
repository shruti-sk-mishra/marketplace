package com.shr.marketplace.exceptions.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Returns 410 in case the entity was expired (no longer in effect)
 *
 * @author shruti.mishra
 */
@ResponseStatus(HttpStatus.GONE)
public class EntityExpiredException extends RuntimeException {
    public EntityExpiredException(String message) {
        super(message);
    }
}