package com.shr.marketplace.listeners.redis;

import com.shr.marketplace.models.Project;
import com.shr.marketplace.services.ProjectBidService;
import com.shr.marketplace.services.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class ExpirationListener implements MessageListener {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectBidService projectBidService;

    private final Logger logger = LoggerFactory.getLogger(ExpirationListener.class);

    @Override
    public void onMessage(Message message, byte[] paramArrayOfByte) {
        final var projectId = new String(message.getBody());
        logger.info("Expired key= [{}]. Now setting the project status as 'EXPIRED", message);
        final var project = new Project();
        project.assignId(projectId);
        project.setStatus(Project.Status.EXPIRED);
        try {
            final var expiredProject = projectService.merge(project);
            if(expiredProject != null) {
                logger.info("Project [{}] expired! Now getting the lowest bid and associating it with the project", projectId);
                final var lowestProjectBid = projectBidService.getLowestBidForProject(projectId);
                if(lowestProjectBid.isEmpty()) {
                    logger.info("There is no bid placed for the project [{}].", projectId);
                } else {
                    // set it as the 'selected bid' into the project object
                    expiredProject.setSelectedBidId(lowestProjectBid.get().getId());
                    final var expiredProjectWithSelectedBid = projectService.update(expiredProject);
                    if(expiredProjectWithSelectedBid != null) {
                        logger.info("Selected lowest bid for project[{}]=[{}]", projectId, lowestProjectBid);
                    } else {
                        logger.error("Something went wrong while selecting the projectBid=[{}] for the project [{}]", lowestProjectBid, projectId);
                    }
                }
            } else {
                logger.error("Something went wrong while expiring the project [{}]", projectId);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}