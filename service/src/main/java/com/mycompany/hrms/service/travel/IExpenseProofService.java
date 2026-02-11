package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.service.dtos.travel.response.ExpenseProofRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IExpenseProofService {
    List<ExpenseProofRes> getExpenseProofs(long expenseId);
    List<String> saveFiles(MultipartFile[] files, long uploadedBy, long expenseId);
    void deleteSavedFile(long proofId);
}
