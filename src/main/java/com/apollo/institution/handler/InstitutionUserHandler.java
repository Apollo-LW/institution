package com.apollo.institution.handler;

import com.apollo.institution.constant.RoutingConstant;
import com.apollo.institution.model.Institution;
import com.apollo.institution.service.InstitutionUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class InstitutionUserHandler {

    private final InstitutionUserService institutionUserService;

    public @NotNull Mono<ServerResponse> getUserInstitution(ServerRequest request) {
        final String userId = request.pathVariable(RoutingConstant.USER_ID);
        final Flux<Institution> institutionFlux = this.institutionUserService.getUserInstitutions(userId).flatMap(optionalInstitution -> optionalInstitution.map(Flux::just).orElseGet(Flux::empty));
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(institutionFlux , Institution.class);
    }

}
