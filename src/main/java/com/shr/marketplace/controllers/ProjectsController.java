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
    public ResponseEntity<List<Project>> getActiveProjects(@RequestParam(name = "status", required = false) Project.Status status,
                                                           @RequestParam(name = "pageStart", required = false, defaultValue = "0") int pageStart,
                                                           @RequestParam(name = "pageSize", required = false, defaultValue = "100") int pageSize) {


        if(status != null && status.equals(Project.Status.ACTIVE)) {
            return ResponseEntity.ok(projectService.findActiveProjects(pageStart, pageSize));
        }
        return ResponseEntity.ok(projectService.findAllProjects(pageStart, pageSize));
    }
}
