package com.apollo.institution.model;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

@Data
public class Institution {

    private final String institutionId = UUID.randomUUID().toString();
    private final Date institutionDateOfCreation = Calendar.getInstance().getTime();
    private String institutionName = this.institutionId + '-' + this.institutionDateOfCreation;
    private HashSet<String> institutionParents = new HashSet<>(), institutionChildren = new HashSet<>(), institutionMembers = new HashSet<>(), institutionAdmins = new HashSet<>();
    private Boolean isActive = true, isPublic = false;

}
