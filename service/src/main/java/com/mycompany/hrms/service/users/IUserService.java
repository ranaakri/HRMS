package com.mycompany.hrms.service.users;

import com.mycompany.hrms.service.dtos.users.request.UpdateUserProfileDto;
import com.mycompany.hrms.service.dtos.users.request.UserProfileCreate;
import com.mycompany.hrms.service.dtos.users.response.UpdatedUserProfileDto;
import com.mycompany.hrms.service.dtos.users.response.UserProfileCreated;
import com.mycompany.hrms.service.dtos.users.response.UserProfileDto;

public interface IUserService {
    UserProfileDto getUserProfileById(long userId);

    UserProfileDto getUserProfileByEmail(String email);

    UserProfileCreated createUserProfile(UserProfileCreate userProfileCreate);

    UpdatedUserProfileDto updateUserProfile(long userId, UpdateUserProfileDto updatedProfile);
}
