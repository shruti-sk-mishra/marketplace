package com.shr.marketplace.services;

import com.shr.marketplace.models.Seller;
import com.shr.marketplace.repositories.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * Contains the service methods of
 * seller entity
 *
 * @author shruti.mishra
 */
@Service
public class SellerService {
    private SellerRepository sellerRepository;

    public Optional<Seller> get(String id) {
        return sellerRepository.findById(id);
    }
}