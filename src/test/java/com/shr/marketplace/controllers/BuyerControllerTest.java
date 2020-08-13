package com.shr.marketplace.controllers;

import com.shr.marketplace.config.BaseTest;
import com.shr.marketplace.models.Buyer;
import com.shr.marketplace.services.BuyerService;
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
 * BuyerController
 *
 * @author shruti.mishra
 */
@ExtendWith(RandomBeansExtension.class)
@ExtendWith(MockitoExtension.class)
public class BuyerControllerTest extends BaseTest {

    @InjectMocks
    private BuyerController buyerController;

    @Mock
    private BuyerService buyerService;

    @Test
    void shouldGetBuyerByIdWhenIdExists(@Random String buyerId, @Random Buyer buyer) {
        when(buyerService.findById(buyerId)).thenReturn(Optional.of(buyer));

        final var response = buyerController.get(buyerId);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(buyer));
    }
    @Test
    void shouldReturnNotFoundHttpStatusOnGetBuyerByIdWhenIdDoesNotExist(@Random String buyerId) {
        when(buyerService.findById(buyerId)).thenReturn(Optional.empty());

        final var response = buyerController.get(buyerId);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertNull(response.getBody());
    }

    @Test
    void shouldCreateBuyer(@Random String buyerId, @Random Buyer buyerToCreate,
                            @Random Buyer buyerCreated) throws Exception {

        buyerCreated.assignId(buyerId);

        when(buyerService.create(buyerToCreate)).thenReturn(buyerCreated);
        final var response = buyerController.create(buyerToCreate);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), is(buyerCreated));
    }
}
