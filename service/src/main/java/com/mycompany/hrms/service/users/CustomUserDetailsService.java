package com.mycompany.hrms.service.users;

import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.exception.IErrorMessages;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    public final UsersRepo usersRepo;

    @Autowired
    public CustomUserDetailsService(UsersRepo usersRepo){
        this.usersRepo = usersRepo;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email){
        Users user = usersRepo.findUsersByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));

        Hibernate.initialize(user.getRole());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().getName())
                .build();
    }
}
