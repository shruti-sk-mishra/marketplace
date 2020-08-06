package com.shr.marketplace.controllers;

import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.services.ProjectBidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
@RequestMapping("/project-bid")
public class ProjectBidController {

    private final ProjectBidService projectBidService;

    private final Logger logger = LoggerFactory.getLogger(ProjectBidController.class);

    public ProjectBidController(ProjectBidService projectBidService) {
        this.projectBidService = projectBidService;
    }

    @PostMapping
    public ResponseEntity<ProjectBid> create(@Valid @RequestBody ProjectBid projectBid) {

        logger.info("Attempting to place the bid={}", projectBid);
        final var createdProjectBid = projectBidService.create(projectBid);
        logger.info("Project bid created={}", createdProjectBid);
        return new ResponseEntity<>(createdProjectBid, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectBid> get(@PathVariable("id") String id) {

        final var projectBid = projectBidService.get(id);
        return projectBid.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
