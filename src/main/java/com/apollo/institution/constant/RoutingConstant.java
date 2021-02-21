package com.apollo.institution.constant;

public interface RoutingConstant {

    String INSTITUTION_PATH = "/institution";
    String INSTITUTION_USER_PATH = INSTITUTION_PATH + "/user";
    String ENDORSE_PATH = "/endorse";
    String ADD_PATH = "/add";
    String ADMIN_PATH = "/admin";
    String MEMBER_PATH = "/member";
    String INSTITUTION_ID = "institutionId";
    String INSTITUTION_ID_PATH = "/{" + INSTITUTION_ID + "}";
    String ADMIN_ID = "adminId";
    String USER_ID = "userId";
    String USER_ID_PATH = "/{" + USER_ID + "}";
    String ADMIN_ID_PATH = "/{" + ADMIN_ID + "}";
    String ENDORSE_COURSE_PATH = ENDORSE_PATH + ADMIN_ID_PATH;
    String ADMIN_ID_INSTITUTION_ID = ADMIN_ID_PATH + INSTITUTION_ID_PATH;

    String ADD_MEMBERS_PATH = ADD_PATH + MEMBER_PATH + ADMIN_ID_INSTITUTION_ID;
    String ADD_ADMINS_PATH = ADD_PATH + ADMIN_PATH + ADMIN_ID_INSTITUTION_ID;
}
