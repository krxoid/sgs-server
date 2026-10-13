package com.sgs.server;

import com.sgs.server.Objects.EmployeeRepository;
import com.sgs.server.Objects.JwtPrincipal;
import com.sgs.server.Objects.UserType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication
        .UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority
        .SimpleGrantedAuthority;
import org.springframework.security.core.context
        .SecurityContextHolder;
import org.springframework.security.web.authentication
        .WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtServicer jwtServicer;
    private final EmployeeRepository employeeRepository;

    public JwtAuthFilter(
            JwtServicer jwtServicer,
            EmployeeRepository employeeRepository
    ) {
        this.jwtServicer = jwtServicer;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (SecurityContextHolder.getContext()
                    .getAuthentication() == null
                    && jwtServicer.validateToken(token)) {

                try {
                    long userId = jwtServicer.extractUserId(token);
                    UserType userType =
                            jwtServicer.extractUserType(token);

                    JwtPrincipal principal =
                            new JwtPrincipal(userId, userType);

                    String role = userType.name();

                    if (userType == UserType.EMPLOYEE) {
                        role = employeeRepository.findByIdNumber(userId)
                                .map(employee ->
                                        employee.isAdmin() ? "ADMIN" : "EMPLOYEE"
                                )
                                .orElseThrow();
                    }

                    var authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + role)
                    );

                    var authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principal,
                                    null,
                                    authorities
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                } catch (RuntimeException ex) {
                    SecurityContextHolder.clearContext();
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
