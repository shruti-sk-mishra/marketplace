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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
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
    void shouldGetActiveProjects() throws Exception {

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
        final var pageRequest = PageRequest.of(pageStart, pageSize, Sort.by("createdAt").descending());
        when(projectService.findByStatus(Project.Status.ACTIVE, pageRequest)).thenReturn(activeProjects);

        final Page<Project> page = new PageImpl<>(activeProjects, pageRequest, activeProjects.size());

        mockMvc.perform(get("/projects?status=ACTIVE&page=0&size=10&sort=createdAt,DESC"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }
    private List<Project> createProjectsListCreatedDuringAnInterval(Date startDate, Date endDate, int size) throws IllegalAccessException {
        final var projects = new ArrayList<Project>();

        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        for(int i = 1; i <= size; i++) {
            long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
            Project project = new Project("Project_" + i, "sellerId", ProjectType.SOFTWARE,"This is project " + i, 40);
            FieldUtils.getField(Project.class, "expiresAt", true)
                    .set(project, new Date(randomMillisSinceEpoch));
            projects.add(project);
        }
        return projects;
    }
}
