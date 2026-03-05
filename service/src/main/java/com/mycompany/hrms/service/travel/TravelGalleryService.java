package com.mycompany.hrms.service.travel;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.travel.TravelGallery;
import com.mycompany.hrms.data.repository.travel.TravelDetailsRepo;
import com.mycompany.hrms.data.repository.travel.TravelGalleryRepo;
import com.mycompany.hrms.data.dtos.travel.response.TravelGalleryRes;
import com.mycompany.hrms.service.exception.InternalServerException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class TravelGalleryService implements ITravelGalleryService {

    private final TravelGalleryRepo travelGalleryRepo;
    private final TravelDetailsRepo travelDetailsRepo;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    @Autowired
    public TravelGalleryService(TravelGalleryRepo travelGalleryRepo, TravelDetailsRepo travelDetailsRepo, ModelMapper modelMapper, Cloudinary cloudinary){
        this.travelGalleryRepo = travelGalleryRepo;
        this.travelDetailsRepo = travelDetailsRepo;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }

    public List<TravelGalleryRes> saveFiles(MultipartFile[] files, long travelId) {
        TravelDetails travelDetails = travelDetailsRepo.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel details not found"));
        return Arrays.stream(files).map(file -> {
            try {
                Map uploadRes = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap("folder", "travel/gallery/")
                );
                String imageUrl = uploadRes.get("secure_url").toString();
                String publicId = uploadRes.get("public_id").toString();
                TravelGallery travelGallery = new TravelGallery();
                travelGallery.setFilePath(imageUrl);
                travelGallery.setPublicId(publicId);
                travelGallery.setTravelDetails(travelDetails);
                travelGalleryRepo.save(travelGallery);
                return modelMapper.map(travelGallery, TravelGalleryRes.class);
            } catch (Exception e) {
                throw new InternalServerException("Could not store the file. Error: " + e.getMessage());
            }
        }).toList();
    }

    public void deleteTravelImage(long imageId) {
        TravelGallery res = travelGalleryRepo.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        try {
            cloudinary.uploader().destroy(res.getPublicId(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new InternalServerException("Could not delete file from storage: " + e.getMessage());
        }

        travelGalleryRepo.delete(res);
    }
}