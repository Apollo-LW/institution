package com.apollo.institution.service.impl;

import com.apollo.institution.kafka.KafkaService;
import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
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

    private boolean isNotValid(Optional<Institution> institution , String adminId) {
        return institution.isEmpty() || !institution.get().getIsActive() || !institution.get().getInstitutionAdmins().contains(adminId);
    }

    @Override
    public Mono<Optional<Institution>> createInstitution(Mono<Institution> institutionMono) {
        return this.kafkaService.sendInstitutionRecord(institutionMono);
    }

    @Override
    public Mono<Boolean> addCourse(Mono<InstitutionCourse> institutionCourseMono , String adminId) {
        return institutionCourseMono.flatMap(institutionCourse -> {
            Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(institutionCourse.getInstitutionId()));
            if (this.isNotValid(optionalInstitution , adminId)) return Mono.just(false);
            return this.kafkaService.sendInstitutionRecord(Mono.just(optionalInstitution.get().addCourseById(institutionCourse.getCourseId()))).map(Optional::isPresent);
        });
    }

    @Override
    public Mono<Boolean> addMembers(String adminId , String institutionId , Flux<String> membersIds) {
        Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(institutionId));
        if (this.isNotValid(optionalInstitution , adminId)) return Mono.just(false);
        Institution institution = optionalInstitution.get();
        return membersIds
                .flatMap(memberId -> Mono.just(institution.addMember(memberId) != null))
                .all(result -> result)
                .flatMap(result -> this.kafkaService.sendInstitutionRecord(Mono.just(institution)).map(Optional::isPresent));
    }

    @Override
    public Mono<Boolean> addAdmins(String adminId , String institutionId , Flux<String> adminsId) {
        Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(institutionId));
        if (this.isNotValid(optionalInstitution , adminId)) return Mono.just(false);
        Institution institution = optionalInstitution.get();
        return adminsId
                .flatMap(adminIdToAdd -> Mono.just(institution.addAdmin(adminIdToAdd) != null))
                .all(result -> result)
                .flatMap(result -> this.kafkaService.sendInstitutionRecord(Mono.just(institution)).map(Optional::isPresent));
    }


    @Override
    public Mono<Boolean> updateInstitution(String adminId , Mono<Institution> institutionMono) {
        return institutionMono.flatMap(updatedInstitution -> {
            Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(updatedInstitution.getInstitutionId()));
            if (this.isNotValid(optionalInstitution , adminId)) return Mono.just(false);
            Institution institution = optionalInstitution.get();
            institution.setInstitutionName(updatedInstitution.getInstitutionName());
            institution.setIsPublic(updatedInstitution.getIsPublic());
            institution.setIsActive(updatedInstitution.getIsActive());
            return this.kafkaService.sendInstitutionRecord(Mono.just(institution)).map(Optional::isPresent);
        });
    }

    @Override
    public Mono<Boolean> deleteInstitution(String adminId , String institutionId) {
        Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(institutionId));
        if (this.isNotValid(optionalInstitution , adminId)) return Mono.just(false);
        Institution institution = optionalInstitution.get();
        institution.setIsActive(false);
        return this.kafkaService.sendInstitutionRecord(Mono.just(institution)).map(Optional::isPresent);
    }

    @Override
    public Mono<Optional<Institution>> getInstitutionById(String institutionId) {
        return Mono.just(Optional.ofNullable(this.getInstitutionStateStore().get(institutionId)));
    }
}
