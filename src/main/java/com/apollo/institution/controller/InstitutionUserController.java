package com.apollo.institution.controller;

import com.apollo.institution.model.Institution;
import com.apollo.institution.service.InstitutionUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/institution/user")
@RequiredArgsConstructor
public class InstitutionUserController {

    private final InstitutionUserService institutionUserService;

    @GetMapping("/{userId}")
    public Flux<Institution> getUserInstitution(@PathVariable("userId") String userId) {
        return this.institutionUserService.getUserInstitutions(userId);
    }

}
