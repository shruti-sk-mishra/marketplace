package com.shr.marketplace.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    protected Date createdAt;
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    protected Date updatedAt;
    @Version
    protected Long version;

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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static Set<String> auditingFields = new HashSet<>();
    static {
        auditingFields.add("createdAt");
        auditingFields.add("updatedAt");
        auditingFields.add("version");
    }
}
