package com.shr.marketplace.models;

import com.shr.marketplace.models.requirements.Requirement;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    @Field(Fields.sellerId)
    private String sellerId;

    @NotNull
    @Field(Fields.type)
    private ProjectType type;

    @NotBlank
    @Field(Fields.description)
    private String description;

    @Min(1)
    @Field(Fields.projectDurationInHours)
    private long projectDurationInHours;

    @Field(Fields.requirements)
    private Set<Requirement> requirements;

    @Field(Fields.expiresAt)
    private Date expiresAt;

    @Field(Fields.selectedBidId)
    private String selectedBidId;

    @Field(Fields.status)
    private Status status;

    // Required for object creation by ObjectMapper
    public Project() {}

    public Project(@NotBlank String name, @NotBlank String sellerId, @NotBlank ProjectType type,
                   @NotBlank String description, @NotBlank long projectDurationInHours) {
        this.name = name;
        this.sellerId = sellerId;
        this.type = type;
        this.description = description;
        this.projectDurationInHours = projectDurationInHours;
        status = Status.INACTIVE;
    }

    public String getName() {
        return name;
    }

    public String getSellerId() {
        return sellerId;
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

    public long getProjectDurationInHours() {
        return projectDurationInHours;
    }

    private void setExpiresAt(LocalDateTime date) {
        expiresAt = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public String getSelectedBidId() {
        return selectedBidId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public Status getStatus() {
        return status;
    }

    public void setSelectedBidId(String selectedBidId) {
        this.selectedBidId = selectedBidId;
    }

    public interface Fields {
        String sellerId = "sellerId";
        String name = "name";
        String type = "type";
        String description = "description";
        String requirements = "requirements";
        String expiresAt = "expiresAt";
        String selectedBidId = "selectedBidId";
        String status = "status";
        String projectDurationInHours = "projectDurationInHours";
    }

    public enum Status {
        INACTIVE, ACTIVE, EXPIRED
    }

}