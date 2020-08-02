package com.shr.marketplace.controllers;

import com.shr.marketplace.config.mongo.BaseTest;
import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.services.ProjectBidService;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for
 * ProjectBidController
 *
 * @author shruti.mishra
 */

@ExtendWith(RandomBeansExtension.class)
@ExtendWith(MockitoExtension.class)
public class ProjectBidControllerTest extends BaseTest {

    @InjectMocks
    private ProjectBidController projectBidController;

    @Mock
    private ProjectBidService projectBidService;

    @Test
    void shouldCreateProject(@Random ProjectBid projectBidToBeSaved) {

        final var savedProjectBid = new ProjectBid(projectBidToBeSaved.getProjectId(), projectBidToBeSaved.getBuyerId(),
                projectBidToBeSaved.getBidAmount(), projectBidToBeSaved.getWageType());

        when(projectBidService.create(projectBidToBeSaved)).thenReturn(savedProjectBid);

        final var response = projectBidController.create(projectBidToBeSaved);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), is(savedProjectBid));
    }

    @Test
    void shouldGetProjectBidsById(@Random String projectId, @Random ProjectBid projectBid) throws IllegalAccessException {

        FieldUtils.getField(ProjectBid.class, "projectId", true).set(projectBid, projectId);

        when(projectBidService.get(projectId)).thenReturn(Optional.of(projectBid));

        final var response = projectBidController.get(projectId);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), projectBid);
    }
}
