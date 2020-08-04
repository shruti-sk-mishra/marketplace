package com.shr.marketplace.repositories;

import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.repositories.mongo.CreateRepository;
import com.shr.marketplace.repositories.mongo.UpdateRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ProjectBid entity
 *
 * @author shruti.mishra
 */
@Repository
public interface ProjectBidRepository extends MongoRepository<ProjectBid, String>,
        CreateRepository<ProjectBid>, UpdateRepository<ProjectBid> {

    List<ProjectBid> findByProjectId(String projectId);

    Optional<ProjectBid> findOneByProjectIdAndBuyerId(String projectId, String buyerId);
}
