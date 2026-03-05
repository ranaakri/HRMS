package com.mycompany.hrms.data.dtos.travel.request;

import com.mycompany.hrms.data.constant.Constants;
import jakarta.validation.constraints.NotNull;

public class UploadTravelDocReq {

    @NotNull
    private Constants.DocType docType;

    @NotNull
    private long uploadedByUserId;

    @NotNull
    private long travelingUserId;

    public Constants.DocType getDocType() {
        return docType;
    }

    public void setDocType(Constants.DocType docType) {
        this.docType = docType;
    }

    public long getUploadedByUserId() {
        return uploadedByUserId;
    }

    public void setUploadedByUserId(long uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
    }

    public long getTravelingUserId() {
        return travelingUserId;
    }

    public void setTravelingUserId(long travelingUserId) {
        this.travelingUserId = travelingUserId;
    }
}
