package com.apollo.institution.model;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

@Data
public class Institution {

    private Boolean isActive = true, isPublic = false;
    private HashSet<String> institutionCourse = new HashSet<>();
    private HashSet<String> institutionAdmins = new HashSet<>();
    private HashSet<String> institutionParents = new HashSet<>();
    private HashSet<String> institutionMembers = new HashSet<>();
    private HashSet<String> institutionChildren = new HashSet<>();
    private final String institutionId = UUID.randomUUID().toString();
    private final Date institutionDateOfCreation = Calendar.getInstance().getTime();
    private String institutionName = this.institutionId + '-' + this.institutionDateOfCreation;

    public Institution addCourseById(String courseId) {
        this.institutionCourse.add(courseId);
        return this;
    }

    public Institution addMember(String memberId) {
        this.institutionMembers.add(memberId);
        return this;
    }

    public Institution addAdmin(String ownerId) {
        this.institutionAdmins.add(ownerId);
        return this;
    }

}
