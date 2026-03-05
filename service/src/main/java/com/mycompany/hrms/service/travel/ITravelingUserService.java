package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.dtos.travel.request.AddTravelingUserReq;
import com.mycompany.hrms.data.dtos.travel.response.BudgetResponse;
import com.mycompany.hrms.data.dtos.travel.response.TravelDetailsRes;
import com.mycompany.hrms.data.dtos.travel.response.TravelingUserRes;

import java.util.List;

public interface ITravelingUserService {
    void assignUserToTravel(AddTravelingUserReq travelingUsers);
    List<TravelingUserRes> getTravelingUsers(long travelId);
    void updateAssignedBudget(long travelingUserId, float travelBalance);
    void deleteUserFromTravel(long travelingUserId);
    BudgetResponse getBudgetAndBalance(long userId, long travelId);
    List<TravelDetailsRes> getTravelPlansByForUser(long userId);
    List<TravelingUserRes> getTravelingUsersUnderManager(long travelId, long managerId);
    TravelDetailsRes getNearestTravelPlan(long userId);
}
