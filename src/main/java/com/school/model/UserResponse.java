package com.school.model;

import com.school.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse implements Serializable{

    private Long id;
    private String email;
    private String name;
    private Role role; // STUDENT, TEACHER
    private Boolean status;
    private String subject;
    private String studentClass;
    private String section;
    private String address;
}
