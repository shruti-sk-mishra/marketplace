package com.shr.marketplace.services;

import com.shr.marketplace.config.mongo.BaseTest;
import com.shr.marketplace.exceptions.http.DuplicateEntityException;
import com.shr.marketplace.exceptions.http.EntityExpiredException;
import com.shr.marketplace.exceptions.http.EntityNotFoundException;
import com.shr.marketplace.models.Project;
import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.models.ProjectType;
import com.shr.marketplace.repositories.ProjectBidRepository;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Mock
    private ProjectService projectService;

    @Test
    void shouldCreateProjectBid(@Random ProjectBid projectBidToBeSaved, @Random Project project) {

        project.assignId("PROJECT_001");
        project.setStatus(Project.Status.ACTIVE);

        doReturn(Optional.of(project)).when(projectService).findById(anyString());

        when(projectBidRepository.findOneByProjectIdAndBuyerId(projectBidToBeSaved.getProjectId(),projectBidToBeSaved.getBuyerId()))
                .thenReturn(Optional.empty());

        final var savedProjectBid = new ProjectBid(projectBidToBeSaved.getProjectId(), projectBidToBeSaved.getBuyerId(),
                projectBidToBeSaved.getBidAmount(), projectBidToBeSaved.getWageType());
        savedProjectBid.assignId("SAVED_PROJECT_BID_001");

        when(projectBidRepository.create(projectBidToBeSaved)).thenReturn(savedProjectBid);

        final var persistedProjectBid = projectBidService.create(projectBidToBeSaved);
        assertEquals(savedProjectBid, persistedProjectBid);
    }

    @Test
    void shouldThrowEntityExpiredExceptionAndNotCreateProjectBidIfProjectIsExpired(@Random ProjectBid projectBidToBeSaved, @Random Project project) {

        project.assignId("PROJECT_001");
        project.setStatus(Project.Status.EXPIRED);

        doReturn(Optional.of(project)).when(projectService).findById(anyString());

        assertThrows(EntityExpiredException.class, () -> projectBidService.create(projectBidToBeSaved));

        verify(projectBidRepository, never()).findOneByProjectIdAndBuyerId(projectBidToBeSaved.getProjectId(), projectBidToBeSaved.getBuyerId());

        verify(projectBidRepository, never()).create(projectBidToBeSaved);
    }

    @Test
    void shouldThrowDuplicateEntityExceptionAndNotCreateProjectBidIfBuyerHasAlreadyPlacedABidForProject(@Random ProjectBid projectBidToBeSaved, @Random Project project) {

        project.assignId("PROJECT_001");
        project.setStatus(Project.Status.ACTIVE);

        doReturn(Optional.of(project)).when(projectService).findById(anyString());

        when(projectBidRepository.findOneByProjectIdAndBuyerId(projectBidToBeSaved.getProjectId(),projectBidToBeSaved.getBuyerId()))
                .thenReturn(Optional.of(projectBidToBeSaved));

        assertThrows(DuplicateEntityException.class, () -> projectBidService.create(projectBidToBeSaved));

        verify(projectBidRepository, never()).create(projectBidToBeSaved);
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

    @Test
    void shouldThrowEntityNotFoundExceptionIfProjectDoesNotExistWhenGetLowestBidForProject(@Random String projectId) {

        doReturn(Optional.empty()).when(projectService).findById(anyString());
        assertThrows(EntityNotFoundException.class, () -> projectBidService.getLowestBidForProject(anyString()));

        verify(projectBidRepository, never()).findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(anyString(), any());
    }

    @Test
    void shouldGetLowestBidFromFixedWageTypeWhenFixedWageBidSmallerThanHourlyBid(@Random String projectId) throws IllegalAccessException {

        final var project = new Project("Project Everest", ProjectType.SOFTWARE,
                "Project everest is a great project", 40);
        project.assignId(projectId);
        project.setStatus(Project.Status.ACTIVE);

        doReturn(Optional.of(project)).when(projectService).findById(anyString());
        when(projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectId, ProjectBid.WageType.FIXED))
                .thenReturn(Collections.emptyList());

        when(projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectId, ProjectBid.WageType.HOURLY))
                .thenReturn(Collections.emptyList());

        final var lowestProjectBid = projectBidService.getLowestBidForProject(projectId);

        // Even if 20 < 500, but the lowest bid should be of fixed wage type because
        // 500 is the fixed cost to cover 40 hours of project work but if we consider
        // per hour wage, then it would take perHourBidAmount * projectDurationHours
        // i.e turns out to be 800
        assertTrue(lowestProjectBid.isEmpty());
    }

    @Test
    void shouldGetLowestBidFromFixedWageTypeWhenFixedWageBidSmallerThanHourlyBid(@Random String projectId,
                                                                                 @Random ProjectBid lowestProjectBidOfFixedWageType,
                                                                                 @Random ProjectBid lowestProjectBidOfHourlyWageType) throws IllegalAccessException {

        final var project = new Project("Project Everest", ProjectType.SOFTWARE,
                "Project everest is a great project", 40);
        project.assignId(projectId);
        project.setStatus(Project.Status.ACTIVE);

        FieldUtils.getField(ProjectBid.class, "wageType", true).set(lowestProjectBidOfFixedWageType, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(lowestProjectBidOfHourlyWageType, ProjectBid.WageType.HOURLY);

        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(lowestProjectBidOfFixedWageType, 500);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(lowestProjectBidOfHourlyWageType, 20);

        doReturn(Optional.of(project)).when(projectService).findById(anyString());
        when(projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectId, ProjectBid.WageType.FIXED))
                .thenReturn(List.of(lowestProjectBidOfFixedWageType));

        when(projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectId, ProjectBid.WageType.HOURLY))
                .thenReturn(List.of(lowestProjectBidOfHourlyWageType));

        final var lowestProjectBid = projectBidService.getLowestBidForProject(projectId);

        // Even if 20 < 500, but the lowest bid should be of fixed wage type because
        // 500 is the fixed cost to cover 40 hours of project work but if we consider
        // per hour wage, then it would take perHourBidAmount * projectDurationHours
        // i.e turns out to be 800
        assertTrue(lowestProjectBid.isPresent());
        assertEquals(lowestProjectBid.get(), lowestProjectBidOfFixedWageType);
    }

    @Test
    void shouldGetLowestBidFromHourlyWageTypeWhenHourlyWageBidSmallerThanFixedBid(@Random String projectId,
                                                                                 @Random ProjectBid lowestProjectBidOfFixedWageType,
                                                                                 @Random ProjectBid lowestProjectBidOfHourlyWageType) throws IllegalAccessException {

        final var project = new Project("Project Everest", ProjectType.SOFTWARE,
                "Project everest is a great project", 40);
        project.assignId(projectId);
        project.setStatus(Project.Status.ACTIVE);

        FieldUtils.getField(ProjectBid.class, "wageType", true).set(lowestProjectBidOfFixedWageType, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(lowestProjectBidOfHourlyWageType, ProjectBid.WageType.HOURLY);

        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(lowestProjectBidOfFixedWageType, 500);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(lowestProjectBidOfHourlyWageType, 10);

        doReturn(Optional.of(project)).when(projectService).findById(anyString());
        when(projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectId, ProjectBid.WageType.FIXED))
                .thenReturn(List.of(lowestProjectBidOfFixedWageType));

        when(projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectId, ProjectBid.WageType.HOURLY))
                .thenReturn(List.of(lowestProjectBidOfHourlyWageType));

        final var lowestProjectBid = projectBidService.getLowestBidForProject(projectId);

        // The lowest bid should be of hourly wage type because
        // 500 is the fixed cost to cover 40 hours of project work & if we consider
        // per hour wage, then it would take perHourBidAmount * projectDurationHours
        // i.e turns out to be 400
        assertTrue(lowestProjectBid.isPresent());
        assertEquals(lowestProjectBid.get(), lowestProjectBidOfHourlyWageType);
    }
}
