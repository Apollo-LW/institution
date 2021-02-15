package com.apollo.institution.constant;

public abstract class RoutingConstant {

    public static final String INSTITUTION_PATH = "/institution";
    public static final String INSTITUTION_USER_PATH = INSTITUTION_PATH + "/user";
    public static final String ENDORSE_PATH = "/endorse";
    public static final String ADD_PATH = "/add";
    public static final String ADMIN_PATH = "/admin";
    public static final String MEMBER_PATH = "/member";
    public static final String INSTITUTION_ID = "institutionId";
    public static final String INSTITUTION_ID_PATH = "/{" + INSTITUTION_ID + "}";
    public static final String ADMIN_ID = "adminId";
    public static final String USER_ID = "userId";
    public static final String USER_ID_PATH = "/{" + USER_ID + "}";
    public static final String ADMIN_ID_PATH = "/{" + ADMIN_ID + "}";
    public static final String ENDORSE_COURSE_PATH = ENDORSE_PATH + ADMIN_ID_PATH;
    public static final String ADMIN_ID_INSTITUTION_ID = ADMIN_ID_PATH + INSTITUTION_ID_PATH;

    public static final String ADD_MEMBERS_PATH = ADD_PATH + MEMBER_PATH + ADMIN_ID_INSTITUTION_ID;
    public static final String ADD_ADMINS_PATH = ADD_PATH + ADMIN_PATH + ADMIN_ID_INSTITUTION_ID;
}
