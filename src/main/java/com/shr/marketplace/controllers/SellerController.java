package com.shr.marketplace.controllers;

import com.shr.marketplace.models.Seller;
import com.shr.marketplace.services.SellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author shruti.mishra
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;
    private final Logger logger = LoggerFactory.getLogger(SellerController.class);

    public SellerController(SellerService sellerService) { this.sellerService = sellerService; }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> get(@PathVariable("id") String sellerId) {
        final var seller = sellerService.findById(sellerId);
        return seller.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Seller> create(@Valid @RequestBody Seller seller) throws Exception {

        logger.info("Attempting to create the seller={}", seller);
        final var createdSeller = sellerService.create(seller);
        logger.info("Seller created={}", createdSeller);
        return new ResponseEntity<>(createdSeller, HttpStatus.CREATED);
    }
}