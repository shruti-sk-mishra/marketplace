package com.shr.marketplace.controllers;

import com.shr.marketplace.config.BaseMVCTest;
import com.shr.marketplace.exceptions.http.DuplicateEntityException;
import com.shr.marketplace.exceptions.http.EntityExpiredException;
import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.services.ProjectBidService;
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
 * @author shruti.mishra
 */
@WebMvcTest(controllers = ProjectBidController.class)
public class ProjectBidControllerIntegrationTest extends BaseMVCTest {

    @MockBean
    private ProjectBidService projectBidService;

    @Test
    void shouldCreateProjectBid(@Random String projectId, @Random String buyerId) throws Exception{

        final var projectBidToBeCreated = new ProjectBid(projectId, buyerId, 200, ProjectBid.WageType.FIXED);
        final var projectBidCreated = new ProjectBid(projectId, buyerId, 200, ProjectBid.WageType.FIXED);

        when(projectBidService.create(projectBidToBeCreated)).thenReturn(projectBidCreated);

        final var createRequestJson = objectMapper.valueToTree(projectBidCreated).toString();

        mockMvc.perform(post("/project-bid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldThrowEntityExpiredExceptionAndNotCreateProjectBidWhenProjectIsExpired(@Random String projectId,
                                                                                     @Random String buyerId) throws Exception{

        final var projectBidToBeCreated = new ProjectBid(projectId, buyerId, 200, ProjectBid.WageType.FIXED);
        final var projectBidCreated = new ProjectBid(projectId, buyerId, 200, ProjectBid.WageType.FIXED);

        when(projectBidService.create(projectBidToBeCreated)).thenThrow(EntityExpiredException.class);

        final var createRequestJson = objectMapper.valueToTree(projectBidCreated).toString();

        mockMvc.perform(post("/project-bid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestJson))
                .andExpect(status().isGone());
    }

    @Test
    void shouldThrowDuplicateEntityExceptionAndNotCreateProjectBidWhenBuyerHasAlreadyPlacedABidForProject(@Random String projectId,
                                                                                                          @Random String buyerId) throws Exception{

        final var projectBidToBeCreated = new ProjectBid(projectId, buyerId, 200, ProjectBid.WageType.FIXED);
        final var projectBidCreated = new ProjectBid(projectId, buyerId, 200, ProjectBid.WageType.FIXED);

        when(projectBidService.create(projectBidToBeCreated)).thenThrow(DuplicateEntityException.class);

        final var createRequestJson = objectMapper.valueToTree(projectBidCreated).toString();

        mockMvc.perform(post("/project-bid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldGetProjectBidById(@Random String projectBidId, @Random ProjectBid projectBid) throws Exception {
        when(projectBidService.get(projectBidId)).thenReturn(Optional.of(projectBid));

        mockMvc.perform(get("/project-bid/" + projectBidId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectBid)));
    }

    @Test
    void shouldReturn404WhenProjectBidWithIdDoesNotExist(@Random String projectBidId) throws Exception {
        when(projectBidService.get(projectBidId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/project-bid/" + projectBidId))
                .andExpect(status().isNotFound());
    }
}