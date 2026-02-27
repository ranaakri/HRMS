package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.service.dtos.travel.request.TravelDetailsReq;
import com.mycompany.hrms.service.dtos.travel.request.UpdateTravelDetailsReq;
import com.mycompany.hrms.service.dtos.travel.response.TravelDetailsRes;

import java.util.List;

public interface ITravelDetailsService {
    TravelDetailsRes getTravelDetailsId(long travelId);
    TravelDetailsRes addTravelDetail(TravelDetailsReq travelDetails);
    void deleteTravelDetails(long travelId);
    List<TravelDetailsRes> getTravelDetailsByUserId(long userId);
    TravelDetailsRes updateTravelDetails(long travelId, UpdateTravelDetailsReq travelDetailsReq);
    List<TravelDetailsRes> getTravels();
    TravelDetailsRes travelDetailsMapper(TravelDetails travelDetails);
}
