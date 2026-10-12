package com.sgs.server.Objects;

import com.sgs.server.Objects.UserType;

public record JwtPrincipal(
        long userId,
        UserType userType
) {}