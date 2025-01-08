//package com.nqvinh.rentofficebackend.infrastructure.config.permission;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class PermissionInterceptorConfig implements WebMvcConfigurer {
//
//    CustomPermissionInterceptor permissionInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        String[] whiteList = {
//                "/", "/api/v1/auth/**",
//        };
//        registry.addInterceptor(permissionInterceptor)
//                .excludePathPatterns(whiteList);
//    }
//}