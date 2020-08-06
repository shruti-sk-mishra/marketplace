package com.shr.marketplace.repositories;

import com.shr.marketplace.models.Buyer;
import com.shr.marketplace.repositories.mongo.CreateRepository;
import com.shr.marketplace.repositories.mongo.IdGenerationListener;
import com.shr.marketplace.repositories.mongo.UpdateRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Buyer entity
 *
 * @author shruti.mishra
 *
 */
@Repository
public interface BuyerRepository extends MongoRepository<Buyer, String>, IdGenerationListener<Buyer>,
        CreateRepository<Buyer>, UpdateRepository<Buyer> {

    Optional<Buyer> findByName(String name);
}