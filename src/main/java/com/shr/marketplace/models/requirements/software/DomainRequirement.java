package com.shr.marketplace.models.requirements.software;

import com.shr.marketplace.models.requirements.SoftwareRequirement;

/**
 * @author shruti.mishra
 */
public class DomainRequirement extends SoftwareRequirement {

    private String domainName;
    DomainRequirement(String domainName) {
        this.domainName = domainName;
    }
}
