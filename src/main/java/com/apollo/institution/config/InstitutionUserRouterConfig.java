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

/**
 * Main institution User API Router configuration that handle API operations and route them to the appropriate handler
 */
@Configuration
public class InstitutionUserRouterConfig {

    /**
     * Routing function for the Institution and User API that handle the routing between the request and the handler and the response
     * <p>
     * with data joins between User and Institution
     * </p>
     *
     * @param institutionUserHandler the router handler, so that the API can preform operations
     *
     * @return a configured router functions with the appropriate handler attached
     */
    @Bean
    public RouterFunction<ServerResponse> routeUserInstitution(final InstitutionUserHandler institutionUserHandler) {
        return RouterFunctions
                .route()
                .path(RoutingConstant.INSTITUTION_USER_PATH , routeFunctionBuilder ->
                        routeFunctionBuilder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON) , builder -> builder
                                .GET(RoutingConstant.USER_ID_PATH , institutionUserHandler::getUserInstitution)))
                .build();
    }

}
