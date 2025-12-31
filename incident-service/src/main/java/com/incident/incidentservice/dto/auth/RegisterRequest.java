package com.incident.incidentservice.dto.auth;

import com.incident.incidentservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
    private String teamId;
    private String teamName;
    private String phoneNumber;
}