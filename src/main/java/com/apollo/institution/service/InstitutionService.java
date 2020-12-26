package com.apollo.institution.service;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import com.apollo.institution.model.InstitutionUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface InstitutionService {

    Mono<Boolean> addMembers(Flux<InstitutionUser> institutionUserFlux);
    Mono<Optional<Institution>> getInstitutionById(String institutionId);
    Mono<Boolean> addCourse(Mono<InstitutionCourse> institutionCourseMono);
    Mono<Institution> createInstitution(Mono<Institution> institutionMono);
    Mono<Institution> updateInstitution(Mono<Institution> institutionMono , String adminId);
    Mono<Institution> deleteInstitution(Mono<Institution> institutionMono , String adminId);

}
