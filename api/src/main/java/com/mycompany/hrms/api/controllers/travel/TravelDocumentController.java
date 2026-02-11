package com.mycompany.hrms.api.controllers.travel;

import com.mycompany.hrms.api.response.ApiResponse;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.service.dtos.travel.response.TravelDocRes;
import com.mycompany.hrms.service.travel.ITravelDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/travel/documents")
public class TravelDocumentController {

    private final ITravelDocumentService travelDocumentService;

    @Autowired
    private TravelDocumentController(ITravelDocumentService travelDocumentService){
        this.travelDocumentService = travelDocumentService;
    }

    @GetMapping("/{travelingUserId}")
    public ResponseEntity<ApiResponse<List<TravelDocRes>>> getTravelDocByTravelingUserId(@PathVariable long travelingUserId){
        return ResponseEntity.ok(
                ApiResponse.success(travelDocumentService.getTravelDocuments(travelingUserId),
                        "Documents fetched successfully"));
    }

    @Operation(
            summary = "Upload travel related documents"
    )
    @PostMapping(value = "/{uploadedBy}/{travelingUser}/{docType}")
    public ResponseEntity<ApiResponse<List<String>>> uploadTravelDocuments(@RequestPart("file") MultipartFile[] files,
                          @PathVariable long uploadedBy,
                          @PathVariable long travelingUser,
                          @PathVariable Constants.DocType docType){
        return ResponseEntity.ok(
                ApiResponse.success(travelDocumentService.saveFiles(files, uploadedBy, travelingUser, docType),
        "Travel documents uploaded successfully"));
    }

    @Operation(
            summary = "Remove Uploaded document"
    )
    @DeleteMapping("/{docId}")
    public ResponseEntity<ApiResponse<String>> deleteTravelDoc(@PathVariable long docId){
        travelDocumentService.deleteSavedFile(docId);
        return ResponseEntity.ok(ApiResponse.successMsg("Travel document deleted successfully"));
    }
}
