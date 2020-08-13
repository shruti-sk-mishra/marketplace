package com.shr.marketplace.controllers;

import com.shr.marketplace.config.BaseMVCTest;
import com.shr.marketplace.models.Seller;
import com.shr.marketplace.services.SellerService;
import io.github.glytching.junit.extension.random.Random;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests
 * for SellerController
 *
 * @author shruti.mishra
 */
@WebMvcTest(controllers = SellerController.class)
public class SellerControllerIntegrationTest extends BaseMVCTest {

    @MockBean
    private SellerService sellerService;

    @Test
    void shouldGetSellerById(@Random String sellerId, @Random Seller seller) throws Exception {
        when(sellerService.findById(sellerId)).thenReturn(Optional.of(seller));

        mockMvc.perform(get("/seller/" + sellerId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(seller)));
    }

    @Test
    void shouldReturn404WhenSellerWithIdDoesNotExist(@Random String sellerId) throws Exception {
        when(sellerService.findById(sellerId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/seller/" + sellerId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateSeller(@Random Seller sellerToBeCreated, @Random Seller sellerCreated) throws Exception{
        when(sellerService.create(sellerToBeCreated)).thenReturn(sellerCreated);

        final var createRequestJson = objectMapper.valueToTree(sellerCreated).toString();

        mockMvc.perform(post("/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestJson))
                .andExpect(status().isCreated());
    }
}