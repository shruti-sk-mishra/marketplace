package com.shr.marketplace.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


/**
 * A basic document skeleton
 * each model class must have
 *
 * @author shruti.mishra
 */
@Document
public abstract class BaseDocument extends BaseModel {
    public static final String IdField = "id";
    @Id
    @Field("id")
    private String id;
    @CreatedDate
    protected Date createdAt;
    @LastModifiedDate
    protected Date updatedAt;
   /* @Version
    protected Long version;*/

    public BaseDocument() {
    }

    public String getId() {
        return this.id;
    }

    public void assignId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
