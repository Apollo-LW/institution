package com.apollo.institution.handler;

import com.apollo.institution.constant.RoutingConstant;
import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import com.apollo.institution.model.ModifyInstitution;
import com.apollo.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class InstitutionHandler {

    private final InstitutionService institutionService;

    public @NotNull Mono<ServerResponse> getInstitutionById(ServerRequest request) {
        final String institutionId = request.pathVariable(RoutingConstant.INSTITUTION_ID);
        final Mono<Institution> institutionMono = this.institutionService.getInstitutionById(institutionId).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(institutionMono , Institution.class);
    }

    public @NotNull Mono<ServerResponse> createInstitution(ServerRequest request) {
        final Mono<Institution> institutionMono = request.bodyToMono(Institution.class);
        final Mono<Institution> createdInstitutionMono = this.institutionService.createInstitution(institutionMono).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok()
                .body(createdInstitutionMono , Institution.class);
    }

    public @NotNull Mono<ServerResponse> endorseCourse(ServerRequest request) {
        final Mono<InstitutionCourse> institutionCourseMono = request.bodyToMono(InstitutionCourse.class);
        final String adminId = request.pathVariable(RoutingConstant.ADMIN_ID);
        final Mono<Boolean> isCourseEndorsed = this.institutionService.addCourse(institutionCourseMono , adminId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isCourseEndorsed , Boolean.class);
    }

    public @NotNull Mono<ServerResponse> addMembers(ServerRequest request) {
        final Mono<ModifyInstitution> modifyInstitutionMono = request.bodyToMono(ModifyInstitution.class);
        final Mono<Boolean> isMembersAdded = this.institutionService.addMembers(modifyInstitutionMono);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isMembersAdded , Boolean.class);
    }

    public @NotNull Mono<ServerResponse> addAdmins(ServerRequest request) {
        final Mono<ModifyInstitution> modifyInstitutionMono = request.bodyToMono(ModifyInstitution.class);
        final Mono<Boolean> isAdminsAdded = this.institutionService.addAdmins(modifyInstitutionMono);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isAdminsAdded , Boolean.class);
    }

    public @NotNull Mono<ServerResponse> updateInstitution(ServerRequest request) {
        final String adminId = request.pathVariable(RoutingConstant.ADMIN_ID);
        final Mono<Institution> institutionMono = request.bodyToMono(Institution.class);
        final Mono<Boolean> isInstitutionUpdated = this.institutionService.updateInstitution(adminId , institutionMono);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isInstitutionUpdated , Boolean.class);
    }

    public @NotNull Mono<ServerResponse> deleteInstitution(ServerRequest request) {
        final Mono<ModifyInstitution> modifyInstitutionMono = request.bodyToMono(ModifyInstitution.class);
        final Mono<Boolean> isInstitutionDeleted = this.institutionService.deleteInstitution(modifyInstitutionMono);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isInstitutionDeleted , Boolean.class);
    }

}
