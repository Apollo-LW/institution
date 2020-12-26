package com.apollo.institution.model;

import lombok.Data;

import java.util.HashSet;

@Data
public class InstitutionUser {

    private String userId;
    private HashSet<Institution> userInstitutions = new HashSet<>();

    public InstitutionUser addInstitution(Institution institution) {
        this.userInstitutions.add(institution);
        return this;
    }

}
