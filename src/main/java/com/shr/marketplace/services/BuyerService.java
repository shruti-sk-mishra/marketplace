package com.shr.marketplace.services;

import com.shr.marketplace.models.Buyer;
import com.shr.marketplace.repositories.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * Contains the service methods of
 * buyer entity
 *
 * @author shruti.mishra
 */
@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public Buyer create(Buyer buyer) { return buyerRepository.create(buyer); }
    public Optional<Buyer> findById(String id) {
        return buyerRepository.findById(id);
    }
}