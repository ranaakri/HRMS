package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.entity.travel.Expenses;
import com.mycompany.hrms.data.entity.travel.ExpensesProofs;
import com.mycompany.hrms.data.repository.travel.ExpensesProofsRepo;
import com.mycompany.hrms.data.repository.travel.ExpensesRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.travel.response.ExpenseProofRes;
import com.mycompany.hrms.service.exception.InternalServerException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import com.mycompany.hrms.service.exception.UnAuthorizedException;
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
public class ExpenseProofService implements IExpenseProofService {

    private final UsersRepo usersRepo;
    private final Path root;
    private final ModelMapper modelMapper;
    private final ExpensesProofsRepo expensesProofsRepo;
    private final ExpensesRepo expensesRepo;

    @Autowired
    public ExpenseProofService(
                               UsersRepo usersRepo,
                               ModelMapper modelMapper,
                               ExpensesProofsRepo expensesProofsRepo, ExpensesRepo expensesRepo){
        this.root = Paths.get("uploads/travel/expenseProofs");
        this.usersRepo = usersRepo;
        init();
        this.modelMapper = modelMapper;
        this.expensesProofsRepo = expensesProofsRepo;
        this.expensesRepo = expensesRepo;
    }

    private void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new InternalServerException("Could not initialize folder for upload!");
        }
    }

    public List<ExpenseProofRes> getExpenseProofs(long expenseId){
        List<ExpensesProofs> documents = expensesProofsRepo.getExpensesProofsByExpenses_ExpenseId(expenseId);
        return documents.stream().map(val -> modelMapper.map(val, ExpenseProofRes.class)).toList();
    }

    public List<String> saveFiles(MultipartFile[] files, long uploadedBy, long expenseId) {
        if(!usersRepo.existsById(uploadedBy))
            throw new ResourceNotFoundException("Uploaded by user not found");

        Expenses expenses = expensesRepo.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if(expenses.getUploadedBy().getUserId() != uploadedBy)
            throw new UnAuthorizedException("Do not have authority to upload proof document");

        return Arrays.stream(files).map(file -> {
            try {
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), this.root.resolve(filename));
                ExpensesProofs expensesProofs = new ExpensesProofs();
                expensesProofs.setProofFilePath(filename);
                expensesProofs.setExpenses(expenses);
                expensesProofsRepo.save(expensesProofs);
                return filename;
            } catch (Exception e) {
                throw new InternalServerException("Could not store the file. Error: " + e.getMessage());
            }
        }).toList();
    }

    public void deleteSavedFile(long proofId) {
        ExpensesProofs res = expensesProofsRepo.findById(proofId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        try {
            Path filePath = root.resolve(res.getProofFilePath()).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new InternalServerException("Could not delete file from storage: " + e.getMessage());
        }

        expensesProofsRepo.delete(res);
    }
}
