package com.shr.marketplace.models.requirements.software;

import com.shr.marketplace.models.requirements.SoftwareRequirement;

import java.util.Set;

/**
 * @author shruti.mishra
 */
public class TechnologyStackRequirement extends SoftwareRequirement {

    private Set<String> technologies;
    public TechnologyStackRequirement(Set<String> technologies) {
        this.technologies = technologies;
    }
}
