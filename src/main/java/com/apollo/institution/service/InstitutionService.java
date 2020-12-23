package com.apollo.institution.service;

import com.apollo.institution.model.Institution;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface InstitutionService {

    Mono<Institution> createInstitution(Mono<Institution> institutionMono);
    Mono<Optional<Institution>> getInstitutionById(String institutionId);
    Mono<Institution> updateInstitution(Mono<Institution> institutionMono , String adminId);
    Mono<Institution> deleteInstitution(Mono<Institution> institutionMono , String adminId);

}
