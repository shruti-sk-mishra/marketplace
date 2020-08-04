package com.shr.marketplace.services;

import com.shr.marketplace.controllers.ProjectBidController;
import com.shr.marketplace.exceptions.http.DuplicateEntityException;
import com.shr.marketplace.exceptions.http.EntityExpiredException;
import com.shr.marketplace.exceptions.http.EntityNotFoundException;
import com.shr.marketplace.models.Project;
import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.repositories.ProjectBidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

/**
 * Service class for ProjectBid entity
 *
 * @author shruti.mishra
 */
@Service
public class ProjectBidService {

    private final ProjectService projectService;
    private final ProjectBidRepository projectBidRepository;
    private final Logger logger = LoggerFactory.getLogger(ProjectBidController.class);

    public ProjectBidService(ProjectService projectService, ProjectBidRepository projectBidRepository) {
        this.projectService = projectService;
        this.projectBidRepository = projectBidRepository;
    }


    /**
     * Creates the project bids
     *
     * @param projectBid
     * @return
     */
    public ProjectBid create(ProjectBid projectBid) {

        // Buyer should be able to place the bid only for the active projects
        final var project = projectService.findById(projectBid.getProjectId()).orElseThrow(() ->
                new EntityNotFoundException(format("The project with ID [%s] does not exist", projectBid.getProjectId())));

        if(!Project.Status.ACTIVE.equals(project.getStatus())) {
            logger.error("The project [{}] is not active and hence not available for bidding", project.getName());
            throw new EntityExpiredException(String.format("The project [%s] is not active and hence not available for bidding",
                    project.getName()));
        }
        // check if buyer hasn't placed the bid for the particular project
        final var retrievedProjectBid = projectBidRepository.findOneByProjectIdAndBuyerId(projectBid.getProjectId(),projectBid.getBuyerId());

        if(!retrievedProjectBid.isEmpty()) {
            logger.error("The buyer with ID [{}] has already placed a bid for the project [{}]",
                    projectBid.getBuyerId(), projectBid.getProjectId());
            throw new DuplicateEntityException(String.format("The buyer with ID [%s] has already placed a bid for the project [%s]",
                    projectBid.getBuyerId(), projectBid.getProjectId()), null);
        }
        return projectBidRepository.create(projectBid);
    }


    public Optional<ProjectBid> get(String id) {
        return projectBidRepository.findById(id);
    }

    public List<ProjectBid> getByProjectId(String projectId) {
        return projectBidRepository.findByProjectId(projectId);
    }
}
