package com.shr.marketplace.repositories;

import com.shr.marketplace.models.Project;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author shruti.mishra
 */
public interface ProjectCustomRepository {
    List<Project> findActiveProjects(Pageable pageable);
    List<Project> findActiveProjectsByExpiryDate(Date startDate, Date endDate, Pageable pageable);
}
