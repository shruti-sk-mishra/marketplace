package com.shr.marketplace.controllers;

import com.shr.marketplace.models.Buyer;
import com.shr.marketplace.services.BuyerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Contains the endpoints for
 * Buyer entity
 *
 * @author shruti.mishra
 */
@RestController
@RequestMapping("/buyer")
public class BuyerController {

    private final BuyerService buyerService;
    private final Logger logger = LoggerFactory.getLogger(BuyerController.class);

    public BuyerController(BuyerService buyerService) { this.buyerService = buyerService; }


    @GetMapping("/{id}")
    public ResponseEntity<Buyer> get(@PathVariable("id") String buyerId) {
        final var buyer = buyerService.findById(buyerId);
        return buyer.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Buyer> create(@Valid @RequestBody Buyer buyer) throws Exception {

        logger.info("Attempting to create the buyer={}", buyer);
        final var createdBuyer = buyerService.create(buyer);
        logger.info("Buyer created={}", createdBuyer);
        return new ResponseEntity<>(createdBuyer, HttpStatus.CREATED);
    }
}