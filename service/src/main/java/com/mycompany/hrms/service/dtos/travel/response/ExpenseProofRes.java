package com.mycompany.hrms.service.dtos.travel.response;

public class ExpenseProofRes {

    private long proofId;

    private String proofFilePath;

    public long getProofId() {
        return proofId;
    }

    public void setProofId(long proofId) {
        this.proofId = proofId;
    }

    public String getProofFilePath() {
        return proofFilePath;
    }

    public void setProofFilePath(String proofFilePath) {
        this.proofFilePath = proofFilePath;
    }
}
