package com.mycompany.hrms.service.travel;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class TravelDocumentService implements ITravelDocumentService {

    private final TravelDocumentsRepo travelDocumentsRepo;
    private final TravelingUserRepo travelingUserRepo;
    private final UsersRepo usersRepo;
    private final Path root;
    private final ModelMapper modelMapper;

    @Autowired
    public TravelDocumentService(TravelDocumentsRepo travelDocumentsRepo, TravelingUserRepo travelingUserRepo, UsersRepo usersRepo, ModelMapper modelMapper){
        this.root = Paths.get("uploads/travel/documents");
        this.travelDocumentsRepo = travelDocumentsRepo;
        this.travelingUserRepo = travelingUserRepo;
        this.usersRepo = usersRepo;
        init();
        this.modelMapper = modelMapper;
    }

    private void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new InternalServerException("Could not initialize folder for upload!");
        }
    }

    public List<TravelDocRes> getTravelDocuments(long travelingUserId){
        List<TravelDocuments> documents = travelDocumentsRepo.getTravelDocumentsByTravelingUser_TravelingUserId(travelingUserId);
        return documents.stream().map(val -> modelMapper.map(val, TravelDocRes.class)).toList();
    }

    public List<String> saveFiles(MultipartFile[] files, long uploadedBy, long travelingUser, Constants.DocType docType) {
        TravelingUser travelingUserInfo = travelingUserRepo.findById(travelingUser)
                .orElseThrow(() -> new ResourceNotFoundException("Traveling user not found"));
        Users user = usersRepo.findById(uploadedBy)
                .orElseThrow(() -> new ResourceNotFoundException("Uploaded by user not found"));

        return Arrays.stream(files).map(file -> {
            try {
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), this.root.resolve(filename));
                TravelDocuments travelDocument = new TravelDocuments();
                travelDocument.setFilePath(filename);
                travelDocument.setTravelingUser(travelingUserInfo);
                travelDocument.setDocType(docType);
                travelDocument.setUploadedBy(user);
                travelDocumentsRepo.save(travelDocument);
                return filename;
            } catch (Exception e) {
                throw new InternalServerException("Could not store the file. Error: " + e.getMessage());
            }
        }).toList();
    }

    public void deleteSavedFile(long docId) {
        TravelDocuments res = travelDocumentsRepo.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        try {
            Path filePath = root.resolve(res.getFilePath()).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new InternalServerException("Could not delete file from storage: " + e.getMessage());
        }

        travelDocumentsRepo.delete(res);
    }

}
