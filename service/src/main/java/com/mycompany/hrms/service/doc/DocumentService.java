package com.mycompany.hrms.service.doc;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mycompany.hrms.data.dtos.DocRequest;
import com.mycompany.hrms.data.dtos.DocResponse;
import com.mycompany.hrms.service.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService implements IDocumentService {

    private final Cloudinary cloudinary;

    @Autowired
    public DocumentService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }
    public DocResponse uploadDoc(MultipartFile cv, String folder){
        try{
            Map uploadRes = cloudinary.uploader().upload(
                    cv.getBytes(),
                    ObjectUtils.asMap("folder", folder)
            );
            String url = uploadRes.get("secure_url").toString();
            String publicId = uploadRes.get("public_id").toString();
            DocResponse res = new DocResponse();
            res.setPath(url);
            res.setPublicId(publicId);
            return res;
        }catch (Exception e) {
            throw new InternalServerException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public List<DocResponse> uploadMultipleFiles(MultipartFile[] files, String folder){
        List<DocResponse> responses = new ArrayList<>();
        try{
            for(MultipartFile file : files)
                responses.add(uploadDoc(file, folder));
            return responses;
        }
        catch (Exception e) {
            throw new InternalServerException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void deleteMultiple(List<DocRequest> publicIds){
        try{
            for(DocRequest p : publicIds)
                deleteDoc(p.getPublicId());
        }catch (Exception e) {
            throw new InternalServerException("Could not delete file from storage: " + e.getMessage());
        }
    }

    public void deleteDoc(String publicId){
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new InternalServerException("Could not delete file from storage: " + e.getMessage());
        }
    }
}
