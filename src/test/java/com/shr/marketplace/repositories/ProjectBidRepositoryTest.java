package com.shr.marketplace.repositories;

import com.shr.marketplace.config.mongo.BaseRepositoryTest;
import com.shr.marketplace.models.ProjectBid;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        projectBidRepository.saveAll(Set.of(firstProjectBidForProjectEverest, secondProjectBidForProjectEverest,
                thirdProjectBidForProjectEverest, projectBidForADifferentProject));

        List<ProjectBid> projectBidsForProjectEverest = projectBidRepository.findByProjectId(projectIdOfProjectEverest);

        assertEquals(projectBidsForProjectEverest.size(), 3);
    }
}
