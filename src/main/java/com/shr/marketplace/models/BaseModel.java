package com.shr.marketplace.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Contains the methods required to be overridden
 * by every model class
 *
 * @author shruti.mishra
 *
 */
public class BaseModel {
    public BaseModel() {
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, new String[0]);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[0]);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
