package com.mycompany.hrms.service.users;

import com.mycompany.hrms.service.dtos.users.request.UpdateUserProfileDto;
import com.mycompany.hrms.service.dtos.users.request.UpdateUserProfileHr;
import com.mycompany.hrms.service.dtos.users.request.UserProfileCreate;
import com.mycompany.hrms.service.dtos.users.response.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserProfileDto getUserProfileById(long userId);

    UserProfileDto getUserProfileByEmail(String email);

    UserProfileCreated createUserProfile(UserProfileCreate userProfileCreate);

    UpdatedUserProfileDto updateUserProfile(long userId, UpdateUserProfileDto updatedProfile);

    List<UserListRes> getUsersListByName(String name);

    List<EventRes> getUsersWithBirthday();

    OrgChartRes getOrgChart(long userId);

    List<OrgChartRes> getOrgChartList(long userId);

    List<OrgChartRes> getAssignedUnder(long userId);

    FavouriteGameResponse getSlotsOfFavouriteGame(long userId);

    List<UserProfileDto> getAllUserProfiles(Pageable pageable, Long department);

    void removeGameFromFavourite(long userId, long gameId);

    void makeGameFavourite(long userId, long gameId);

    void updateActiveStatus(long userId, boolean status);

    boolean isBlocked(String email);

    void updateUserProfile(long userId, UpdateUserProfileHr updatedProfile);

    UserProfileDto getUserProfileByUserId(long userId);

    List<String> getAllDesignations();

}
