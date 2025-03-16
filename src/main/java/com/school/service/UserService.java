package com.school.service;

import com.school.entity.Student;
import com.school.entity.Teacher;
import com.school.entity.User;
import com.school.enums.Role;
import com.school.exception.BadReqException;
import com.school.exception.UserNotFoundException;
import com.school.model.LoginRequest;
import com.school.model.LoginResponse;
import com.school.model.UserRequest;
import com.school.model.UserResponse;
import com.school.repository.StudentRepository;
import com.school.repository.TeacherRepository;
import com.school.repository.UserRepository;
import com.school.config.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final JwtHelper helper;

    @Autowired
    public UserService(UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, JwtHelper helper) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.helper = helper;
    }

//    ================= Scheduler =======================

    @Scheduled(cron = "0 15 17 * * ?")  // Cron expression for every day at 5:15 PM
    public void sendAssessmentReminder() {
        System.out.println("Scheduler is working..............");
    }

//    =================== Add User ======================

    public UserResponse registerUser(UserRequest userRequest) {
        log.info("START: Adding new user");

        User user = User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .name(userRequest.getName())
                .role(userRequest.getRole())
                .status(true)
                .address(userRequest.getAddress())
                .build();

        // Save the User entity first
        User savedUser = userRepository.save(user);

        // Handle role-specific entity creation
        if (userRequest.getRole() == Role.STUDENT) {
            Student student = new Student();
            student.setUser(savedUser);
            student.setStudentClass(userRequest.getStudentClass());
            student.setSection(userRequest.getSection());
            studentRepository.save(student);
        } else if (userRequest.getRole() == Role.TEACHER) {
            Teacher teacher = new Teacher();
            teacher.setUser(savedUser);
            teacher.setSubject(userRequest.getSubject());
            teacherRepository.save(teacher);
        }

        return mapToUserResponse(savedUser, userRequest);
    }

//    ---------------- Map to user response

    private UserResponse mapToUserResponse(User savedUser, UserRequest userRequest) {
        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .status(savedUser.getStatus())
                .role(savedUser.getRole())
                .name(savedUser.getName())
                .studentClass(userRequest.getStudentClass())
                .section(userRequest.getSection())
                .subject(userRequest.getSubject())
                .address(savedUser.getAddress())
                .build();
    }

//    ================ Get User By ID ===============

    public UserResponse getUser(Long id) {
        log.info("Get user details ID: {}", id);
        return userRepository.findUserResponseById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

//    =================== Get User List ===============

    public List<UserResponse> getUserList(Role role) {
        log.info("Get user list role: {}", role);
        return userRepository.findUserResponseByRole(role);
    }

//    ================== Delete User =============

    public String deleteUser(Long id) {
        log.info("Deleting user ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        if (!user.getStatus()){
            return "User already deleted";
        }
        user.setStatus(false);
        userRepository.save(user);
        return "User deleted successfully";
    }

//    ================ Update User API ===================

    public String updateUser(UserRequest userRequest) {
        log.info("Updating user details ID :{}", userRequest.getId());
        User user = userRepository.findById(userRequest.getId())
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        if (isNotBlank(userRequest.getName()))
            user.setName(userRequest.getName());
        if (isNotBlank(userRequest.getName()))
            user.setAddress(userRequest.getAddress());
        if (user.getRole().equals(Role.STUDENT)){
            Student student = studentRepository.findByUserId(user.getId());
            if (isNotBlank(userRequest.getSection()))
                student.setSection(userRequest.getSection());
            if (isNotBlank(userRequest.getStudentClass()))
                student.setStudentClass(userRequest.getStudentClass());

        } else {
            Teacher teacher = teacherRepository.findByUserId(user.getId());
            if (isNotBlank(userRequest.getSubject()))
                teacher.setSubject(userRequest.getSubject());
        }
        userRepository.save(user);
        log.info("User details updated successfully");
        return "User details updated successfully";
    }

//    ============= User Login =======================

    public LoginResponse login(LoginRequest loginRequest, Role role) {
        log.info("Login user email : {}", loginRequest.getEmail());
        User user = userRepository.findByEmailAndPasswordAndRoleAndStatusIsTrue(loginRequest.getEmail(), loginRequest.getPassword(), role);
        if (user == null) throw new BadReqException("Invalid credentials");

        final UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), new BCryptPasswordEncoder().encode(user.getPassword()), new ArrayList<>());
        String token = helper.generateToken(userDetails);
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .jwtToken(token)
                .build();
    }
}