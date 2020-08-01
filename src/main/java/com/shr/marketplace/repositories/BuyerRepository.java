package com.shr.marketplace.repositories;

import com.shr.marketplace.models.Buyer;
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
public interface BuyerRepository extends MongoRepository<Buyer, String> {

    Optional<Buyer> findByName(String name);
}