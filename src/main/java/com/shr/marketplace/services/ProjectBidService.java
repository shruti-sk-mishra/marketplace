package com.shr.marketplace.services;

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

    private final ProjectBidRepository projectBidRepository;

    public ProjectBidService(ProjectBidRepository projectBidRepository) {
        this.projectBidRepository = projectBidRepository;
    }

    public ProjectBid create(ProjectBid projectBid) {
        return projectBidRepository.create(projectBid);
    }


    public Optional<ProjectBid> get(String id) {
        return projectBidRepository.findById(id);
    }

    public List<ProjectBid> getByProjectId(String projectId) {
        return projectBidRepository.findByProjectId(projectId);
    }
}
