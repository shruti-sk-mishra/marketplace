package com.shr.marketplace.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;

/**
 * Entity class for the bids placed against a project
 * Note:-
 * Technically, it should be the part of projects
 * collection (a field). But, since the only bid, would any
 * project care about, is the one with the lowest bid
 * amount (selected one). Rest of the bids will be
 * kept for the auditing purpose. Hence, this should
 * be considered as a separate entity altogether
 *
 * @author shruti.mishra
 */
@Document(collection = "project_bids")
public class ProjectBid extends BaseDocument {


    @NotBlank
    @Indexed
    @Field(Fields.projectId)
    private String projectId;

    @NotBlank
    @Field(Fields.buyerId)
    private String buyerId;

    @NotBlank
    @Field(Fields.bidAmount)
    private  double bidAmount;

    @NotBlank
    @Field(Fields.wageType)
    private WageType wageType;

    public interface Fields {
        String id = "id";
        String projectId = "projectId";
        String buyerId = "buyerId";
        String bidAmount = "bidAmount";
        String wageType = "wageType";
    }

    public enum WageType {
        FIXED, HOURLY
    }
}
