package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.service.dtos.travel.response.TravelDocRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITravelDocumentService {
    List<String> saveFiles(MultipartFile[] files, long uploadedBy, long travelingUser, Constants.DocType docType);
    void deleteSavedFile(long docId);
    List<TravelDocRes> getTravelDocuments(long travelingUserId);
}
