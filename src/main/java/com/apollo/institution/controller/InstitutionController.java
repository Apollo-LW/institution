package com.apollo.institution.controller;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionCourse;
import com.apollo.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @GetMapping("/{institutionId}")
    public Mono<Institution> getInstitutionById(@PathVariable("institutionId") String institutionId) {
        return this.institutionService.getInstitutionById(institutionId).flatMap(Mono::justOrEmpty);
    }

    @PostMapping("/")
    public Mono<Institution> createInstitution(@RequestBody Mono<Institution> institutionMono) {
        return this.institutionService.createInstitution(institutionMono).flatMap(Mono::justOrEmpty);
    }

    @PutMapping("/endorse/{adminId}")
    public Mono<Boolean> endorseCourse(@RequestBody Mono<InstitutionCourse> institutionCourseMono , @PathVariable("adminId") String adminId) {
        return this.institutionService.addCourse(institutionCourseMono , adminId);
    }

    @PutMapping("/add/member/{adminId}/{institutionId}")
    public Mono<Boolean> addMembers(@PathVariable("adminId") String adminId , @PathVariable("institutionId") String institutionId , @RequestBody List<String> membersToAdd) {
        return this.institutionService.addMembers(adminId , institutionId , Flux.fromIterable(membersToAdd));
    }

    @PutMapping("/add/admin/{adminId}/{institutionId}")
    public Mono<Boolean> addAdmins(@PathVariable("adminId") String adminId , @PathVariable("institutionId") String institutionId , @RequestBody List<String> adminsToAdd) {
        return this.institutionService.addAdmins(adminId , institutionId , Flux.fromIterable(adminsToAdd));
    }

    @PutMapping("/{adminId}")
    public Mono<Boolean> updateInstitution(@PathVariable("adminId") String adminId , @RequestBody Mono<Institution> institutionMono) {
        return this.institutionService.updateInstitution(adminId , institutionMono);
    }

    @DeleteMapping("/{adminId}/{institutionId}")
    public Mono<Boolean> deleteInstitution(@PathVariable("adminId") String adminId , @PathVariable("institutionId") String institutionId) {
        return this.institutionService.deleteInstitution(adminId , institutionId);
    }

}
