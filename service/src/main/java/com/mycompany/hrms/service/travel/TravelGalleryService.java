package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.travel.TravelGallery;
import com.mycompany.hrms.data.repository.travel.TravelDetailsRepo;
import com.mycompany.hrms.data.repository.travel.TravelGalleryRepo;
import com.mycompany.hrms.service.dtos.travel.response.TravelGalleryRes;
import com.mycompany.hrms.service.exception.InternalServerException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class TravelGalleryService implements ITravelGalleryService {

    private final TravelGalleryRepo travelGalleryRepo;
    private final TravelDetailsRepo travelDetailsRepo;
    private final Path root;
    private final ModelMapper modelMapper;

    @Autowired
    public TravelGalleryService(TravelGalleryRepo travelGalleryRepo, TravelDetailsRepo travelDetailsRepo, ModelMapper modelMapper){
        this.root = Paths.get(
                System.getProperty("user.dir"),  "../../","uploads","travel", "gallary"
        );
        this.travelGalleryRepo = travelGalleryRepo;
        this.travelDetailsRepo = travelDetailsRepo;
        this.modelMapper = modelMapper;
        init();
    }

    private void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new InternalServerException("Could not initialize folder for upload!");
        }
    }

    public List<TravelGalleryRes> saveFiles(MultipartFile[] files, long travelId) {
        TravelDetails travelDetails = travelDetailsRepo.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel details not found"));
        return Arrays.stream(files).map(file -> {
            try {
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), this.root.resolve(filename));
                TravelGallery travelGallery = new TravelGallery();
                travelGallery.setFilePath(filename);
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
            Path filePath = root.resolve(res.getFilePath()).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new InternalServerException("Could not delete file from storage: " + e.getMessage());
        }

        travelGalleryRepo.delete(res);
    }

}