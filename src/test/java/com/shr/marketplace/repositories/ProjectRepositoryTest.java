package com.shr.marketplace.repositories;

import com.shr.marketplace.config.mongo.BaseRepositoryTest;
import com.shr.marketplace.models.Project;
import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.models.ProjectType;
import com.shr.marketplace.models.requirements.CommonRequirement;
import com.shr.marketplace.models.requirements.Requirement;
import com.shr.marketplace.models.requirements.software.DomainRequirement;
import com.shr.marketplace.models.requirements.software.TechnologyStackRequirement;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test cases for
 * ProjectRepository
 *
 * @author shruti.mishra
 */
@SpringBootTest
@ExtendWith(RandomBeansExtension.class)
public class ProjectRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void shouldCreateGenericProject(@Random Project project) {
       final var savedProject = projectRepository.create(project);

       assertNotNull(savedProject.getId());
       assertNotNull(projectRepository.findById(savedProject.getId()));
    }

    @Test
    void shouldCreateSoftwareProject(@Random CommonRequirement commonRequirement,
                                     @Random DomainRequirement domainRequirement) throws IllegalAccessException {

        final var project = new Project("Project Everest", ProjectType.SOFTWARE,
                "Project everest is a great project", 40);
        final var technologyStackRequirements = new TechnologyStackRequirement(Set.of("Java 8", "Spring Boot", "MongoDB"));
        FieldUtils.getField(Requirement.class, "name", true).set(technologyStackRequirements, "Software Requirement");
        FieldUtils.getField(Requirement.class, "description", true).set(technologyStackRequirements, "It's a software project");

        final var requirements = Set.of(commonRequirement, domainRequirement, technologyStackRequirements);
        FieldUtils.getField(Project.class, Project.Fields.requirements, true).set(project, requirements);
        final var savedProject = projectRepository.create(project);

        assertNotNull(savedProject.getId());
        assertNotNull(projectRepository.findById(savedProject.getId()));
    }

    @Test
    void shouldUpdateProject(@Random Project project) throws IllegalAccessException {
        final var savedProject = projectRepository.create(project);
        assertNotNull(savedProject.getId());
        final var projectNewDescription = "This is sample test project";

        FieldUtils.getField(Project.class, Project.Fields.description, true)
                .set(savedProject, projectNewDescription);

        final var updatedProject = projectRepository.update(savedProject);
        assertThat(updatedProject.getDescription(), is(projectNewDescription));
    }

    @Test
    void shouldMergeProject() throws NoSuchFieldException, IllegalAccessException {
        final var project = new Project("Project Everest", ProjectType.SOFTWARE, "Project everest is a great project", 40);
        final var savedProject = projectRepository.create(project);

        assertNotNull(savedProject.getId());
        assertEquals(savedProject.getStatus(), Project.Status.INACTIVE);

        final var retrievedProject = new Project();
        retrievedProject.assignId(savedProject.getId());
        retrievedProject.setStatus(Project.Status.EXPIRED);

        final var mergedProject = projectRepository.merge(retrievedProject);
        assertEquals(mergedProject.getStatus(), Project.Status.EXPIRED);
    }

    @Test
    void shouldGetProject(@Random Project project) {
        final var savedProject = projectRepository.create(project);
        assertNotNull(projectRepository.findById(savedProject.getId()));
    }

    /**
     * Should return only those projects which expiry date
     * is between the two dates
     *
     * @throws ParseException
     * @throws IllegalAccessException
     */
    @Test
    void shouldGetAllActiveProjectsOrderedByExpiryDate() throws ParseException, IllegalAccessException {
        final var dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        final var startDate = dateFormat.parse("10-08-2020 00:00:00");
        final var endDate = dateFormat.parse("11-08-2020 23:59:59");
        Set<Project> projects = createProjectsListCreatedDuringAnInterval(Project.Status.INACTIVE, startDate, endDate, 20);

        //add more projects which were added before 10-08-2020 00:00:00
        Set<Project> oldProjects = createProjectsListCreatedDuringAnInterval(Project.Status.ACTIVE, dateFormat.parse("08-08-2020 00:00:00"),
                dateFormat.parse("09-08-2020 23:59:59"), 5);

        projects.addAll(oldProjects);

        projectRepository.saveAll(projects);

        final var pageRequest = PageRequest.of(0, 10, Sort.by(Project.Fields.expiresAt).descending());
        List<Project> activeProjects = projectRepository.findActiveProjectsByExpiryDate(startDate, endDate, pageRequest);

        assertThat(activeProjects.size(), is(10));
    }

    @Test
    void shouldGetAllTheActiveProjects() throws ParseException, IllegalAccessException {
        final var dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        final var startDate = dateFormat.parse("10-07-2020 00:00:00");
        final var endDate = dateFormat.parse("11-07-2020 23:59:59");

        final var projects = createProjectsListCreatedDuringAnInterval(Project.Status.INACTIVE, startDate, endDate, 10);

        final var futureStartCalendar = Calendar.getInstance();
        futureStartCalendar.setTime(new Date());
        futureStartCalendar.add(Calendar.DATE, 10);
        final var futureStartDate = futureStartCalendar.getTime();

        final var futureEndCalendar = Calendar.getInstance();
        futureEndCalendar.setTime(new Date());
        futureEndCalendar.add(Calendar.DATE, 11);
        final var futureEndDate = futureEndCalendar.getTime();

        final var activeProjects = createProjectsListCreatedDuringAnInterval(Project.Status.ACTIVE, futureStartDate, futureEndDate, 10);
        projects.addAll(activeProjects);

        projectRepository.saveAll(projects);

        final var pageRequest = PageRequest.of(0, 20, Sort.by(Project.Fields.expiresAt).descending());
        List<Project> retrievedActiveProjects = projectRepository.findByStatus(Project.Status.ACTIVE, pageRequest);

        assertThat(retrievedActiveProjects.size(), is(10));
    }

    private Set<Project> createProjectsListCreatedDuringAnInterval(Project.Status status, Date startDate, Date endDate, int size) throws IllegalAccessException {
        final var projects = new LinkedHashSet<Project>();

        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        for(int i = 1; i <= size; i++) {
            long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
            Project project = new Project("Project_" + i, ProjectType.SOFTWARE, "This is project " + i, 40);
            FieldUtils.getField(Project.class, "expiresAt", true).set(project, new Date(randomMillisSinceEpoch));
            FieldUtils.getField(Project.class, "status", true).set(project, status);
            projects.add(project);
        }
        return projects;
    }
}
