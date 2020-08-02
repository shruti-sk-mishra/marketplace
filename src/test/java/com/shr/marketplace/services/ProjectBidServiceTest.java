package com.shr.marketplace.services;

import com.shr.marketplace.config.mongo.BaseTest;
import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.repositories.ProjectBidRepository;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit test cases for ProjectBidService
 *
 * @author shruti.mishra
 */

@ExtendWith(RandomBeansExtension.class)
@ExtendWith(MockitoExtension.class)
public class ProjectBidServiceTest extends BaseTest {

    @InjectMocks
    private ProjectBidService projectBidService;

    @Mock
    private ProjectBidRepository projectBidRepository;

    @Test
    void shouldCreateProjectBidService(@Random ProjectBid projectBidToBeSaved, @Random String savedProjectId) {

        final var savedProjectBid = new ProjectBid(projectBidToBeSaved.getProjectId(), projectBidToBeSaved.getBuyerId(),
                projectBidToBeSaved.getBidAmount(), projectBidToBeSaved.getWageType());
        savedProjectBid.assignId(savedProjectId);

        when(projectBidRepository.create(projectBidToBeSaved)).thenReturn(savedProjectBid);

        final var persistedProjectBid = projectBidService.create(projectBidToBeSaved);
        assertEquals(savedProjectBid, persistedProjectBid);
    }

    @Test
    void shouldGetProjectBidById(@Random String projectBidId, @Random ProjectBid projectBid) {

        projectBid.assignId(projectBidId);

        when(projectBidRepository.findById(projectBidId)).thenReturn(Optional.of(projectBid));

        final var retrievedProjectBid = projectBidService.get(projectBidId).orElseThrow();
        assertEquals(projectBid, retrievedProjectBid);
    }

    @Test
    void shouldNotGetProjectBidWhenIdNotFound(@Random String projectBidId) {
        when(projectBidRepository.findById(projectBidId)).thenReturn(Optional.empty());

        final var retrievedProjectBid = projectBidService.get(projectBidId);
        assertFalse(retrievedProjectBid.isPresent());
    }

    @Test
    void shouldGetProjectBidsByProjectId(@Random String projectId, @Random ProjectBid firstProjectBid,
            @Random ProjectBid secondProjectBid, @Random ProjectBid thirdProjectBid) throws IllegalAccessException {

        FieldUtils.getField(ProjectBid.class, "projectId", true).set(firstProjectBid, projectId);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(secondProjectBid, projectId);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(thirdProjectBid, projectId);

        final var projectBidsList = List.of(firstProjectBid, secondProjectBid, thirdProjectBid);
        when(projectBidRepository.findByProjectId(projectId)).thenReturn(projectBidsList);

        final var retrievedProjectsList = projectBidService.getByProjectId(projectId);
        assertEquals(retrievedProjectsList, projectBidsList);
    }
}
