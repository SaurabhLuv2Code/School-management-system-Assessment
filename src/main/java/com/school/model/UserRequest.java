package com.school.model;

import com.school.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRequest {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;
    private Role role;
    private String subject;
    private String studentClass;
    private String section;
}

