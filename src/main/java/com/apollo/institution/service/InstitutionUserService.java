package com.apollo.institution.service;

import com.apollo.institution.model.Institution;
import reactor.core.publisher.Flux;

import java.util.Optional;

public interface InstitutionUserService {

    Flux<Optional<Institution>> getUserInstitutions(String userId);
}
