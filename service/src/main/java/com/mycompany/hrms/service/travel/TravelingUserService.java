package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.travel.TravelingUser;
import com.mycompany.hrms.data.repository.travel.ExpensesRepo;
import com.mycompany.hrms.data.repository.travel.TravelDetailsRepo;
import com.mycompany.hrms.data.repository.travel.TravelingUserRepo;
import com.mycompany.hrms.service.dtos.travel.request.AddTravelingUserReq;
import com.mycompany.hrms.service.dtos.travel.request.TravelingUserReq;
import com.mycompany.hrms.service.dtos.travel.response.TravelingUserRes;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.BusinessException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelingUserService implements ITravelingUserService{

    private final TravelingUserRepo travelingUserRepo;
    private final TravelDetailsRepo travelDetailsRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public TravelingUserService(TravelingUserRepo travelingUserRepo, TravelDetailsRepo travelDetailsRepo, ModelMapper modelMapper){
        this.travelingUserRepo = travelingUserRepo;
        this.travelDetailsRepo = travelDetailsRepo;
        this.modelMapper = modelMapper;
    }

    public List<TravelingUserRes> getTravelingUsers(long travelId){
        List<TravelingUser> users = travelingUserRepo.getTravelingUsersByTravelDetails_TravelId(travelId);
        return users.stream().map(val -> modelMapper.map(val, TravelingUserRes.class)).toList();
    }

    public void assignUserToTravel(AddTravelingUserReq travelingUsers) {
        float sum = 0;
        float assignedBudget = travelDetailsRepo.findById(travelingUsers.getTravelId())
                .orElseThrow(() -> new ResourceNotFoundException("Travel details not found")).getAssignedBudget();
        for(TravelingUserReq t : travelingUsers.getUsers()){
            sum+=t.getTravelBalance();
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