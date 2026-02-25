package com.mycompany.hrms.api.controllers.doc;

import com.mycompany.hrms.service.doc.IDocumentService;
import com.mycompany.hrms.service.dtos.DocRequest;
import com.mycompany.hrms.service.dtos.DocResponse;
import com.mycompany.hrms.service.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/doc")
public class DocController {

    private final IDocumentService documentService;

    @Autowired
    public DocController(IDocumentService documentService){
        this.documentService = documentService;
    }

    @Operation(
            summary = "upload document"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocResponse> uploadCv(@RequestParam MultipartFile doc, @RequestParam String uploadFor){
        String s = switch (uploadFor) {
            case "Cv" -> "job/referral";
            case "Jd" -> "job/jd";
            case "Proof" -> "travel/proof";
            case "Gallery" -> "travel/gallery";
            case "Post" -> "post";
            case "Profile" -> "Profile";
            default -> throw new BadRequestException("Invalid reason");
        };
        return ResponseEntity.ok(documentService.uploadDoc(doc, s));
    }

    @Operation(
            summary = "Upload multiple Documents"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PostMapping(value = "/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<DocResponse>> uploadMultiple(@RequestParam MultipartFile[] files,  @RequestParam String uploadFor){
        String s = switch (uploadFor) {
            case "Cv" -> "job/referral";
            case "Jd" -> "job/jd";
            case "Proof" -> "travel/proof";
            case "Gallery" -> "travel/gallery";
            case "Profile" -> "Profile";
            default -> throw new BadRequestException("Invalid reason");
        };
        return ResponseEntity.ok(documentService.uploadMultipleFiles(files, s));
    }

    @Operation(
            summary = "delete document"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @DeleteMapping("")
    public ResponseEntity<Void> deleteFile(@RequestBody DocRequest req){
        documentService.deleteDoc(req.getPublicId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "delete multiple files"
    )
    @DeleteMapping("/multiple")
    public ResponseEntity<Void> deleteFile(@RequestBody List<DocRequest> list){
        documentService.deleteMultiple(list);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
