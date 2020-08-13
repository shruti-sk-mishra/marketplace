package com.shr.marketplace.controllers;

import com.shr.marketplace.config.BaseMVCTest;
import com.shr.marketplace.models.Buyer;
import com.shr.marketplace.services.BuyerService;
import io.github.glytching.junit.extension.random.Random;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests
 * for BuyerController
 *
 * @author shruti.mishra
 */
@WebMvcTest(controllers = BuyerController.class)
public class BuyerControllerIntegrationTest extends BaseMVCTest {

    @MockBean
    private BuyerService buyerService;

    @Test
    void shouldGetBuyerById(@Random String buyerId, @Random Buyer buyer) throws Exception {
        when(buyerService.findById(buyerId)).thenReturn(Optional.of(buyer));

        mockMvc.perform(get("/buyer/" + buyerId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(buyer)));
    }

    @Test
    void shouldReturn404WhenBuyerWithIdDoesNotExist(@Random String buyerId) throws Exception {
        when(buyerService.findById(buyerId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/buyer/" + buyerId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBuyer(@Random Buyer buyerToBeCreated, @Random Buyer buyerCreated) throws Exception{
        when(buyerService.create(buyerToBeCreated)).thenReturn(buyerCreated);

        final var createRequestJson = objectMapper.valueToTree(buyerCreated).toString();

        mockMvc.perform(post("/buyer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestJson))
                .andExpect(status().isCreated());
    }
}