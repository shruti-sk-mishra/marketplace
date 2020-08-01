package com.shr.marketplace.services;

import com.shr.marketplace.config.mongo.BaseTest;
import com.shr.marketplace.models.Buyer;
import com.shr.marketplace.repositories.BuyerRepository;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for
 * BuyerService
 *
 * @author shruti.mishra
 */
@ExtendWith(RandomBeansExtension.class)
@ExtendWith({MockitoExtension.class})
class BuyerServiceTest extends BaseTest {
    @InjectMocks
    private BuyerService buyerService;

    @Mock
    private BuyerRepository buyerRepository;

    @Test
    void shouldGetBuyerById(@Random Buyer buyer, @Random String buyerId) {
        buyer.assignId(buyerId);
        when(buyerRepository.findById(buyerId)).thenReturn(Optional.of(buyer));

        final var retrievedBuyer = buyerService.get(buyerId);
        assertThat(retrievedBuyer, optionalWithValue(is(buyer)));
    }

    @Test
    void shouldNotGetBuyerByIdWhenIdDoesNotExist(@Random String buyerId) {
        when(buyerRepository.findById(buyerId)).thenReturn(Optional.empty());

        final var retrievedBuyer = buyerService.get(buyerId);
        assertFalse(retrievedBuyer.isPresent());
    }
}