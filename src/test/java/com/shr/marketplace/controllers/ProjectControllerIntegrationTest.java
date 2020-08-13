package com.shr.marketplace.controllers;

import com.shr.marketplace.config.BaseMVCTest;
import com.shr.marketplace.models.Project;
import com.shr.marketplace.services.ProjectService;
import io.github.glytching.junit.extension.random.Random;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author shruti.mishra
 */
@WebMvcTest(controllers = ProjectController.class)
public class ProjectControllerIntegrationTest extends BaseMVCTest {

    @MockBean
    private ProjectService projectService;

    @Test
    void shouldGetProjectById(@Random String projectId, @Random Project project) throws Exception {
        when(projectService.findById(projectId)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/project/" + projectId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(project)));
    }

    @Test
    void shouldReturn404WhenProjectWithIdDoesNotExist(@Random String projectId) throws Exception {
        when(projectService.findById(projectId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/project/" + projectId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateProject(@Random Project projectToCreated, @Random Project projectCreated) throws Exception{
        when(projectService.create(projectToCreated)).thenReturn(projectCreated);

        final var createRequestJson = objectMapper.valueToTree(projectCreated).toString();

        mockMvc.perform(post("/project")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateProjectById(@Random String projectId, @Random Project project) throws Exception {
        when(projectService.update(project)).thenReturn(project);

        final var updateRequestJson = objectMapper.valueToTree(project).toString();

        mockMvc.perform(put("/project/" + projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestJson))
                .andExpect(status().isNoContent())
                .andExpect(content().string(EMPTY));
    }

    @Test
    void shouldPatchProjectById(@Random String projectId, @Random Project project) throws Exception {
        when(projectService.merge(project)).thenReturn(project);

        mockMvc.perform(patch("/project/" + projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(project)))
                .andExpect(status().isNoContent());
    }
}