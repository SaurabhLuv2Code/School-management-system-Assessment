package com.school.repository;

import com.school.entity.User;
import com.school.enums.Role;
import com.school.model.LoginRequest;
import com.school.model.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT new com.school.model.UserResponse( " +
            "u.id, u.email, u.name, u.role, u.status, " +
            "COALESCE(t.subject, null), " +  // Ensures NULL values are handled
            "COALESCE(s.studentClass, null), " +
            "COALESCE(s.section, null), u.address) " +
            "FROM User u " +
            "LEFT JOIN Student s ON u.id = s.user.id " +
            "LEFT JOIN Teacher t ON u.id = t.user.id " +
            "WHERE u.id = :userId AND u.status=true")
    Optional<UserResponse> findUserResponseById(@Param("userId") Long userId);

    @Query("SELECT new com.school.model.UserResponse( " +
            "u.id, u.email, u.name, u.role, u.status, " +
            "COALESCE(t.subject, null), " +  // Ensures NULL values are handled
            "COALESCE(s.studentClass, null), " +
            "COALESCE(s.section, null), u.address) " +
            "FROM User u " +
            "LEFT JOIN Student s ON u.id = s.user.id " +
            "LEFT JOIN Teacher t ON u.id = t.user.id " +
            "WHERE u.role = :role AND u.status=true")
    List<UserResponse> findUserResponseByRole(@Param("role") Role role);

    User findByEmailAndPasswordAndRoleAndStatusIsTrue(String email, String password, Role role);

    User findByEmail(String username);
}
