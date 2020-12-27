package com.apollo.institution.controller;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
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
            if (optionalInstitution.isEmpty()) return Mono.empty();
            return Mono.just(optionalInstitution.get());
        });
    }

    @PostMapping("/")
    public Mono<Institution> createInstitution(@RequestBody Mono<Institution> institutionMono) {
        return this.institutionService.createInstitution(institutionMono).flatMap(Mono::justOrEmpty);
    }

    @PutMapping("/share/{adminId}")
    public Mono<Boolean> endorseCourse(@RequestBody Mono<InstitutionCourse> institutionCourseMono , @PathVariable("adminId") String adminId) {
        return this.institutionService.endorseCourse(institutionCourseMono , adminId);
    }

    @PutMapping("/join/{adminIdA}/{adminIdB}")
    public Mono<Boolean> joinCourse(@RequestBody Mono<InstitutionCourse> institutionCourseMono , @PathVariable("adminIdA") String adminIdA , @PathVariable("adminIdB") String adminIdB) {
        return this.institutionService.joinCourse(institutionCourseMono , adminIdA , adminIdB);
    }

    @PutMapping("/{adminId}")
    public Mono<Institution> updateInstitution(@PathVariable("adminId") String adminId , @RequestBody Mono<Institution> institutionMono) {
        return this.institutionService.updateInstitution(institutionMono , adminId).flatMap(Mono::justOrEmpty);
    }

    @DeleteMapping("/{adminId}")
    public Mono<Boolean> deleteInstitution(@PathVariable("adminId") String adminId , @RequestBody Mono<Institution> institutionMono) {
        return this.institutionService.deleteInstitution(institutionMono , adminId).flatMap(Mono::justOrEmpty);
    }

}
