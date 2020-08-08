package com.shr.marketplace.services;

import com.shr.marketplace.models.Project;
import com.shr.marketplace.repositories.ProjectRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for
 * Project entity
 *
 * @author shruti.mishra
 */
@Service
public class ProjectService {


    private final ProjectRepository projectRepository;
    private final CacheService cacheService;

    public ProjectService(ProjectRepository projectRepository, CacheService cacheService) {
        this.projectRepository = projectRepository;
        this.cacheService = cacheService;
    }

    /**
     * Creates a project
     *
     * @param project
     * @return
     * @throws Exception
     */
    public Project create(Project project) throws Exception {
        final var createdProject =  projectRepository.create(project);
        if(createdProject != null) {
            // set a projectId as key in redis with the
            // expiry date of project so that the key should expire implicitly
            this.cacheService.setValue(createdProject.getId(), createdProject.getId(),
                    createdProject.getExpiresAt());
            return createdProject;
        }
        throw new Exception("Error occurred during the creating project.");
    }

    public Project update(Project project) {
        return projectRepository.update(project);
    }

    public Project merge(Project project) throws NoSuchFieldException, IllegalAccessException {
        return projectRepository.merge(project);
    }

    public Optional<Project> findById(String id) { return projectRepository.findById(id); }

    public List<Project> findAllProjects(int pageStart, int pageSize) {
        final var pageRequest = PageRequest.of(pageStart, pageSize, Sort.by(Project.Fields.expiresAt).descending());
        return projectRepository.findAll(pageRequest).getContent();
    }

    public List<Project> findByStatus(Project.Status status, int pageStart, int pageSize) {
        final var pageRequest = PageRequest.of(pageStart, pageSize, Sort.by(Project.Fields.expiresAt).descending());
        return projectRepository.findByStatus(status, pageRequest);
    }
}
