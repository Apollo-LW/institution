package com.apollo.institution.model;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class InstitutionJoinRequest {

    private String institutionIdA , institutionIdB , userId , courseId;
    private Date requestDateOfCreation = Calendar.getInstance().getTime();

}
