package com.nqvinh.rentofficebackend.application.constant;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class UrlConstant {
     public static final String API_BASE = "/api/v1";

     //Role
     public static final String ROLES = API_BASE + "/roles";
     public static final String UPDATE_ROLE = ROLES + "/{id}";
     public static final String DELETE_ROLE = ROLES + "/{id}";
     public static final String GET_ALL_ROLE = ROLES + "/all";

     //User
        public static final String USERS = API_BASE + "/users";
        public static final String UPDATE_USER = USERS + "/{id}";
        public static final String DELETE_USER = USERS + "/{id}";

}