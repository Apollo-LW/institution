package com.apollo.institution.service.impl;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionUser;
import com.apollo.institution.service.InstitutionUserService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstitutionUserServiceImpl implements InstitutionUserService {

    @Value("${user.kafka.store}")
    private String institutionUserStateStoreName;
    private final InteractiveQueryService interactiveQueryService;
    private ReadOnlyKeyValueStore<String , InstitutionUser> institutionUserStateStore;

    private ReadOnlyKeyValueStore<String , InstitutionUser> getInstitutionUserStateStore() {
        if(this.institutionUserStateStore == null)
            this.institutionUserStateStore = this.interactiveQueryService.getQueryableStore(this.institutionUserStateStoreName , QueryableStoreTypes.keyValueStore());
        return this.institutionUserStateStore;
    }

    @Override
    public Flux<Institution> getUserInstitutions(String userId) {
        Optional<InstitutionUser> optionalInstitutionUser = Optional.ofNullable(this.getInstitutionUserStateStore().get(userId));
        if(optionalInstitutionUser.isEmpty()) return Flux.empty();
        return Flux.fromIterable(optionalInstitutionUser.get().getUserInstitutions());
    }
}
