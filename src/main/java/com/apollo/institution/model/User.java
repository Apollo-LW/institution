package com.apollo.institution.model;

import lombok.Data;

import java.util.HashSet;

@Data
public class User {

    private String userId;
    private HashSet<String> userInstitutions = new HashSet<>();

    public void addInstitution(String institutionId) {
        this.userInstitutions.add(institutionId);
    }

}
