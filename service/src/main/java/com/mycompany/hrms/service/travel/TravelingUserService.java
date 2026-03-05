package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.travel.TravelingUser;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.travel.TravelDetailsRepo;
import com.mycompany.hrms.data.repository.travel.TravelingUserRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.data.dtos.travel.request.AddTravelingUserReq;
import com.mycompany.hrms.data.dtos.travel.request.TravelingUserReq;
import com.mycompany.hrms.data.dtos.travel.response.BudgetResponse;
import com.mycompany.hrms.data.dtos.travel.response.TravelDetailsRes;
import com.mycompany.hrms.data.dtos.travel.response.TravelingUserRes;
import com.mycompany.hrms.service.email.EmailService;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.BusinessException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import com.mycompany.hrms.service.notification.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelingUserService implements ITravelingUserService{

    private final TravelingUserRepo travelingUserRepo;
    private final TravelDetailsRepo travelDetailsRepo;
    private final ModelMapper modelMapper;
    private final UsersRepo usersRepo;
    private final NotificationService notificationService;
    private final ITravelDetailsService travelDetailsService;
    private final EmailService emailService;

    @Autowired
    public TravelingUserService(TravelingUserRepo travelingUserRepo,
                                TravelDetailsRepo travelDetailsRepo,
                                ModelMapper modelMapper,
                                UsersRepo usersRepo,
                                NotificationService notificationService,
                                ITravelDetailsService travelDetailsService,
                                EmailService emailService){
        this.travelingUserRepo = travelingUserRepo;
        this.travelDetailsRepo = travelDetailsRepo;
        this.modelMapper = modelMapper;
        this.usersRepo = usersRepo;
        this.notificationService = notificationService;
        this.travelDetailsService = travelDetailsService;
        this.emailService = emailService;
    }

    public List<TravelDetailsRes> getTravelPlansByForUser(long userId){
        List<TravelingUser> travelingUsers = travelingUserRepo.getTravelingUsersByUser_UserId(userId);
        return travelingUsers.stream().map(val -> modelMapper.map(val.getTravelDetails(), TravelDetailsRes.class)).toList();
    }

    public TravelDetailsRes getNearestTravelPlan(long userId){
        List<TravelDetails> travelDetails = travelDetailsRepo.findFirstNearestTravel(userId, PageRequest.of(0,1));
        TravelDetails details = travelDetails.stream().findFirst().orElse(null);
        if(details == null)
            return null;
        return travelDetailsService.travelDetailsMapper(details);
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
        List<Long> userIds = travelingUsers.getUsers().stream().map(TravelingUserReq::getUserId).toList();
        List<Users> usersList = usersRepo.findAllById(userIds);
        notificationService.addNotification(usersList, "ADDED_IN_TRAVEL", "");
        emailService.sendAddedInTravelPlanEmail(usersList);
    }

    public void updateAssignedBudget(long travelingUserId, float travelBalance){
        try {
            travelingUserRepo.sp_U_travelingUser(travelingUserId, travelBalance);
        } catch (Exception e) {
            throw new BusinessException("Insufficient budget: Assignment exceeds total travel budget.");
        }
    }

    public void deleteUserFromTravel(long travelingUserId){
        TravelingUser travelingUser = travelingUserRepo.findById(travelingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Traveling user not found"));
        if(travelingUser.getUsedBalance() > 0)
            throw new BadRequestException("User has used some of provided balance now can not be removed");
        travelingUserRepo.deleteById(travelingUserId);
    }
}