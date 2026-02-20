package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.entity.travel.Expenses;
import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.travel.TravelingUser;
import com.mycompany.hrms.data.repository.travel.TravelDetailsRepo;
import com.mycompany.hrms.data.repository.travel.TravelingUserRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.travel.request.AddTravelingUserReq;
import com.mycompany.hrms.service.dtos.travel.request.TravelingUserReq;
import com.mycompany.hrms.service.dtos.travel.response.BudgetResponse;
import com.mycompany.hrms.service.dtos.travel.response.TravelDetailsRes;
import com.mycompany.hrms.service.dtos.travel.response.TravelingUserRes;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.BusinessException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import com.mycompany.hrms.service.notification.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelingUserService implements ITravelingUserService{

    private final TravelingUserRepo travelingUserRepo;
    private final TravelDetailsRepo travelDetailsRepo;
    private final ModelMapper modelMapper;
    private final UsersRepo usersRepo;
    private final NotificationService notificationService;

    @Autowired
    public TravelingUserService(TravelingUserRepo travelingUserRepo, TravelDetailsRepo travelDetailsRepo, ModelMapper modelMapper, UsersRepo usersRepo, NotificationService notificationService){
        this.travelingUserRepo = travelingUserRepo;
        this.travelDetailsRepo = travelDetailsRepo;
        this.modelMapper = modelMapper;
        this.usersRepo = usersRepo;
        this.notificationService = notificationService;
    }

    public List<TravelDetailsRes> getTravelPlansByForUser(long userId){
        List<TravelingUser> travelingUsers = travelingUserRepo.getTravelingUsersByUser_UserId(userId);
        return travelingUsers.stream().map(val -> modelMapper.map(val.getTravelDetails(), TravelDetailsRes.class)).toList();
    }

    public List<TravelingUserRes> getTravelingUsersUnderManager(long travelId, long managerId){
        if(!usersRepo.existsById(managerId))
            throw new ResourceNotFoundException("Manger does not exist");
        List<TravelingUser> users = travelingUserRepo.getTravelingUsersAssignUnder(managerId, travelId);
        return users.stream().map(val -> modelMapper.map(val, TravelingUserRes.class)).toList();
    }

    public List<TravelingUserRes> getTravelingUsers(long travelId){
        List<TravelingUser> users = travelingUserRepo.getTravelingUsersByTravelDetails_TravelId(travelId);
        return users.stream().map(val -> modelMapper.map(val, TravelingUserRes.class)).toList();
    }

    public BudgetResponse getBudgetAndBalance(long userId, long travelId){
        TravelingUser travelingUser = travelingUserRepo.getTravelingUsersByUser_UserIdAndTravelDetails_TravelId(userId, travelId)
                .orElseThrow(() -> new ResourceNotFoundException("Traveling user not found"));
        return modelMapper.map(travelingUser, BudgetResponse.class);
    }

    public void assignUserToTravel(AddTravelingUserReq travelingUsers) {
        float sum = 0;
        float assignedBudget = travelDetailsRepo.findById(travelingUsers.getTravelId())
                .orElseThrow(() -> new ResourceNotFoundException("Travel details not found")).getAssignedBudget();
        for(TravelingUserReq t : travelingUsers.getUsers()){
            sum+=t.getTravelBalance();
            if(travelingUserRepo.existsByUser_UserIdAndTravelDetails_TravelId(t.getUserId(), travelingUsers.getTravelId())){
                throw new BadRequestException("One of the user is already assigned to travel");
            }
        }
        if(sum > assignedBudget)
            throw new BusinessException("Invalid balance assigned");

        try {
            for(TravelingUserReq t : travelingUsers.getUsers()){
                travelingUserRepo.sp_I_travelingUser(t.getUserId(), travelingUsers.getTravelId(), t.getTravelBalance());
            }
        } catch (Exception e) {
            throw new BusinessException("Insufficient budget: Assignment exceeds total travel budget.");
        }
        List<Long> userIds = travelingUsers.getUsers().stream().map(val -> val.getUserId()).toList();
        notificationService.addNotification(userIds, "ADDED_IN_TRAVEL", "");
    }

    public void updateAssignedBudget(long travelingUserId, float travelBalance){
        try {
            travelingUserRepo.sp_U_travelingUser(travelingUserId, travelBalance);
        } catch (Exception e) {
            throw new BusinessException("Insufficient budget: Assignment exceeds total travel budget.");
        }
    }

    public void deleteUserFromTravel(long travelingUserId){
        if(!travelingUserRepo.existsById(travelingUserId))
            throw new ResourceNotFoundException("Traveling user not found");
        travelingUserRepo.deleteById(travelingUserId);
    }
}