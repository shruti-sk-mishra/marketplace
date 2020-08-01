package com.shr.marketplace.controllers;

import com.shr.marketplace.models.Project;
import com.shr.marketplace.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contains the endpoints for
 * multiple projects
 *
 * @author shruti.mishra
 */

@RestController("projects")
public class ProjectsController {
    private final ProjectService projectService;

    public ProjectsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping()
    public ResponseEntity<List<Project>> getActiveProjects(@RequestParam("pageStart") int pageStart,
                                                           @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.ok(projectService.findActiveProjects(pageStart, pageSize));
    }
}
