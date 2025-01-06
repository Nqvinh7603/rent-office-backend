package com.nqvinh.rentofficebackend.application.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlConstant {
    public static final String API_BASE = "/api/v1";

    //Role
    public static final String ROLES = API_BASE + "/roles";
    public static final String UPDATE_ROLE = "/{id}";
    public static final String DELETE_ROLE = "/{id}";
    public static final String GET_ALL_ROLE = "/all";
    public static final String GET_ROLE_BY_ID = "/{id}";

    //User
    public static final String USERS = API_BASE + "/users";
    public static final String UPDATE_USER ="/{id}";
    public static final String DELETE_USER ="/{id}";


    //Permission
    public static final String PERMISSIONS = API_BASE + "/permissions";
    public static final String UPDATE_PERMISSION = "/{id}";
    public static final String DELETE_PERMISSION = "/{id}";
    public static final String GET_ALL_PERMISSION = "/all";

    //Auth
    public static final String AUTH = API_BASE + "/auth";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REFRESH_TOKEN = "/refresh-token";
    public static final String FAILURE = "/failure";


}