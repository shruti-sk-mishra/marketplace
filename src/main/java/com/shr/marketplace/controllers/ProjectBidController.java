package com.shr.marketplace.controllers;

import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.services.ProjectBidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Contains the endpoints for
 * ProjectBid entity
 *
 * @author shruti.mishra
 */
@RestController("project-bid")
public class ProjectBidController {

    private final ProjectBidService projectBidService;

    public ProjectBidController(ProjectBidService projectBidService) {
        this.projectBidService = projectBidService;
    }

    @PostMapping
    public ResponseEntity<ProjectBid> create(@Valid @RequestBody ProjectBid projectBid) {

        final var createdProjectBid = projectBidService.create(projectBid);
        return new ResponseEntity<>(createdProjectBid, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectBid> get(@PathVariable("id") String id) {

        final var projectBid = projectBidService.get(id);
        return projectBid.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
