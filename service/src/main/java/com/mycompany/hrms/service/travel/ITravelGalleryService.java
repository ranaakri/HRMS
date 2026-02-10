package com.mycompany.hrms.service.travel;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITravelGalleryService {
    List<String> saveFiles(MultipartFile[] files, long travelId);
    void deleteTravelImage(long imageId);
}
