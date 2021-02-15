package com.apollo.institution.model;

import lombok.Data;

import java.util.*;

@Data
public class Institution {

    private Boolean isActive = true, isPublic = false;
    private HashSet<String> institutionCourse = new HashSet<>();
    private HashSet<String> institutionAdmins = new HashSet<>(), institutionParents = new HashSet<>();
    private HashSet<String> institutionMembers = new HashSet<>(), institutionChildren = new HashSet<>();
    private final String institutionId = UUID.randomUUID().toString();
    private final Date institutionDateOfCreation = Calendar.getInstance().getTime();
    private String institutionName = this.institutionId + '-' + this.institutionDateOfCreation, institutionRoomId;

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

    public Set<String> getAllInstitutionMembers() {
        HashSet<String> allInstitutionMembers = new HashSet<>();
        allInstitutionMembers.addAll(this.institutionMembers);
        allInstitutionMembers.addAll(this.institutionAdmins);
        return allInstitutionMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Institution that = (Institution) o;
        return institutionId.equals(that.institutionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(institutionId);
    }
}
