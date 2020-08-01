package com.shr.marketplace.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;

/**
 * Buyer entity
 *
 * @author shruti.mishra
 *
 */

@Document(collection = "buyers")
public class Buyer extends BaseDocument {

    @NotBlank
    @Indexed
    @Field(Fields.name)
    private String name;

    private Buyer() {
    }

    public Buyer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public interface Fields {
        String id = "id";
        String name = "name";
    }
}