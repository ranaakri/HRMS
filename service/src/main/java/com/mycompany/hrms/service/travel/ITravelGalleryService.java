package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.dtos.travel.response.TravelGalleryRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITravelGalleryService {
    List<TravelGalleryRes> saveFiles(MultipartFile[] files, long travelId);
    void deleteTravelImage(long imageId);
}
