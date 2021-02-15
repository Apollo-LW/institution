package com.apollo.institution.service;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import com.apollo.institution.model.ModifyInstitution;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface InstitutionService {

    Mono<Boolean> addMembers(Mono<ModifyInstitution> modifyInstitutionMono);

    Mono<Boolean> addAdmins(Mono<ModifyInstitution> modifyInstitutionMono);

    Mono<Boolean> addCourse(Mono<InstitutionCourse> institutionCourseMono , String adminId);

    Mono<Optional<Institution>> getInstitutionById(String institutionId);

    Mono<Optional<Institution>> createInstitution(Mono<Institution> institutionMono);

    Mono<Boolean> updateInstitution(String adminId , Mono<Institution> institutionMono);

    Mono<Boolean> deleteInstitution(Mono<ModifyInstitution> modifyInstitutionMono);
}
