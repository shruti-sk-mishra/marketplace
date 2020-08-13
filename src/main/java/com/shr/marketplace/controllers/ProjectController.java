package com.shr.marketplace.controllers;

import com.shr.marketplace.models.Project;
import com.shr.marketplace.services.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author shruti.mishra
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    public ProjectController(ProjectService projectService) {
            this.projectService = projectService;
        }


    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable("id") String projectId) {
        final var project = projectService.findById(projectId);
        return project.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping
    public ResponseEntity<Project> create(@Valid @RequestBody Project project) throws Exception {

        logger.info("Attempting to create the project={}", project);
        final var createdProject = projectService.create(project);
        logger.info("Project created={}", createdProject);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Project project) {

        logger.info("Updating the project={}", project);
        final var updatedProject = projectService.update(project);
        logger.info("Project updated={}", updatedProject);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping
    public ResponseEntity<Void> merge(@RequestBody Project project)throws NoSuchFieldException,IllegalAccessException {
        logger.info("Merging the project={}", project);
        final var updatedProject = projectService.merge(project);
        logger.info("Project updated={}", updatedProject);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
