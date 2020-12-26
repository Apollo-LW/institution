package com.apollo.institution.service;

import com.apollo.institution.model.Institution;
import reactor.core.publisher.Flux;

public interface InstitutionUserService {

    Flux<Institution> getUserInstitutions(String userId);
}
