package com.alberto.workout_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    
    private static UserService userService;
    
    @Autowired
    public void setUserService(UserService userService) {
        SecurityUtils.userService = userService;
    }
    
    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            
            Object userIdClaim = jwt.getClaim("sub");
            if (userIdClaim != null) {
                try {
                    return Long.parseLong(userIdClaim.toString());
                } catch (NumberFormatException e) {
                }
            }
            
            Object usernameClaim = jwt.getClaim("username");
            if (usernameClaim != null) {
                String email = usernameClaim.toString();
                return getUserIdFromEmail(email);
            }
        }
        
        return null;
    }
    
    private static Long getUserIdFromEmail(String email) {
        if (userService != null) {
            return userService.getUserIdByEmail(email);
        }
        return null;
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
