
package com.sgs.server;

import com.sgs.server.Objects.JwtPrincipal;
import com.sgs.server.Objects.UserType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityHolderContext {

    private SecurityHolderContext() {
        // Utility class: do not instantiate.
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }

    public static JwtPrincipal getPrincipal() {
        Authentication authentication = getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof JwtPrincipal jwtPrincipal) {
            return jwtPrincipal;
        }

        return null;
    }

    public static Long getCurrentUserId() {
        JwtPrincipal principal = getPrincipal();

        return principal != null
                ? principal.userId()
                : null;
    }

    public static UserType getCurrentUserType() {
        JwtPrincipal principal = getPrincipal();

        return principal != null
                ? principal.userType()
                : null;
    }

    public static boolean isAuthenticated() {
        return getPrincipal() != null;
    }

    public static void setAuthentication(
            Authentication authentication
    ) {
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

    public static void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
