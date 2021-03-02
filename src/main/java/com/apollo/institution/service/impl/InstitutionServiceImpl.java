package com.apollo.institution.service.impl;

import com.apollo.institution.constant.ErrorConstant;
import com.apollo.institution.kafka.KafkaService;
import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import com.apollo.institution.model.ModifyInstitution;
import com.apollo.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;
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

    private boolean isNotValid(final Optional<Institution> institution , final String adminId) {
        return institution.isEmpty() || !institution.get().getIsActive() || institution.get().doesNotHaveAdmin(adminId);
    }

    @Override
    public Mono<Optional<Institution>> createInstitution(final Mono<Institution> institutionMono) {
        return this.kafkaService.sendInstitutionRecord(institutionMono);
    }

    @Override
    public Mono<Boolean> addCourse(final Mono<InstitutionCourse> institutionCourseMono , final String adminId) {
        if (adminId == null)
            return Mono.error(new NullPointerException(ErrorConstant.ADMIN_ID_NULL));
        if (adminId.length() == 0)
            return Mono.error(new IllegalArgumentException(ErrorConstant.ADMIN_ID_EMPTY));

        return institutionCourseMono.flatMap(institutionCourse -> this.getInstitutionById(institutionCourse.getInstitutionId()).flatMap(institutionOptional -> {
            if (this.isNotValid(institutionOptional , adminId)) return Mono.just(false);
            final Institution institution = institutionOptional.get();
            institution.addCourseById(institutionCourse.getCourseId());
            return this.kafkaService.sendInstitutionRecord(Mono.just(institution)).map(Optional::isPresent);
        }));
    }

    @Override
    public Mono<Boolean> addMembers(final Mono<ModifyInstitution> modifyInstitutionMono) {
        return modifyInstitutionMono.flatMap(modifyInstitution ->
                this.getInstitutionById(modifyInstitution.getInstitutionId()).flatMap(optionalInstitution -> {
                    if (this.isNotValid(optionalInstitution , modifyInstitution.getAdminId())) return Mono.just(false);
                    final Institution institution = optionalInstitution.get();
                    Boolean isAdded = institution.addMembers(modifyInstitution.getUserIds());
                    return this.kafkaService.sendInstitutionRecord(Mono.just(institution)).map(updatedInstitutionOptional -> updatedInstitutionOptional.isPresent() && isAdded);
                }));
    }

    @Override
    public Mono<Boolean> addAdmins(final Mono<ModifyInstitution> modifyInstitutionMono) {
        return modifyInstitutionMono.flatMap(modifyInstitution ->
                this.getInstitutionById(modifyInstitution.getInstitutionId()).flatMap(optionalInstitution -> {
                    if (this.isNotValid(optionalInstitution , modifyInstitution.getAdminId())) return Mono.just(false);
                    final Institution institution = optionalInstitution.get();
                    Boolean isAdded = institution.addAdmins(modifyInstitution.getUserIds());
                    return this.kafkaService.sendInstitutionRecord(Mono.just(institution)).map(updatedInstitutionOptional -> updatedInstitutionOptional.isPresent() && isAdded);
                }));
    }


    @Override
    public Mono<Boolean> updateInstitution(final Mono<Institution> institutionMono , final String adminId) {
        if (adminId == null)
            return Mono.error(new NullPointerException(ErrorConstant.ADMIN_ID_NULL));
        if (adminId.length() == 0)
            return Mono.error(new IllegalArgumentException(ErrorConstant.ADMIN_ID_EMPTY));

        return institutionMono.flatMap(institution -> this.getInstitutionById(institution.getInstitutionId()).flatMap(institutionOptional -> {
            if (this.isNotValid(institutionOptional , adminId)) return Mono.just(false);
            Institution updatedInstitution = institutionOptional.get();
            updatedInstitution.setInstitutionName(institution.getInstitutionName());
            updatedInstitution.setIsPublic(institution.getIsPublic());
            updatedInstitution.setIsActive(institution.getIsActive());
            return this.kafkaService.sendInstitutionRecord(Mono.just(updatedInstitution)).map(Optional::isPresent);
        }));
    }

    @Override
    public Mono<Boolean> deleteInstitution(final Mono<ModifyInstitution> modifyInstitutionMono) {
        return modifyInstitutionMono.flatMap(modifyInstitution ->
                this.getInstitutionById(modifyInstitution.getInstitutionId()).flatMap(optionalInstitution -> {
                    if (this.isNotValid(optionalInstitution , modifyInstitution.getAdminId())) return Mono.just(false);
                    final Institution institution = optionalInstitution.get();
                    institution.setIsActive(false);
                    return this.kafkaService.sendInstitutionRecord(Mono.just(institution)).map(Optional::isPresent);
                }));
    }

    @Override
    public Mono<Optional<Institution>> getInstitutionById(final String institutionId) {
        if (institutionId == null)
            return Mono.error(new NullPointerException(ErrorConstant.INSTITUTION_ID_NULL));
        if (institutionId.length() == 0)
            return Mono.error(new IllegalArgumentException(ErrorConstant.INSTITUTION_ID_EMPTY));

        final Optional<Institution> institutionOptional = Optional.ofNullable(this.getInstitutionStateStore().get(institutionId));
        if (institutionOptional.isPresent() && !institutionOptional.get().getIsActive())
            return Mono.just(Optional.empty());
        return Mono.just(institutionOptional);
    }
}
