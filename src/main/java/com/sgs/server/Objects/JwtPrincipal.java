package com.sgs.server.Objects;

public record JwtPrincipal(
        long userId,
        UserType userType
) {}