package com.school.model;


import com.school.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoginResponse {
    private Long id;
    private String email;
    private String name;
    private Role role;
    private String jwtToken;
}
