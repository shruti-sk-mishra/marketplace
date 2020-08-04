package com.shr.marketplace.services;

import com.shr.marketplace.models.Project;
import com.shr.marketplace.models.ProjectType;
import com.shr.marketplace.repositories.ProjectRepository;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * @author shruti.mishra
 */
@ExtendWith(RandomBeansExtension.class)
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void shouldCreateProject() {

        final var projectToSave = new Project("Sample Project", ProjectType.AVIATION, "Project for testing");
        final var savedProject = new Project("Sample Project", ProjectType.AVIATION, "Project for testing");
        savedProject.assignId("1");

        when(projectRepository.create(projectToSave)).thenReturn(savedProject);
        final var retrievedProject = projectService.create(projectToSave);
        assertEquals(retrievedProject, savedProject);
    }

    @Test
    void shouldGetProjectById(@Random String projectId, @Random Project project) {
        project.assignId(projectId);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        final var retrievedProject = projectService.findById(projectId).orElseThrow();
        assertEquals(retrievedProject, project);
    }

    @Test
    void shouldReturnEmptyProjectWhenIdNotFound(@Random String projectId) {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        final var retrievedProject = projectService.findById(projectId);
        assertFalse(retrievedProject.isPresent());
    }

    @Test
    void shouldGetPageableListOfActiveProjects() throws IllegalAccessException {

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
        final var pageRequest = PageRequest.of(pageStart, pageSize, Sort.by(Project.Fields.expiresAt).descending());
        when(projectRepository.findByStatus(Project.Status.ACTIVE, pageRequest)).thenReturn(activeProjects);

        List<Project> retrievedActiveProjects = projectService.findByStatus(Project.Status.ACTIVE, pageStart, pageSize);

        assertEquals(retrievedActiveProjects, activeProjects);
    }

    @Test
    void shouldGetAllProjects() throws IllegalAccessException, ParseException {

        final var dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        final var startDate = dateFormat.parse("10-07-2020 00:00:00");
        final var endDate = dateFormat.parse("11-07-2020 23:59:59");
        final var projects = createProjectsListCreatedDuringAnInterval(startDate, endDate, 10);

        int pageStart = 0;
        int pageSize = 10;
        final var pageRequest = PageRequest.of(pageStart, pageSize, Sort.by(Project.Fields.expiresAt).descending());

        Page<Project> page = new PageImpl<>(projects, pageRequest, pageSize);
        when(projectRepository.findAll(pageRequest)).thenReturn(page);

        List<Project> retrievedProjects = projectService.findAllProjects(pageStart, pageSize);
        assertEquals(retrievedProjects, projects);

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
