package com.school.config;

import com.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("Email from load by user " + username);


        com.school.entity.User user = userRepository.findByEmail(username);


        if(user!=null) {
            if (user.getEmail() != null) {
                return new User(username, new BCryptPasswordEncoder().encode(user.getPassword()),
                        new ArrayList<>());
            }
        }

        return new User(username, new BCryptPasswordEncoder().encode(user.getPassword()),
                new ArrayList<>());

    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

}