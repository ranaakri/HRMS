package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.dtos.travel.request.TravelDetailsReq;
import com.mycompany.hrms.data.dtos.travel.request.UpdateTravelDetailsReq;
import com.mycompany.hrms.data.dtos.travel.response.TravelDetailsRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITravelDetailsService {
    TravelDetailsRes getTravelDetailsId(long travelId);
    TravelDetailsRes addTravelDetail(TravelDetailsReq travelDetails);
    void deleteTravelDetails(long travelId);
    List<TravelDetailsRes> getTravelDetailsByUserId(long userId);
    TravelDetailsRes updateTravelDetails(long travelId, UpdateTravelDetailsReq travelDetailsReq);
    List<TravelDetailsRes> getTravels(Pageable pageable);
    TravelDetailsRes travelDetailsMapper(TravelDetails travelDetails);
}
