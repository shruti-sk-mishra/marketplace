package com.shr.marketplace.controllers;

import com.shr.marketplace.models.Project;
import com.shr.marketplace.services.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * Contains the endpoints for
 * multiple projects
 *
 * @author shruti.mishra
 */
@RestController
@RequestMapping("/projects")
public class ProjectsController {
    private final ProjectService projectService;

    public ProjectsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping()
    public ResponseEntity<Page<Project>> getActiveProjects(@RequestParam(name = "status", required = false) Project.Status status,
                                                           Pageable pageable) {
        if(status == null) {
            final var allProjects = projectService.findAllProjects(pageable);
            return ResponseEntity.ok(new PageImpl(allProjects, pageable, allProjects.size()));
        }
        final var activeProjects = projectService.findByStatus(status, pageable);
        return ResponseEntity.ok(new PageImpl(activeProjects, pageable, activeProjects.size()));
    }
}
