package com.shr.marketplace.repositories;

import com.shr.marketplace.models.Project;
import com.shr.marketplace.repositories.mongo.CreateRepository;
import com.shr.marketplace.repositories.mongo.UpdateRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Buyer entity
 *
 * @author shruti.mishra
 *
 */
@Repository
public interface ProjectRepository extends MongoRepository<Project, String>,
        CreateRepository<Project>, UpdateRepository<Project>, ProjectCustomRepository {
}