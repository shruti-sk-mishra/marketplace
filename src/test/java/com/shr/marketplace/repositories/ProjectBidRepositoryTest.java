package com.shr.marketplace.repositories;

import com.shr.marketplace.config.mongo.BaseRepositoryTest;
import com.shr.marketplace.models.ProjectBid;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test cases for ProjectBidRepository
 *
 * @author shruti.mishra
 */
@SpringBootTest
@ExtendWith(RandomBeansExtension.class)
public class ProjectBidRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ProjectBidRepository projectBidRepository;

    @Test
    void shouldCreateAProjectBid(@Random ProjectBid projectBid) {
        final var savedProjectBid = projectBidRepository.create(projectBid);
        assertNotNull(savedProjectBid.getId());
    }

    @Test
    void shouldGetProjectBidById(@Random ProjectBid projectBid) {
        final var savedProjectBid = projectBidRepository.create(projectBid);

        final var retrievedBid = projectBidRepository.findById(savedProjectBid.getId()).orElseThrow();
        assertNotNull(retrievedBid);
    }

    @Test
    void shouldNotGetProjectBidWhenProjectBidIdNotFound(@Random String projectBidId) {

        final var retrievedBid = projectBidRepository.findById(projectBidId);
        assertFalse(retrievedBid.isPresent());
    }

    @Test
    void shouldGetProjectBidsByProjectId(@Random String projectIdOfProjectEverest, @Random ProjectBid firstProjectBidForProjectEverest,
                                         @Random ProjectBid secondProjectBidForProjectEverest, @Random ProjectBid thirdProjectBidForProjectEverest,
                                         @Random ProjectBid projectBidForADifferentProject) throws IllegalAccessException {

        FieldUtils.getField(ProjectBid.class, "projectId", true).set(firstProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(secondProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(thirdProjectBidForProjectEverest, projectIdOfProjectEverest);

        projectBidRepository.create(firstProjectBidForProjectEverest);
        projectBidRepository.create(secondProjectBidForProjectEverest);
        projectBidRepository.create(thirdProjectBidForProjectEverest);
        projectBidRepository.create(projectBidForADifferentProject);

        List<ProjectBid> projectBidsForProjectEverest = projectBidRepository.findByProjectId(projectIdOfProjectEverest);

        assertEquals(projectBidsForProjectEverest.size(), 3);
    }

    @Test
    void shouldGetProjectBidsForDifferentWageTypesOrderedByLowestBidAmountsForAProject(@Random String projectIdOfProjectEverest, @Random ProjectBid firstProjectBidForProjectEverest,
                                                              @Random ProjectBid secondProjectBidForProjectEverest, @Random ProjectBid thirdProjectBidForProjectEverest,
                                                              @Random ProjectBid fourthProjectBidForProjectEverest, @Random ProjectBid fifthProjectBidForProjectEverest,
                                                              @Random ProjectBid projectBidForADifferentProject) throws IllegalAccessException {

        FieldUtils.getField(ProjectBid.class, "projectId", true).set(firstProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(secondProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(thirdProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(fourthProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(fifthProjectBidForProjectEverest, projectIdOfProjectEverest);

        FieldUtils.getField(ProjectBid.class, "wageType", true).set(firstProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(secondProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(thirdProjectBidForProjectEverest, ProjectBid.WageType.HOURLY);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(fourthProjectBidForProjectEverest, ProjectBid.WageType.HOURLY);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(fifthProjectBidForProjectEverest, ProjectBid.WageType.HOURLY);

        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(firstProjectBidForProjectEverest, 20000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(secondProjectBidForProjectEverest, 25000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(thirdProjectBidForProjectEverest, 15000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(fourthProjectBidForProjectEverest, 17000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(fifthProjectBidForProjectEverest, 12000.00);

        projectBidRepository.create(firstProjectBidForProjectEverest);
        projectBidRepository.create(secondProjectBidForProjectEverest);
        projectBidRepository.create(thirdProjectBidForProjectEverest);
        projectBidRepository.create(fourthProjectBidForProjectEverest);
        projectBidRepository.create(fifthProjectBidForProjectEverest);
        projectBidRepository.create(projectBidForADifferentProject);

        final var lowestBidOfFixedWageType = projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectIdOfProjectEverest, ProjectBid.WageType.FIXED);

        assertEquals(lowestBidOfFixedWageType.size(), 2);
        assertEquals(lowestBidOfFixedWageType.get(0).getBidAmount(), 20000.00);
        assertEquals(lowestBidOfFixedWageType.get(1).getBidAmount(), 25000.00);

        final var lowestBidOfHourlyWageType = projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectIdOfProjectEverest, ProjectBid.WageType.HOURLY);

        assertEquals(lowestBidOfHourlyWageType.size(), 3);
        assertEquals(lowestBidOfHourlyWageType.get(0).getBidAmount(), 12000.00);
        assertEquals(lowestBidOfHourlyWageType.get(1).getBidAmount(), 15000.00);
        assertEquals(lowestBidOfHourlyWageType.get(2).getBidAmount(), 17000.00);
    }

    @Test
    void shouldGetProjectBidsOrderedByLowestBidAmountsAndCreatedAtForAProject(@Random String projectIdOfProjectEverest, @Random ProjectBid firstProjectBidForProjectEverest,
                                                                   @Random ProjectBid secondProjectBidForProjectEverest, @Random ProjectBid thirdProjectBidForProjectEverest,
                                                                   @Random ProjectBid fourthProjectBidForProjectEverest, @Random ProjectBid fifthProjectBidForProjectEverest,
                                                                   @Random ProjectBid projectBidForADifferentProject) throws IllegalAccessException {

        FieldUtils.getField(ProjectBid.class, "projectId", true).set(firstProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(secondProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(thirdProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(fourthProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(fifthProjectBidForProjectEverest, projectIdOfProjectEverest);

        FieldUtils.getField(ProjectBid.class, "wageType", true).set(firstProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(secondProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(thirdProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(fourthProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(fifthProjectBidForProjectEverest, ProjectBid.WageType.FIXED);

        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(firstProjectBidForProjectEverest, 20000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(secondProjectBidForProjectEverest, 25000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(thirdProjectBidForProjectEverest, 12000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(fourthProjectBidForProjectEverest, 17000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(fifthProjectBidForProjectEverest, 12000.00);

        int addMinuteTime = 5;
        Date now = new Date(); //now
        final var fiveMinutesLater = DateUtils.addMinutes(now, addMinuteTime);
        FieldUtils.getField(ProjectBid.class, "createdAt", true).set(fifthProjectBidForProjectEverest, now);
        FieldUtils.getField(ProjectBid.class, "createdAt", true).set(thirdProjectBidForProjectEverest, fiveMinutesLater);

        projectBidRepository.create(firstProjectBidForProjectEverest);
        projectBidRepository.create(secondProjectBidForProjectEverest);
        projectBidRepository.create(thirdProjectBidForProjectEverest);
        projectBidRepository.create(fourthProjectBidForProjectEverest);
        projectBidRepository.create(fifthProjectBidForProjectEverest);
        projectBidRepository.create(projectBidForADifferentProject);

        final var lowestBidOfFixedWageType = projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectIdOfProjectEverest, ProjectBid.WageType.FIXED);

        assertEquals(lowestBidOfFixedWageType.size(), 5);
        assertEquals(lowestBidOfFixedWageType.get(0).getBidAmount(), 12000.00);
        assertEquals(lowestBidOfFixedWageType.get(1).getBidAmount(), 12000.00);
        assertEquals(lowestBidOfFixedWageType.get(2).getBidAmount(), 17000.00);
        assertEquals(lowestBidOfFixedWageType.get(3).getBidAmount(), 20000.00);
        assertEquals(lowestBidOfFixedWageType.get(4).getBidAmount(), 25000.00);

        assertEquals(lowestBidOfFixedWageType.get(0).getBuyerId(), fifthProjectBidForProjectEverest.getBuyerId());
        assertEquals(lowestBidOfFixedWageType.get(1).getBuyerId(), thirdProjectBidForProjectEverest.getBuyerId());
    }

    @Test
    void shouldGetProjectBidsOrderedRandomlyWhenBidAmountsAndCreatedAtAreSameForOneOrMoreBidForAProject(@Random String projectIdOfProjectEverest, @Random ProjectBid firstProjectBidForProjectEverest,
                                                                              @Random ProjectBid secondProjectBidForProjectEverest, @Random ProjectBid thirdProjectBidForProjectEverest,
                                                                              @Random ProjectBid fourthProjectBidForProjectEverest, @Random ProjectBid fifthProjectBidForProjectEverest,
                                                                              @Random ProjectBid projectBidForADifferentProject) throws IllegalAccessException {

        FieldUtils.getField(ProjectBid.class, "projectId", true).set(firstProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(secondProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(thirdProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(fourthProjectBidForProjectEverest, projectIdOfProjectEverest);
        FieldUtils.getField(ProjectBid.class, "projectId", true).set(fifthProjectBidForProjectEverest, projectIdOfProjectEverest);

        FieldUtils.getField(ProjectBid.class, "wageType", true).set(firstProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(secondProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(thirdProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(fourthProjectBidForProjectEverest, ProjectBid.WageType.FIXED);
        FieldUtils.getField(ProjectBid.class, "wageType", true).set(fifthProjectBidForProjectEverest, ProjectBid.WageType.FIXED);

        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(firstProjectBidForProjectEverest, 20000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(secondProjectBidForProjectEverest, 25000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(thirdProjectBidForProjectEverest, 12000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(fourthProjectBidForProjectEverest, 17000.00);
        FieldUtils.getField(ProjectBid.class, "bidAmount", true).set(fifthProjectBidForProjectEverest, 12000.00);

        Date now = new Date();
        FieldUtils.getField(ProjectBid.class, "createdAt", true).set(fifthProjectBidForProjectEverest, now);
        FieldUtils.getField(ProjectBid.class, "createdAt", true).set(thirdProjectBidForProjectEverest, now);

        projectBidRepository.create(firstProjectBidForProjectEverest);
        projectBidRepository.create(secondProjectBidForProjectEverest);
        projectBidRepository.create(thirdProjectBidForProjectEverest);
        projectBidRepository.create(fourthProjectBidForProjectEverest);
        projectBidRepository.create(fifthProjectBidForProjectEverest);
        projectBidRepository.create(projectBidForADifferentProject);

        final var lowestBidOfFixedWageType = projectBidRepository
                .findByProjectIdAndWageTypeOrderByBidAmountAscCreatedAtAsc(projectIdOfProjectEverest, ProjectBid.WageType.FIXED);

        assertEquals(lowestBidOfFixedWageType.size(), 5);
        assertEquals(lowestBidOfFixedWageType.get(0).getBidAmount(), 12000.00);
        assertEquals(lowestBidOfFixedWageType.get(1).getBidAmount(), 12000.00);
        assertEquals(lowestBidOfFixedWageType.get(2).getBidAmount(), 17000.00);
        assertEquals(lowestBidOfFixedWageType.get(3).getBidAmount(), 20000.00);
        assertEquals(lowestBidOfFixedWageType.get(4).getBidAmount(), 25000.00);

        final var possibleLowestBidders = Set.of(thirdProjectBidForProjectEverest.getBuyerId(),
                fifthProjectBidForProjectEverest.getBuyerId());

        assertTrue(possibleLowestBidders.contains(lowestBidOfFixedWageType.get(0).getBuyerId()));
        assertTrue(possibleLowestBidders.contains(lowestBidOfFixedWageType.get(1).getBuyerId()));
        assertFalse(possibleLowestBidders.contains(lowestBidOfFixedWageType.get(2).getBuyerId()));
        assertFalse(possibleLowestBidders.contains(lowestBidOfFixedWageType.get(3).getBuyerId()));
        assertFalse(possibleLowestBidders.contains(lowestBidOfFixedWageType.get(4).getBuyerId()));
    }
}
