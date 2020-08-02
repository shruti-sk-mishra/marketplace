package com.shr.marketplace.models;

import com.shr.marketplace.models.requirements.Requirement;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

/**
 * Seller entity
 *
 * @author shruti.mishra
 *
 */

@Document(collection = "projects")
public class Project extends BaseDocument {

    @NotBlank
    @Indexed
    @Field(Fields.name)
    private String name;

    @NotBlank
    @Indexed
    @Field(Fields.type)
    private ProjectType type;

    @NotBlank
    @Field(Fields.description)
    private String description;

    @Field(Fields.requirements)
    private Set<Requirement> requirements;

    @Field(Fields.expiresAt)
    private Date expiresAt;

    @Field(Fields.selectedBid)
    private String selectedBid;

    public Project(@NotBlank String name, @NotBlank ProjectType type,
                   @NotBlank String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public ProjectType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Set<Requirement> getRequirements() {
        return requirements;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public String getSelectedBid() {
        return selectedBid;
    }

    public interface Fields {
        String id = "id";
        String name = "name";
        String type = "type";
        String description = "description";
        String requirements = "requirements";
        String expiresAt = "expiresAt";
        String selectedBid = "selectedBid";
    }

    public enum Status {
        ACTIVE, EXPIRED
    }

}