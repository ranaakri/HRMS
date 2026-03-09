package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.travel.TravelDetailsRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.data.dtos.travel.request.TravelDetailsReq;
import com.mycompany.hrms.data.dtos.travel.request.UpdateTravelDetailsReq;
import com.mycompany.hrms.data.dtos.travel.response.CreatedByUser;
import com.mycompany.hrms.data.dtos.travel.response.TravelDetailsRes;
import com.mycompany.hrms.data.dtos.travel.response.TravelGalleryRes;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.ForbiddenException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class TravelDetailService implements ITravelDetailsService{

    private final ModelMapper modelMapper;
    private final TravelDetailsRepo travelDetailsRepo;
    private final UsersRepo usersRepo;

    @Autowired
    public TravelDetailService(TravelDetailsRepo travelDetailsRepo,UsersRepo usersRepo, ModelMapper modelMapper){
        this.travelDetailsRepo = travelDetailsRepo;
        this.usersRepo = usersRepo;
        this.modelMapper = modelMapper;
    }

    public TravelDetailsRes getTravelDetailsId(long travelId){
        return travelDetailsMapper(travelDetailsRepo.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel details not found")));
    }

    public List<TravelDetailsRes> searchTravelDetails(String title, Pageable pageable){
        Page<TravelDetails> travelDetails = travelDetailsRepo.findAllByTitleContainingIgnoreCase(title, pageable);
        return  travelDetails.stream().map(this::travelDetailsMapper).toList();
    }

    public List<TravelDetailsRes> getTravelDetailsByUserId(long userId) {
        List<TravelDetails> travelDetails = travelDetailsRepo.findAllByCreatedBy_UserId(userId);
        return travelDetails.stream().map(this::travelDetailsMapper).toList();
    }

    public List<TravelDetailsRes> getTravels(Pageable pageable){
        Page<TravelDetails> travelDetails = travelDetailsRepo.findAll(pageable);
        return travelDetails.stream().map(this::travelDetailsMapper).toList();
    }

    public TravelDetailsRes travelDetailsMapper(TravelDetails travelDetails){
        TravelDetailsRes res = modelMapper.map(travelDetails, TravelDetailsRes.class);
        if(travelDetails.getTravelGallery() != null && !travelDetails.getTravelGallery().isEmpty())
            res.setTravelGallery(travelDetails.getTravelGallery().stream().map(val -> modelMapper.map(val, TravelGalleryRes.class)).toList());
        else
            res.setTravelGallery(Collections.emptyList());
        res.setCreatedByUser(modelMapper.map(travelDetails.getCreatedBy(), CreatedByUser.class));
        return res;
    }

    public TravelDetailsRes addTravelDetail(TravelDetailsReq travelDetails){
        TravelDetails details = modelMapper.map(travelDetails, TravelDetails.class);
        Users user = usersRepo.findById(travelDetails.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for created by"));
        details.setCreatedBy(user);
        if(travelDetails.getStartDate().isBefore(ZonedDateTime.now()))
            throw new BadRequestException("Invalid starting date");
        if(travelDetails.getTotalExpense() > travelDetails.getAssignedBudget())
            throw new BadRequestException("Total expense should not be grater then assigned budget");
        return travelDetailsMapper(travelDetailsRepo.save(details));
    }

    @Transactional
    public TravelDetailsRes updateTravelDetails(long travelId, UpdateTravelDetailsReq travelDetailsReq){
        TravelDetails details = travelDetailsRepo.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel details not found"));
        if(details.getCreatedBy().getUserId() != travelDetailsReq.getUpdatedBy())
            throw new ForbiddenException("Unauthorized Action, Can not update travel details");

        if(travelDetailsReq.getTotalExpense() > travelDetailsReq.getAssignedBudget())
            throw new BadRequestException("Total expense should not be grater then assigned budget");
        modelMapper.map(travelDetailsReq, details);
        return modelMapper.map(travelDetailsRepo.save(details), TravelDetailsRes.class);
    }

    public void deleteTravelDetails(long travelId){
        if(!travelDetailsRepo.existsById(travelId))
            throw new ResourceNotFoundException("Travel Details not found");

        travelDetailsRepo.deleteById(travelId);
    }
}
