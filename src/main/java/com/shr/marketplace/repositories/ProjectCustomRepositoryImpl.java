package com.shr.marketplace.repositories;

import com.shr.marketplace.models.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author shruti.mishra
 */
@Component
public class ProjectCustomRepositoryImpl implements ProjectCustomRepository {

    private final MongoOperations mongoOperations;

    public ProjectCustomRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<Project> findActiveProjectsByExpiryDate(Date startDate, Date endDate, Pageable pageable) {
        final var query = new Query();
        query.addCriteria(Criteria.where(Project.Fields.expiresAt).gte(startDate).lt(endDate));
        return mongoOperations.find(query.with(pageable), Project.class);
    }
}
