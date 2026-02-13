package com.mycompany.hrms.api.controllers.travel;

import com.mycompany.hrms.api.response.ApiResponse;
import com.mycompany.hrms.service.dtos.travel.response.ExpenseProofRes;
import com.mycompany.hrms.service.travel.IExpenseProofService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/travel/proofs/")
public class ExpenseProofsController {
    private final IExpenseProofService expenseProofService;

    @Autowired
    private ExpenseProofsController(IExpenseProofService expenseProofService){
        this.expenseProofService = expenseProofService;
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<List<ExpenseProofRes>> getTravelDocByTravelingUserId(@PathVariable long expenseId){
        return ResponseEntity.ok(expenseProofService.getExpenseProofs(expenseId));
    }

    @Operation(
            summary = "Upload expense proof related documents"
    )
    @PostMapping(value = "/{uploadedBy}/{expenseId}")
    public ResponseEntity<List<String>> uploadTravelDocuments(@RequestPart("file") MultipartFile[] files,
                                                                           @PathVariable long uploadedBy,
                                                                           @PathVariable long expenseId){
        return ResponseEntity.ok(expenseProofService.saveFiles(files, uploadedBy, expenseId));
    }

    @Operation(
            summary = "Remove Uploaded expense proof document"
    )
    @DeleteMapping("/{proofId}")
    public ResponseEntity<ApiResponse<String>> deleteTravelDoc(@PathVariable long proofId){
        expenseProofService.deleteSavedFile(proofId);
        return ResponseEntity.ok(ApiResponse.successMsg("Expense proof document deleted successfully"));
    }
}
