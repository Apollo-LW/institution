package com.apollo.institution.model;

import lombok.Data;

import java.util.Set;

@Data
public class ModifyInstitution {

    private String adminId, institutionId;
    private Set<String> userIds;

}
