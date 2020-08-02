package com.shr.marketplace.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shr.marketplace.config.mongo.BaseTest;
import com.shr.marketplace.models.Project;
import com.shr.marketplace.models.ProjectType;
import com.shr.marketplace.services.ProjectService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author shruti.mishra
 */
@WebMvcTest(controllers = ProjectsController.class)
public class ProjectsControllerIntegrationTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetActiveApplications() throws Exception {

        final var futureStartCalendar = Calendar.getInstance();
        futureStartCalendar.setTime(new Date());
        futureStartCalendar.add(Calendar.DATE, 10);
        final var futureStartDate = futureStartCalendar.getTime();

        final var futureEndCalendar = Calendar.getInstance();
        futureEndCalendar.setTime(new Date());
        futureEndCalendar.add(Calendar.DATE, 11);
        final var futureEndDate = futureEndCalendar.getTime();

        final var activeProjects = createProjectsListCreatedDuringAnInterval(futureStartDate, futureEndDate, 10);

        int pageStart = 0;
        int pageSize = 10;
        when(projectService.findActiveProjects(pageStart, pageSize)).thenReturn(activeProjects);

        mockMvc.perform(get("/projects?status=ACTIVE&pageStart=" + pageStart + "&pageSize=" + pageSize ))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(activeProjects)));
    }

    private List<Project> createProjectsListCreatedDuringAnInterval(Date startDate, Date endDate, int size) throws IllegalAccessException {
        final var projects = new ArrayList<Project>();

        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        for(int i = 1; i <= size; i++) {
            long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
            Project project = new Project("Project_" + i, ProjectType.SOFTWARE,"This is project " + i);
            FieldUtils.getField(Project.class, "expiresAt", true)
                    .set(project, new Date(randomMillisSinceEpoch));
            projects.add(project);
        }
        return projects;
    }
}
