package com.mycompany.hrms.service.travel;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.travel.TravelDocuments;
import com.mycompany.hrms.data.entity.travel.TravelingUser;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.travel.TravelDocumentsRepo;
import com.mycompany.hrms.data.repository.travel.TravelingUserRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.travel.response.TravelDocRes;
import com.mycompany.hrms.service.exception.InternalServerException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class TravelDocumentService implements ITravelDocumentService {

    private final TravelDocumentsRepo travelDocumentsRepo;
    private final TravelingUserRepo travelingUserRepo;
    private final UsersRepo usersRepo;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    @Autowired
    public TravelDocumentService(TravelDocumentsRepo travelDocumentsRepo, TravelingUserRepo travelingUserRepo, UsersRepo usersRepo, ModelMapper modelMapper, Cloudinary cloudinary){

        this.travelDocumentsRepo = travelDocumentsRepo;
        this.travelingUserRepo = travelingUserRepo;
        this.usersRepo = usersRepo;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }

    public List<TravelDocRes> getTravelDocuments(long userId, long travelId){
        List<TravelDocuments> documents = travelDocumentsRepo.getTravelDocsForUser(userId, travelId);
        return documents.stream().map(val -> modelMapper.map(val, TravelDocRes.class)).toList();
    }

    public List<TravelDocRes> getTravelDocumentsByTravelingUserId(long travelingUserId){
        List<TravelDocuments> documents = travelDocumentsRepo.getTravelDocumentsByTravelingUser_TravelingUserId(travelingUserId);
        return documents.stream().map(val -> modelMapper.map(val, TravelDocRes.class)).toList();
    }

    public List<TravelDocRes> saveByEmp(MultipartFile[] files, long userId, long travelId, Constants.DocType docType){
        TravelingUser travelingUser = travelingUserRepo.getTravelingUsersByUser_UserIdAndTravelDetails_TravelId(userId, travelId)
                .orElseThrow(() -> new ResourceNotFoundException("Traveling user not found"));
        return saveFiles(files, userId, travelingUser.getTravelingUserId(), docType);
    }

    public List<TravelDocRes> saveFiles(MultipartFile[] files, long uploadedBy, long travelingUser, Constants.DocType docType) {
        TravelingUser travelingUserInfo = travelingUserRepo.findById(travelingUser)
                .orElseThrow(() -> new ResourceNotFoundException("Traveling user not found"));
        Users user = usersRepo.findById(uploadedBy)
                .orElseThrow(() -> new ResourceNotFoundException("Uploaded by user not found"));

        for(var file : files ) {
            try {
                Map uploadRes = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap("folder", "travel/gallery/")
                );
                String imageUrl = uploadRes.get("secure_url").toString();
                String publicId = uploadRes.get("public_id").toString();
                TravelDocuments travelDocument = new TravelDocuments();
                travelDocument.setFilePath(imageUrl);
                travelDocument.setTravelingUser(travelingUserInfo);
                travelDocument.setDocType(docType);
                travelDocument.setPublicId(publicId);
                travelDocument.setUploadedBy(user);
                travelDocumentsRepo.save(travelDocument);
            } catch (Exception e) {
                throw new InternalServerException("Could not store the file. Error: " + e.getMessage());
            }
        }
        return travelDocumentsRepo.findTravelDocumentsByTravelingUser_TravelingUserId(travelingUser).stream().map(val -> modelMapper.map(val, TravelDocRes.class)).toList();
    }

    public void deleteSavedFile(long docId) {
        TravelDocuments res = travelDocumentsRepo.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        try {
            cloudinary.uploader().destroy(res.getPublicId(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new InternalServerException("Could not delete file from storage: " + e.getMessage());
        }

        travelDocumentsRepo.delete(res);
    }

}
