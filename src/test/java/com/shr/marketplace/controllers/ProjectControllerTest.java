package com.shr.marketplace.controllers;

import com.shr.marketplace.config.mongo.BaseTest;
import com.shr.marketplace.models.Project;
import com.shr.marketplace.models.ProjectType;
import com.shr.marketplace.services.ProjectService;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for
 * ProjectsController
 *
 * @author shruti.mishra
 */
@ExtendWith(RandomBeansExtension.class)
@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest extends BaseTest {

    @InjectMocks
    private ProjectController projectController;

    @Mock
    private ProjectService projectService;

    @Test
    void shouldCreateProject(@Random String projectId, @Random Project projectToCreate,
                             @Random Project projectCreated) throws Exception {

        projectCreated.assignId(projectId);

        when(projectService.create(projectToCreate)).thenReturn(projectCreated);
        final var response = projectController.create(projectToCreate);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), is(projectCreated));
    }

    @Test
    void shouldUpdateProject(@Random String projectId, @Random Project projectToUpdate,
                             @Random Project projectUpdated) throws Exception {

        projectToUpdate.setStatus(Project.Status.EXPIRED);
        projectToUpdate.setSelectedBidId("1");

        projectUpdated.assignId(projectId);
        when(projectService.update(projectToUpdate)).thenReturn(projectUpdated);
        final var response = projectController.update(projectToUpdate);

        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
        assertNull(response.getBody());
    }

    @Test
    void shouldPatchProject(@Random String projectId, @Random Project projectToUpdate) throws Exception {

        projectToUpdate.setStatus(Project.Status.EXPIRED);
        final var projectUpdated = new Project("Project Everest", "sellerId", ProjectType.SOFTWARE, "Project everest is a great project",
                40);
        projectUpdated.assignId(projectId);

        when(projectService.merge(projectToUpdate)).thenReturn(projectUpdated);
        final var response = projectController.merge(projectToUpdate);

        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
        assertNull(response.getBody());
    }
}
