package com.sgs.server.Objects;

import lombok.Data;

@Data
public class LoginDTO {
    private Long id;
    private String password;
    private UserType userType;
}
