package com.shr.marketplace.exceptions.http;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Returns 409 in case the entity already exists
 *
 * @author shruti.mishra
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEntityException extends DuplicateKeyException {
    public DuplicateEntityException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
