package com.shr.marketplace.controllers;

import com.shr.marketplace.config.BaseTest;
import com.shr.marketplace.models.Seller;
import com.shr.marketplace.services.SellerService;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for
 * SellerController
 *
 * @author shruti.mishra
 */
@ExtendWith(RandomBeansExtension.class)
@ExtendWith(MockitoExtension.class)
public class SellerControllerTest extends BaseTest {

    @InjectMocks
    private SellerController sellerController;

    @Mock
    private SellerService sellerService;

    @Test
    void shouldGetSellerByIdWhenIdExists(@Random String sellerId, @Random Seller seller) {
        when(sellerService.findById(sellerId)).thenReturn(Optional.of(seller));

        final var response = sellerController.get(sellerId);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(seller));
    }
    @Test
    void shouldReturnNotFoundHttpStatusOnGetSellerByIdWhenIdDoesNotExist(@Random String sellerId) {
        when(sellerService.findById(sellerId)).thenReturn(Optional.empty());

        final var response = sellerController.get(sellerId);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertNull(response.getBody());
    }

    @Test
    void shouldCreateSeller(@Random String sellerId, @Random Seller sellerToCreate,
                            @Random Seller sellerCreated) throws Exception {

        sellerCreated.assignId(sellerId);

        when(sellerService.create(sellerToCreate)).thenReturn(sellerCreated);
        final var response = sellerController.create(sellerToCreate);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), is(sellerCreated));
    }
}
