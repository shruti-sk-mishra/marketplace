package com.shr.marketplace.services;

import com.shr.marketplace.exceptions.http.DuplicateEntityException;
import com.shr.marketplace.exceptions.http.EntityExpiredException;
import com.shr.marketplace.models.Project;
import com.shr.marketplace.models.ProjectBid;
import com.shr.marketplace.repositories.ProjectBidRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for ProjectBid entity
 *
 * @author shruti.mishra
 */
@Service
public class ProjectBidService {

    private final ProjectService projectService;
    private final ProjectBidRepository projectBidRepository;

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
        final var project = projectService.findById(projectBid.getProjectId()).orElseThrow();
        if(!Project.Status.ACTIVE.equals(project.getStatus())) {
            throw new EntityExpiredException(String.format("The project [%s] with id [%s] is expired and no longer available for bidding",
                    project.getName(), project.getId()));
        }
        // check if buyer hasn't placed the bid for the particular project
        final var retrievedProjectBid = projectBidRepository.findOneByProjectIdAndBuyerId(projectBid.getProjectId(),projectBid.getBuyerId());

        if(!retrievedProjectBid.isEmpty()) {
            throw new DuplicateEntityException(String.format("The buyer with id [%s] has already placed the bid for the project [%s]",
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
