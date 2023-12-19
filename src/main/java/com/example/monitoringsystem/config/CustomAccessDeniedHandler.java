//package com.example.monitoringsystem.config;
//
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
//            throws IOException {
//        // Set custom response for access denied
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
////        response.getWriter().write("You don't have access");
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write("{\"message\":\"You don't have access\"}");
//        // You can also redirect to a custom error page if needed
//        // response.sendRedirect("/access-denied");
//    }
//}
