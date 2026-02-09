package com.mycompany.hrms.service.users;

import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.users.DepartmentsRepo;
import com.mycompany.hrms.data.repository.users.RolesRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.users.request.UpdateUserProfileDto;
import com.mycompany.hrms.service.dtos.users.request.UserProfileCreate;
import com.mycompany.hrms.service.dtos.users.response.UpdatedUserProfileDto;
import com.mycompany.hrms.service.dtos.users.response.UserProfileCreated;
import com.mycompany.hrms.service.dtos.users.response.UserProfileDto;
import com.mycompany.hrms.service.exception.IErrorMessages;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements IUserService {

    private final UsersRepo usersRepo;
    private final ModelMapper modelMapper;
    private final RolesRepo rolesRepo;
    private final DepartmentsRepo departmentsRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(
            UsersRepo usersRepo,
            ModelMapper modelMapper,
            RolesRepo rolesRepo,
            DepartmentsRepo departmentsRepo,
            PasswordEncoder passwordEncoder
    ){
        this.usersRepo = usersRepo;
        this.modelMapper = modelMapper;
        this.rolesRepo = rolesRepo;
        this.departmentsRepo = departmentsRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfileDto getUserProfileById(long userId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        System.out.println(user.getName());
        return modelMapper.map(user, UserProfileDto.class);
    }

    public UserProfileDto getUserProfileByEmail(String email){
        return modelMapper.map(usersRepo.findUsersByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND)), UserProfileDto.class);
    }

    @Transactional
    public UserProfileCreated createUserProfile(UserProfileCreate userProfileCreate){
        Users users = modelMapper.map(userProfileCreate, Users.class);
        users.setRole(rolesRepo.findById(userProfileCreate.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found in database")));
        users.setDepartment(departmentsRepo.findById(userProfileCreate.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found in database")));
        users.setPassword(passwordEncoder.encode(userProfileCreate.getPassword()));
        return modelMapper.map(usersRepo.save(users), UserProfileCreated.class);
    }

    @Transactional
    public UpdatedUserProfileDto updateUserProfile(long userId, UpdateUserProfileDto updatedProfile){
        Users profile = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(IErrorMessages.USER_NOT_FOUND));
        modelMapper.map(updatedProfile, profile);
        return  modelMapper.map(profile, UpdatedUserProfileDto.class);
    }
}
