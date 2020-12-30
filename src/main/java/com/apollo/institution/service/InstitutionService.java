package com.apollo.institution.service;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface InstitutionService {

    Mono<Boolean> addMembers(String adminId , String institutionId , Flux<String> membersIds);

    Mono<Boolean> addAdmins(String adminId , String institutionId , Flux<String> ownerIds);

    Mono<Boolean> addCourse(Mono<InstitutionCourse> institutionCourseMono , String adminId);

    Mono<Optional<Institution>> getInstitutionById(String institutionId);

    Mono<Optional<Institution>> createInstitution(Mono<Institution> institutionMono);

    Mono<Boolean> updateInstitution(String adminId , Mono<Institution> institutionMono);

    Mono<Boolean> deleteInstitution(String adminId , String institutionId);
}
