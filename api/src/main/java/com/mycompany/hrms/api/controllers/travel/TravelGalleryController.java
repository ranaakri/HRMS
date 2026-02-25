package com.mycompany.hrms.api.controllers.travel;

import com.mycompany.hrms.api.response.ApiResponse;
import com.mycompany.hrms.service.dtos.travel.response.TravelGalleryRes;
import com.mycompany.hrms.service.travel.ITravelGalleryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/travel/gallery")
public class TravelGalleryController {

    private final ITravelGalleryService travelGalleryService;

    @Autowired
    public TravelGalleryController(ITravelGalleryService travelGalleryService){
        this.travelGalleryService = travelGalleryService;
    }

    @Operation(
            summary = "Upload Images related to travel"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @PostMapping("/{travelId}")
    public ResponseEntity<List<TravelGalleryRes>> uploadFiles(@RequestParam("files") MultipartFile[] files, @PathVariable long travelId) {
        List<TravelGalleryRes> fileNames = travelGalleryService.saveFiles(files, travelId);
        return ResponseEntity.ok(fileNames);
    }

    @Operation(
            summary = "Remove images of travel using image id"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @DeleteMapping("/{travelId}")
    public ResponseEntity<ApiResponse<Void>> deleteTravelImage(@PathVariable long travelId){
        travelGalleryService.deleteTravelImage(travelId);
        return ResponseEntity.ok(ApiResponse.deleted("Image deleted successfully"));
    }
}
