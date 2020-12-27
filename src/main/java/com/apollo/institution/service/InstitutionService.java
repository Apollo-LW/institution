package com.apollo.institution.service;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface InstitutionService {

    Mono<Boolean> addMembers(Flux<String> membersIds , String institutionId , String adminId);

    Mono<Boolean> addCourse(Mono<InstitutionCourse> institutionCourseMono , String adminId);

    Mono<Optional<Institution>> getInstitutionById(String institutionId);

    Mono<Optional<Institution>> createInstitution(Mono<Institution> institutionMono);

    Mono<Optional<Institution>> updateInstitution(Mono<Institution> institutionMono , String adminId);

    Mono<Boolean> deleteInstitution(Mono<Institution> institutionMono , String adminId);

    Mono<Boolean> joinCourse(Mono<InstitutionCourse> institutionCourseMono , String adminIdA , String adminIdB);
}
