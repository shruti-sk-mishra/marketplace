package com.shr.marketplace.services;

import com.shr.marketplace.config.mongo.BaseTest;
import com.shr.marketplace.models.Seller;
import com.shr.marketplace.repositories.SellerRepository;
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
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit test cases for
 * SellerService
 *
 * @author shruti.mishra
 */
@ExtendWith(RandomBeansExtension.class)
@ExtendWith({MockitoExtension.class})
class SellerServiceTest extends BaseTest {
    @InjectMocks
    private SellerService sellerService;

    @Mock
    private SellerRepository sellerRepository;

    @Test
    void shouldGetSellerById(@Random Seller seller, @Random String sellerId) {
        seller.assignId(sellerId);
        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        final var retrievedSeller = sellerService.get(sellerId);
        assertThat(retrievedSeller, optionalWithValue(is(seller)));
    }

    @Test
    void shouldNotGetSellerByIdWhenIdDoesNotExist(@Random String sellerId) {
        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        final var retrievedSeller = sellerService.get(sellerId);
        assertFalse(retrievedSeller.isPresent());
    }
}