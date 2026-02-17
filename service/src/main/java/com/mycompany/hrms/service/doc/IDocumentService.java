package com.mycompany.hrms.service.doc;

import com.mycompany.hrms.service.dtos.DocRequest;
import com.mycompany.hrms.service.dtos.DocResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentService {
    DocResponse uploadDoc(MultipartFile cv, String folder);
    void deleteDoc(String publicId);
    List<DocResponse> uploadMultipleFiles(MultipartFile[] files, String folder);
    void deleteMultiple(List<DocRequest> publicIds);
}
