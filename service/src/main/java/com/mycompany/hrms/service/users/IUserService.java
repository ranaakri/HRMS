package com.mycompany.hrms.service.users;

import com.mycompany.hrms.data.dtos.users.request.UpdateUserProfileDto;
import com.mycompany.hrms.data.dtos.users.request.UpdateUserProfileHr;
import com.mycompany.hrms.data.dtos.users.request.UserProfileCreate;
import com.mycompany.hrms.data.dtos.users.response.EventRes;
import com.mycompany.hrms.data.dtos.users.response.OrgChartRes;
import com.mycompany.hrms.data.dtos.users.response.UserListRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {

    com.mycompany.hrms.data.dtos.users.response.UserProfileDto getUserProfileByEmail(String email);

    com.mycompany.hrms.data.dtos.users.response.UserProfileCreated createUserProfile(UserProfileCreate userProfileCreate);

    com.mycompany.hrms.data.dtos.users.response.UpdatedUserProfileDto updateUserProfile(long userId, UpdateUserProfileDto updatedProfile);

    List<UserListRes> getUsersListByName(String name);

    List<EventRes> getUsersWithBirthday();

    OrgChartRes getOrgChart(long userId);

    List<OrgChartRes> getOrgChartList(long userId);

    List<OrgChartRes> getAssignedUnder(long userId);

    com.mycompany.hrms.data.dtos.users.response.FavouriteGameResponse getSlotsOfFavouriteGame(long userId);

    List<com.mycompany.hrms.data.dtos.users.response.UserProfileDto> getAllUserProfiles(Pageable pageable, Long department);

    void removeGameFromFavourite(long userId, long gameId);

    void makeGameFavourite(long userId, long gameId);

    void updateActiveStatus(long userId, boolean status);

    boolean isBlocked(String email);

    void updateUserProfile(long userId, UpdateUserProfileHr updatedProfile);

    com.mycompany.hrms.data.dtos.users.response.UserProfileDto getUserProfileByUserId(long userId);

    List<String> getAllDesignations();

    void deleteUser(long userId);

    List<com.mycompany.hrms.data.dtos.users.response.UserProfileDto> getUsersProfileByName(String name, Long departmentId);
}
