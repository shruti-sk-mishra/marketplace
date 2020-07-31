package com.shr.marketplace.repositories;

import com.shr.marketplace.models.Seller;
import com.shr.marketplace.repositories.mongo.UpdateRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Seller entity
 *
 * @author shruti.mishra
 *
 */
@Repository
public interface SellerRepository extends MongoRepository<Seller, String> {

    Optional<Seller> findByName(String shortId);
}