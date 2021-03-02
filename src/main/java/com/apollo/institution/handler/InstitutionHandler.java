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

/**
 * Main Institution handler for the Institution API
 */
@Component
@RequiredArgsConstructor
public class InstitutionHandler {

    /**
     * Institution Service that handle API operations from the event side
     */
    private final InstitutionService institutionService;

    /**
     * @param request
     *
     * @return
     */
    public @NotNull Mono<ServerResponse> getInstitutionById(final ServerRequest request) {
        final String institutionId = request.pathVariable(RoutingConstant.INSTITUTION_ID);
        final Mono<Institution> institutionMono = this.institutionService.getInstitutionById(institutionId).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(institutionMono , Institution.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    public @NotNull Mono<ServerResponse> createInstitution(final ServerRequest request) {
        final Mono<Institution> institutionMono = request.bodyToMono(Institution.class);
        final Mono<Institution> createdInstitutionMono = this.institutionService.createInstitution(institutionMono).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok()
                .body(createdInstitutionMono , Institution.class)
                .switchIfEmpty(ServerResponse.unprocessableEntity().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    public @NotNull Mono<ServerResponse> endorseCourse(final ServerRequest request) {
        final Mono<InstitutionCourse> institutionCourseMono = request.bodyToMono(InstitutionCourse.class);
        final String adminId = request.pathVariable(RoutingConstant.ADMIN_ID);
        final Mono<Boolean> isCourseEndorsed = this.institutionService.addCourse(institutionCourseMono , adminId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isCourseEndorsed , Boolean.class)
                .switchIfEmpty(ServerResponse.badRequest().build())
                .doOnError(throwable -> ServerResponse.unprocessableEntity().build());
    }

    public @NotNull Mono<ServerResponse> addMembers(final ServerRequest request) {
        final Mono<ModifyInstitution> modifyInstitutionMono = request.bodyToMono(ModifyInstitution.class);
        final Mono<Boolean> isMembersAdded = this.institutionService.addMembers(modifyInstitutionMono);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isMembersAdded , Boolean.class)
                .switchIfEmpty(ServerResponse.badRequest().build())
                .doOnError(throwable -> ServerResponse.unprocessableEntity().build());
    }

    public @NotNull Mono<ServerResponse> addAdmins(final ServerRequest request) {
        final Mono<ModifyInstitution> modifyInstitutionMono = request.bodyToMono(ModifyInstitution.class);
        final Mono<Boolean> isAdminsAdded = this.institutionService.addAdmins(modifyInstitutionMono);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isAdminsAdded , Boolean.class)
                .switchIfEmpty(ServerResponse.badRequest().build())
                .doOnError(throwable -> ServerResponse.unprocessableEntity().build());
    }

    public @NotNull Mono<ServerResponse> updateInstitution(final ServerRequest request) {
        final String adminId = request.pathVariable(RoutingConstant.ADMIN_ID);
        final Mono<Institution> institutionMono = request.bodyToMono(Institution.class);
        final Mono<Boolean> isInstitutionUpdated = this.institutionService.updateInstitution(institutionMono , adminId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isInstitutionUpdated , Boolean.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    public @NotNull Mono<ServerResponse> deleteInstitution(final ServerRequest request) {
        final Mono<ModifyInstitution> modifyInstitutionMono = request.bodyToMono(ModifyInstitution.class);
        final Mono<Boolean> isInstitutionDeleted = this.institutionService.deleteInstitution(modifyInstitutionMono);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isInstitutionDeleted , Boolean.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

}
