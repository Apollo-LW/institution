package com.apollo.institution.config;

import com.apollo.institution.constant.RoutingConstant;
import com.apollo.institution.handler.InstitutionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class InstitutionRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeInstitution(final InstitutionHandler institutionHandler) {
        return RouterFunctions
                .route()
                .path(RoutingConstant.INSTITUTION_PATH , routeFunctionBuilder ->
                        routeFunctionBuilder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON) , builder -> builder
                                .GET(RoutingConstant.INSTITUTION_ID_PATH , institutionHandler::getInstitutionById)
                                .POST(institutionHandler::createInstitution)
                                .PUT(RoutingConstant.ENDORSE_COURSE_PATH , institutionHandler::endorseCourse)
                                .PUT(RoutingConstant.ADD_MEMBERS_PATH , institutionHandler::addMembers)
                                .PUT(RoutingConstant.ADD_ADMINS_PATH , institutionHandler::addAdmins)
                                .PUT(RoutingConstant.ADMIN_ID_PATH , institutionHandler::updateInstitution)
                                .DELETE(RoutingConstant.ADMIN_ID_INSTITUTION_ID , institutionHandler::deleteInstitution)))
                .build();
    }

}
