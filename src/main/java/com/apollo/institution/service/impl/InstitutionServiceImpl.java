package com.apollo.institution.service.impl;

import com.apollo.institution.kafka.KafkaService;
import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import com.apollo.institution.model.InstitutionJoinRequest;
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
    public Mono<Boolean> addMembers(Flux<String> membersIds , String institutionId , String adminId) {
        Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(institutionId));
        if (this.isNotValid(optionalInstitution , adminId)) return Mono.just(false);
        Institution institution = optionalInstitution.get();
        return membersIds
                .flatMap(memberId -> Mono.just(institution.addMember(memberId) != null))
                .all(result -> result)
                .flatMap(result -> this.kafkaService.sendInstitutionRecord(Mono.just(institution)).map(Optional::isPresent));
    }


    @Override
    public Mono<Optional<Institution>> updateInstitution(Mono<Institution> institutionMono , String adminId) {
        return institutionMono.flatMap(institution -> {
            Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(institution.getInstitutionId()));
            if (this.isNotValid(optionalInstitution , adminId)) return Mono.empty();
            Institution updatedInstitution = optionalInstitution.get();
            updatedInstitution.setInstitutionName(institution.getInstitutionName());
            updatedInstitution.setIsActive(institution.getIsActive());
            updatedInstitution.setIsPublic(institution.getIsPublic());
            return this.kafkaService.sendInstitutionRecord(Mono.just(updatedInstitution));
        });
    }

    @Override
    public Mono<Boolean> deleteInstitution(Mono<Institution> institutionMono , String adminId) {
        return institutionMono.flatMap(institution -> {
            Optional<Institution> optionalInstitution = Optional.ofNullable(this.getInstitutionStateStore().get(institution.getInstitutionId()));
            if (this.isNotValid(optionalInstitution , adminId)) return Mono.empty();
            Institution deletedInstitution = optionalInstitution.get();
            deletedInstitution.setIsActive(false);
            return this.kafkaService.sendInstitutionRecord(Mono.just(deletedInstitution)).map(Optional::isPresent);
        });
    }

    @Override
    public Mono<Boolean> joinCourse(Mono<InstitutionCourse> institutionCourseMono , String adminIdA , String adminIdB) {
        return institutionCourseMono.flatMap(institutionCourse -> {
            Optional<Institution> optionalInstitutionA = Optional.ofNullable(this.getInstitutionStateStore().get(institutionCourse.getInstitutionId()));
            if (this.isNotValid(optionalInstitutionA , adminIdA)) return Mono.just(false);
            Optional<Institution> optionalInstitutionB = Optional.ofNullable(this.getInstitutionStateStore().get(institutionCourse.getJoinInstitutionId()));
            if (this.isNotValid(optionalInstitutionB , adminIdB)) return Mono.just(false);
            return this.kafkaService
                    .sendInstitutionRecord(Mono.just(optionalInstitutionA.get().addCourseById(institutionCourse.getCourseId()))).map(Optional::isPresent)
                    .flatMap(result -> this.kafkaService
                            .sendInstitutionRecord(Mono.just(optionalInstitutionB.get().addCourseById(institutionCourse.getCourseId())))
                            .map(optionalInstitution -> result && optionalInstitution.isPresent()));
        });
    }

    @Override
    public Mono<Boolean> createJoinRequest(Mono<InstitutionJoinRequest> institutionJoinRequestMono , String adminIdA , String adminIdB) {
        //TODO: create requests as well as create a way to send requests to kafka and processes them
        return null;
    }

    @Override
    public Mono<Optional<Institution>> getInstitutionById(String institutionId) {
        return Mono.just(Optional.ofNullable(this.getInstitutionStateStore().get(institutionId)));
    }
}
