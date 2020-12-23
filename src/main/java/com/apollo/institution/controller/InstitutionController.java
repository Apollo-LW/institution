package com.apollo.institution.controller;

import com.apollo.institution.model.Institution;
import com.apollo.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @GetMapping("/{institutionId}")
    public Mono<Institution> getInstitutionById(@PathVariable("institutionId") String institutionId) {
        return this.institutionService.getInstitutionById(institutionId).flatMap(optionalInstitution -> {
            if(optionalInstitution.isEmpty()) return Mono.empty();
            return Mono.just(optionalInstitution.get());
        });
    }

    @PostMapping("/")
    public Mono<Institution> createInstitution(@RequestBody Mono<Institution> institutionMono) {
        return this.institutionService.createInstitution(institutionMono);
    }

    @PutMapping("/{adminId}")
    public Mono<Institution> updateInstitution(@PathVariable("adminId") String adminId , @RequestBody Mono<Institution> institutionMono) {
        return this.institutionService.updateInstitution(institutionMono , adminId);
    }

    @DeleteMapping("/{adminId}")
    public Mono<Institution> deleteInstitution(@PathVariable("adminId") String adminId , @RequestBody Mono<Institution> institutionMono) {
        return this.institutionService.deleteInstitution(institutionMono , adminId);
    }

}
