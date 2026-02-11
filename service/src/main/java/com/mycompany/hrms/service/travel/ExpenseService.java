package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.travel.Expenses;
import com.mycompany.hrms.data.entity.travel.ExpensesSplits;
import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.travel.TravelingUser;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.travel.ExpensesRepo;
import com.mycompany.hrms.data.repository.travel.ExpensesSplitsRepo;
import com.mycompany.hrms.data.repository.travel.TravelDetailsRepo;
import com.mycompany.hrms.data.repository.travel.TravelingUserRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.travel.request.AddExpense;
import com.mycompany.hrms.service.dtos.travel.request.AddExpenseSplit;
import com.mycompany.hrms.service.dtos.travel.response.ExpenseRes;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ExpenseService implements IExpenseService {

    private final ExpensesRepo expensesRepo;
    private final TravelDetailsRepo travelDetailsRepo;
    private final UsersRepo usersRepo;
    private final ModelMapper modelMapper;
    private final ExpensesSplitsRepo expensesSplitsRepo;
    private final TravelingUserRepo travelingUserRepo;

    @Autowired
    public ExpenseService(ExpensesRepo expensesRepo, TravelDetailsRepo travelDetailsRepo,UsersRepo usersRepo,ModelMapper modelMapper,ExpensesSplitsRepo expensesSplitsRepo,TravelingUserRepo travelingUserRepo) {
        this.expensesRepo = expensesRepo;
        this.travelDetailsRepo = travelDetailsRepo;
        this.usersRepo = usersRepo;
        this.modelMapper = modelMapper;
        this.expensesSplitsRepo = expensesSplitsRepo;
        this.travelingUserRepo = travelingUserRepo;
    }

    public List<ExpenseRes> getAllExpenseByTravelId(long travelId){
        List<Expenses> expenses = expensesRepo.getExpensesByTravelDetails_TravelId(travelId);

        return expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseRes.class))
                .toList();
    }

    public ExpenseRes getExpenseByExpenseId(long expenseId){
        Expenses expenses = expensesRepo.getExpensesByExpenseId(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        return modelMapper.map(expenses, ExpenseRes.class);
    }

    @Transactional
    public long addExpense(AddExpense expense) {
        TravelDetails travelDetails = travelDetailsRepo.findById(expense.getTravelId())
                .orElseThrow(() -> new ResourceNotFoundException("Travel details not found"));

        Users uploadedBy = usersRepo.findById(expense.getUploadedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Uploaded by user details not found"));

        if (travelDetails.getTotalExpense() >= travelDetails.getAssignedBudget())
            throw new BadRequestException("Assigned budget is already used");

        if (travelDetails.getTotalExpense() + expense.getAmount() > travelDetails.getAssignedBudget())
            throw new BadRequestException("New Expense Exceeds sum of assigned budget");

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

        if (splitSum != savedExpense.getAmount())
            throw new BadRequestException("Sum of splits does not match expense amount");

        travelDetails.setTotalExpense(travelDetails.getTotalExpense() + expense.getAmount());
        travelDetailsRepo.save(travelDetails);
        return expenses.getExpenseId();
    }

    @Transactional
    public Constants.ExpenseStatus changeExpenseStatus(long expenseId, Constants.ExpenseStatus expenseStatus){
        Expenses expenses = expensesRepo.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        switch (expenseStatus.toString()){
            case "APPROVED":
                if(expenses.getStatus().equals(Constants.ExpenseStatus.REJECTED.toString()))
                    redoTravelBalance(expenses);
                expenses.setApprovedAt(ZonedDateTime.now());
                expenses.setStatus(Constants.ExpenseStatus.APPROVED);
                break;
            case "PENDING":
                if(expenses.getStatus().equals(Constants.ExpenseStatus.REJECTED.toString()))
                    redoTravelBalance(expenses);
                expenses.setApprovedAt(ZonedDateTime.now());
                expenses.setStatus(Constants.ExpenseStatus.PENDING);
                break;
            case "REJECTED":
                undoTravelBalance(expenses);
                expenses.setApprovedAt(ZonedDateTime.now());
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
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if(expenses.getStatus().equals(Constants.ExpenseStatus.APPROVED.toString()))
            throw new BadRequestException("Can not delete Approved Expense");
        if(expenses.getStatus().equals(Constants.ExpenseStatus.PENDING.toString()))
            undoTravelBalance(expenses);
        expensesRepo.delete(expenses);
    }
}