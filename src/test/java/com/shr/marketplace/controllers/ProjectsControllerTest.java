package com.shr.marketplace.controllers;

import com.shr.marketplace.config.mongo.BaseTest;
import com.shr.marketplace.models.Project;
import com.shr.marketplace.models.ProjectType;
import com.shr.marketplace.services.ProjectService;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Unit test cases for
 * ProjectsController
 *
 * @author shruti.mishra
 */
@ExtendWith(RandomBeansExtension.class)
@ExtendWith(MockitoExtension.class)
public class ProjectsControllerTest extends BaseTest {

    @InjectMocks
    private ProjectsController projectsController;

    @Mock
    private ProjectService projectService;

    @Test
    void shouldGetActiveProjects() throws IllegalAccessException {

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
        final var response = projectsController.getActiveProjects(Project.Status.ACTIVE, pageRequest);

        final Page<Project> page = new PageImpl<>(activeProjects, pageRequest, activeProjects.size());

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(page));
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
