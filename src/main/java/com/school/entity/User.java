package com.school.entity;

import com.school.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role; // STUDENT, TEACHER
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date modifiedDate;
    private Boolean status;
    private String address;
}

