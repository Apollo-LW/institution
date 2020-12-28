package com.apollo.institution.model;

import lombok.Data;

import java.util.HashSet;

@Data
public class InstitutionJoin {

    private String institutionId;
    private HashSet<InstitutionJoinRequest> institutionJoinRequests = new HashSet<>();

    public InstitutionJoin addRequest(InstitutionJoinRequest institutionJoinRequest) {
        this.institutionJoinRequests.add(institutionJoinRequest);
        return this;
    }

}
