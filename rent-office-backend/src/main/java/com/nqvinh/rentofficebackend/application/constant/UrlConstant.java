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
    public static final String UPDATE_USER = "/{id}";
    public static final String DELETE_USER = "/{id}";
    public static final String LOGGED_IN_USER = "/logged-in";
    public static final String GET_USER_BY_ID = "/{id}";
    public static final String CHANGE_PASSWORD = "/change-password";
    public static final String GET_ALL_STAFF = "/staffs";

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
    public static final String FORGOT_PASSWORD = "/forgot-password";
    public static final String RESET_PASSWORD = "/reset-password";
    public static final String VERIFY_RESET_TOKEN = "/verify-reset-token";


    //Building
    public static final String BUILDINGS = API_BASE + "/buildings";
    public static final String UPDATE_BUILDING = "/{id}";
    public static final String DELETE_BUILDING = "/{id}";
    public static final String GET_BUILDING_BY_ID = "/{id}";

    public static final String BUILDING_TYPES = API_BASE + "/building-types";
    public static final String UPDATE_BUILDING_TYPE = "/{id}";
    public static final String DELETE_BUILDING_TYPE = "/{id}";
    public static final String GET_ALL_BUILDING_TYPE = "/all";

    public static final String BUILDING_LEVELS = API_BASE + "/building-levels";
    public static final String UPDATE_BUILDING_LEVEL = "/{id}";
    public static final String DELETE_BUILDING_LEVEL = "/{id}";
    public static final String GET_ALL_BUILDING_LEVEL = "/all";

    //Customer
    public static final String CUSTOMERS = API_BASE + "/customers";
    public static final String ASSIGN_CUSTOMER = "/assign-customer";
    public static final String GET_STAFFS_BY_CUSTOMER_ID = "/{id}/staffs";

}