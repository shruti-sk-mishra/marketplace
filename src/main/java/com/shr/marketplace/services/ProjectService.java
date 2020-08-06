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

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project create(Project project) { return projectRepository.create(project); }

    public Project update(Project project) {
        return projectRepository.update(project);
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
