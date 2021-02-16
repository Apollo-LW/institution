package com.apollo.institution.service;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import com.apollo.institution.model.ModifyInstitution;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface InstitutionService {

    Mono<Boolean> addMembers(final Mono<ModifyInstitution> modifyInstitutionMono);

    Mono<Boolean> addAdmins(final Mono<ModifyInstitution> modifyInstitutionMono);

    Mono<Boolean> addCourse(final Mono<InstitutionCourse> institutionCourseMono , String adminId);

    Mono<Optional<Institution>> getInstitutionById(final String institutionId);

    Mono<Optional<Institution>> createInstitution(final Mono<Institution> institutionMono);

    Mono<Boolean> updateInstitution(final Mono<Institution> institutionMono , final String adminId);

    Mono<Boolean> deleteInstitution(final Mono<ModifyInstitution> modifyInstitutionMono);
}
