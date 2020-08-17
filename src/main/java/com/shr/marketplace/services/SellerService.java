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
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Seller create(Seller seller) { return sellerRepository.create(seller); }
    public Optional<Seller> findById(String id) { return sellerRepository.findById(id); }
}