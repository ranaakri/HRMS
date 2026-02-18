package com.mycompany.hrms.service.users;

import com.mycompany.hrms.service.dtos.users.request.UpdateUserProfileDto;
import com.mycompany.hrms.service.dtos.users.request.UserProfileCreate;
import com.mycompany.hrms.service.dtos.users.response.*;

import java.util.List;

public interface IUserService {
    UserProfileDto getUserProfileById(long userId);

    UserProfileDto getUserProfileByEmail(String email);

    UserProfileCreated createUserProfile(UserProfileCreate userProfileCreate);

    UpdatedUserProfileDto updateUserProfile(long userId, UpdateUserProfileDto updatedProfile);

    List<UserListRes> getUsersListByName(String name);

    List<EventRes> getUsersWithBirthday();
    OrgChartRes getOrgChart(long userId);
}
