package com.apollo.institution.service.impl;

import com.apollo.institution.kafka.KafkaService;
import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import com.apollo.institution.model.InstitutionUser;
import com.apollo.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstitutionServiceImpl implements InstitutionService {

    @Value("${institution.kafka.store}")
    private String institutionStateStoreName;
    private final KafkaService kafkaService;
    private final InteractiveQueryService interactiveQueryService;
    private ReadOnlyKeyValueStore<String, Institution> institutionStateStore;

    private ReadOnlyKeyValueStore<String, Institution> getInstitutionStateStore() {
        if (this.institutionStateStore == null)
            this.institutionStateStore = this.interactiveQueryService.getQueryableStore(this.institutionStateStoreName , QueryableStoreTypes.keyValueStore());
        return this.institutionStateStore;
    }

    private boolean isValid(Optional<Institution> institution , String adminId) {
        return institution.isEmpty() || !institution.get().getIsActive() || !institution.get().getInstitutionAdmins().contains(adminId);
    }

    @Override
    public Mono<Institution> createInstitution(Mono<Institution> institutionMono) {
        return this.kafkaService.sendInstitutionRecord(institutionMono).map(Optional::get);
    }

    @Override
    public Mono<Boolean> addCourse(Mono<InstitutionCourse> institutionCourseMono) {
        //TODO
        return null;
    }

    @Override
    public Mono<Boolean> addMembers(Flux<InstitutionUser> institutionUserFlux) {
        //TODO
        return null;
    }


    @Override
    public Mono<Institution> updateInstitution(Mono<Institution> institutionMono , String adminId) {
        return institutionMono.flatMap(institution -> {
            Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(institution.getInstitutionId()));
            if (this.isValid(optionalInstitution , adminId)) return Mono.empty();
            Institution updatedInstitution = optionalInstitution.get();
            updatedInstitution.setInstitutionName(institution.getInstitutionName());
            updatedInstitution.setInstitutionAdmins(institution.getInstitutionAdmins());
            updatedInstitution.setInstitutionChildren(institution.getInstitutionChildren());
            updatedInstitution.setInstitutionMembers(institution.getInstitutionMembers());
            updatedInstitution.setInstitutionParents(institution.getInstitutionParents());
            updatedInstitution.setIsActive(institution.getIsActive());
            updatedInstitution.setIsPublic(institution.getIsPublic());
            return Mono.just(updatedInstitution);
        });
    }

    @Override
    public Mono<Institution> deleteInstitution(Mono<Institution> institutionMono , String adminId) {
        return institutionMono.flatMap(institution -> {
            Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(institution.getInstitutionId()));
            if (this.isValid(optionalInstitution , adminId)) return Mono.empty();
            Institution deletedInstitution = optionalInstitution.get();
            deletedInstitution.setIsActive(false);
            return this.kafkaService.sendInstitutionRecord(Mono.just(deletedInstitution)).map(Optional::get);
        });
    }

    @Override
    public Mono<Optional<Institution>> getInstitutionById(String institutionId) {
        return Mono.just(Optional.ofNullable(this.getInstitutionStateStore().get(institutionId)));
    }
}
