package com.apollo.institution.model;

import lombok.Data;

import java.util.HashSet;

@Data
public class InstitutionUser {

    private String userId;
    private HashSet<String> userInstitutions = new HashSet<>();

    public InstitutionUser addInstitution(String institutionId) {
        this.userInstitutions.add(institutionId);
        return this;
    }

}
