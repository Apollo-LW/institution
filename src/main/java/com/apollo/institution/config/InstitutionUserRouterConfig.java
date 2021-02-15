package com.apollo.institution.config;

import com.apollo.institution.constant.RoutingConstant;
import com.apollo.institution.handler.InstitutionUserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class InstitutionUserRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeUserInstitution(InstitutionUserHandler institutionUserHandler) {
        return RouterFunctions
                .route()
                .path(RoutingConstant.INSTITUTION_USER_PATH , routeFunctionBuilder ->
                        routeFunctionBuilder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON) , builder -> builder
                                .GET(RoutingConstant.USER_ID_PATH , institutionUserHandler::getUserInstitution)))
                .build();
    }

}
