package com.shr.marketplace.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;

/**
 * Seller entity
 *
 * @author shruti.mishra
 *
 */

@Document(collection = "sellers")
public class Seller extends BaseDocument {

    @NotBlank
    @Indexed
    @Field(Fields.name)
    private String name;

    private Seller() {
    }

    public Seller(String name) {
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