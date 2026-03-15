package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.travel.*;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.travel.*;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.data.dtos.DocResponse;
import com.mycompany.hrms.data.dtos.travel.request.AddExpense;
import com.mycompany.hrms.data.dtos.travel.request.AddExpenseSplit;
import com.mycompany.hrms.data.dtos.travel.response.ExpenseRes;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import com.mycompany.hrms.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService implements IExpenseService {

    private static final String EXPENSE_NOT_FOUND = "Expense not found";

    private final ExpensesRepo expensesRepo;
    private final TravelDetailsRepo travelDetailsRepo;
    private final UsersRepo usersRepo;
    private final ModelMapper modelMapper;
    private final ExpensesSplitsRepo expensesSplitsRepo;
    private final TravelingUserRepo travelingUserRepo;
    private final ExpensesProofsRepo expensesProofsRepo;
    private final NotificationService notificationService;

    @Autowired
    public ExpenseService(ExpensesRepo expensesRepo,
                          TravelDetailsRepo travelDetailsRepo,
                          UsersRepo usersRepo,
                          ModelMapper modelMapper,
                          ExpensesSplitsRepo expensesSplitsRepo,
                          TravelingUserRepo travelingUserRepo,
                          ExpensesProofsRepo expensesProofsRepo,
                          NotificationService notificationService) {
        this.expensesRepo = expensesRepo;
        this.travelDetailsRepo = travelDetailsRepo;
        this.usersRepo = usersRepo;
        this.modelMapper = modelMapper;
        this.expensesSplitsRepo = expensesSplitsRepo;
        this.travelingUserRepo = travelingUserRepo;
        this.expensesProofsRepo = expensesProofsRepo;
        this.notificationService = notificationService;
    }

    public List<ExpenseRes> getAllExpenseByTravelId(long travelId) {

        List<Expenses> expenses =
                expensesRepo.findWithSplitsByTravelDetails_TravelId(travelId);

        if(!expenses.isEmpty()) {
            expensesRepo.findWithProofsByTravelDetails_TravelId(travelId);
        }

        return expenses.stream()
                .map(expense -> {
                    ExpenseRes res = modelMapper.map(expense, ExpenseRes.class);
                    if(res.getExpensesProofs().isEmpty())
                        res.setExpensesProofs(List.of());
                    return res;
                })
                .toList();
    }

    public List<ExpenseRes> getAllExpenseByTravelIdFiltered(long travelId, ZonedDateTime startDate, ZonedDateTime endDate) {

        List<Expenses> expenses =
                expensesRepo.findWithSplitsByTravelDetails_TravelId(travelId);

        if(!expenses.isEmpty()) {
            expensesRepo.findWithProofsByTravelDetails_TravelId(travelId);
        }

        return expenses.stream()
                .filter(val -> val.getExpenseDate().isAfter(startDate) && val.getExpenseDate().isBefore(endDate))
                .map(expense -> {
                    ExpenseRes res = modelMapper.map(expense, ExpenseRes.class);
                    if(res.getExpensesProofs().isEmpty())
                        res.setExpensesProofs(List.of());
                    return res;
                })
                .toList();
    }

    public List<ExpenseRes> getMyExpenses(long travelId, long userId){
        List<Expenses> myExpenses = expensesRepo.getAllByTravelDetails_TravelIdAndExpensesSplits_TravelingUser_User_UserId(travelId, userId);
        if(!myExpenses.isEmpty()) {
            expensesRepo.findWithProofsByTravelDetails_TravelId(travelId);
        }
        return myExpenses.stream().map( val -> {
            ExpenseRes res = modelMapper.map(val, ExpenseRes.class);
            if(res.getExpensesProofs().isEmpty())
                res.setExpensesProofs(List.of());
            return res;
        }).toList();
    }

    public ExpenseRes getExpenseByExpenseId(long expenseId){
        Expenses expenses = expensesRepo.getExpensesByExpenseId(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException(EXPENSE_NOT_FOUND));
        return modelMapper.map(expenses, ExpenseRes.class);
    }

    @Transactional
    public long addExpense(AddExpense expense) {
        TravelDetails travelDetails = travelDetailsRepo.findById(expense.getTravelId())
                .orElseThrow(() -> new ResourceNotFoundException("Travel details not found"));

        Users uploadedBy = usersRepo.findById(expense.getUploadedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Uploaded by user details not found"));

        if(!(travelDetails.getStatus().equals(Constants.TravelStatus.ONGOING.toString()) || travelDetails.getStatus().equals(Constants.TravelStatus.PENDING.toString())))
            throw new BadRequestException("Can not add new expense");
        if (travelDetails.getTotalExpense() >= travelDetails.getAssignedBudget())
            throw new BadRequestException("Assigned budget is already used");

        if (travelDetails.getTotalExpense() + expense.getAmount() > travelDetails.getAssignedBudget())
            throw new BadRequestException("New Expense Exceeds sum of assigned budget");

        long days = ChronoUnit.DAYS.between(travelDetails.getEndDate(), ZonedDateTime.now());
        if(days > 10){
            throw new BadRequestException("Exceeded time duration of 10 days");
        }

        Expenses expenses = modelMapper.map(expense, Expenses.class);
        expenses.setUploadedBy(uploadedBy);
        expenses.setTravelDetails(travelDetails);
        expenses.setExpensesSplits(null);

        Expenses savedExpense = expensesRepo.save(expenses);

        float splitSum = 0;
        for (AddExpenseSplit splitDto : expense.getExpensesSplits()) {
            splitSum += splitDto.getSplitAmount();

            TravelingUser travelingUser = travelingUserRepo.findById(splitDto.getTravelingUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Traveling user not found"));

            float newUsedBalance = splitDto.getSplitAmount() + travelingUser.getUsedBalance();
            if (newUsedBalance > travelingUser.getTravelBalance())
                throw new BadRequestException("Split exceeds user's travel balance: " + travelingUser.getTravelingUserId());

            ExpensesSplits splitEntity = modelMapper.map(splitDto, ExpensesSplits.class);
            splitEntity.setExpense(savedExpense);
            splitEntity.setTravelingUser(travelingUser);

            expensesSplitsRepo.save(splitEntity);

            travelingUser.setUsedBalance(newUsedBalance);
            travelingUserRepo.save(travelingUser);
        }

        List<ExpensesProofs> proofs = new ArrayList<>();
        for(DocResponse docRes : expense.getExpenseProof()){
            ExpensesProofs proof = new ExpensesProofs();
            proof.setPublicId(docRes.getPublicId());
            proof.setProofFilePath(docRes.getPath());
            proof.setExpenses(savedExpense);
            proofs.add(proof);
        }
        expensesProofsRepo.saveAll(proofs);

        if (splitSum != savedExpense.getAmount())
            throw new BadRequestException("Sum of splits does not match expense amount");

        travelDetails.setTotalExpense(travelDetails.getTotalExpense() + expense.getAmount());
        travelDetailsRepo.save(travelDetails);

        List<Users> splitWith = new ArrayList<>(expense
                .getExpensesSplits()
                .stream()
                .map(
            val -> travelingUserRepo
                    .findById(val.getTravelingUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for notification"))
                    .getUser())
                .toList());
        splitWith.remove(uploadedBy);
        notificationService.addNotification(splitWith, "EXPENSE_SPLIT", "by " + uploadedBy.getName());
        return expenses.getExpenseId();
    }

    @Transactional
    public Constants.ExpenseStatus changeExpenseStatus(long expenseId, Constants.ExpenseStatus expenseStatus, String remarks){
        Expenses expenses = expensesRepo.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException(EXPENSE_NOT_FOUND));

        switch (expenseStatus){
            case APPROVED:
                if(expenses.getStatus().equals(Constants.ExpenseStatus.REJECTED.toString()))
                    redoTravelBalance(expenses);
                expenses.setApprovedAt(ZonedDateTime.now());
                expenses.setRemarks(remarks);
                expenses.setStatus(Constants.ExpenseStatus.APPROVED);
                break;
            case PENDING:
                if(expenses.getStatus().equals(Constants.ExpenseStatus.REJECTED.toString()))
                    redoTravelBalance(expenses);
                expenses.setApprovedAt(ZonedDateTime.now());
                expenses.setRemarks(remarks);
                expenses.setStatus(Constants.ExpenseStatus.PENDING);
                break;
            case REJECTED:
                undoTravelBalance(expenses);
                expenses.setApprovedAt(ZonedDateTime.now());
                expenses.setRemarks(remarks);
                expenses.setStatus(Constants.ExpenseStatus.REJECTED);
                break;
            default:
                throw new BadRequestException("Invalid Status provided");
        }
        return expenseStatus;
    }

    @Transactional
    protected void redoTravelBalance(Expenses expenses){
        TravelDetails travelDetails = expenses.getTravelDetails();
        float expenseAmount = expenses.getAmount();
        travelDetails.setTotalExpense(travelDetails.getTotalExpense() + expenseAmount);

        for(ExpensesSplits e : expenses.getExpensesSplits()){
            float balance = e.getTravelingUser().getUsedBalance();
            e.getTravelingUser().setUsedBalance(balance + e.getSplitAmount());
        }
    }

    @Transactional
    protected void undoTravelBalance(Expenses expenses){
        TravelDetails travelDetails = expenses.getTravelDetails();
        float expenseAmount = expenses.getAmount();
        travelDetails.setTotalExpense(travelDetails.getTotalExpense() - expenseAmount);

        for(ExpensesSplits e : expenses.getExpensesSplits()){
            float balance = e.getTravelingUser().getUsedBalance();
            e.getTravelingUser().setUsedBalance(balance - e.getSplitAmount());
        }
    }

    @Transactional
    public void deleteExpense(long expenseId){
        Expenses expenses = expensesRepo.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException(EXPENSE_NOT_FOUND));

        if(expenses.getStatus().equals(Constants.ExpenseStatus.APPROVED.toString()))
            throw new BadRequestException("Can not delete Approved Expense");
        if(expenses.getStatus().equals(Constants.ExpenseStatus.PENDING.toString()))
            undoTravelBalance(expenses);
        expensesRepo.delete(expenses);
    }
}