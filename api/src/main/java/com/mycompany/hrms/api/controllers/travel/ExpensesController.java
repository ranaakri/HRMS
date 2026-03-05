package com.mycompany.hrms.api.controllers.travel;

import com.mycompany.hrms.api.response.ApiResponse;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.travel.request.AddExpense;
import com.mycompany.hrms.data.dtos.travel.request.UpdateExpenseStatus;
import com.mycompany.hrms.data.dtos.travel.response.ExpenseRes;
import com.mycompany.hrms.service.travel.IExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/travel/expense")
public class ExpensesController {

    private final IExpenseService expenseService;

    @Autowired
    public ExpensesController(IExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @Operation(
            summary = "Get list of expenses by travel id"
    )
    @GetMapping("/{travelId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<List<ExpenseRes>> getListOfExpenseByTravelId(@PathVariable long travelId, @RequestParam(required = false) ZonedDateTime startDate, @RequestParam(required = false) ZonedDateTime endDate){
        if(startDate!=null&&endDate!=null)
            return ResponseEntity.ok(expenseService.getAllExpenseByTravelIdFiltered(travelId, startDate, endDate));
        return ResponseEntity.ok(expenseService.getAllExpenseByTravelId(travelId));
    }

    @Operation(
            summary = "Get my expenses"
    )
    @GetMapping("/user/{userId}/travel/{travelId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<List<ExpenseRes>> getMyExpenses(@PathVariable long userId, @PathVariable long travelId){
        return ResponseEntity.ok(expenseService.getMyExpenses(travelId, userId));
    }

    @Operation(
            summary = "Get Expense by expense id"
    )
    @GetMapping("/expenseId/{expenseId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<ApiResponse<ExpenseRes>> getExpenseByExpenseId(@PathVariable long expenseId){
        return ResponseEntity.ok(ApiResponse.success(expenseService.getExpenseByExpenseId(expenseId), "Expense fetched successfully"));
    }

    @Operation(
            summary = "Add new expense"
    )
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<ApiResponse<Long>> addExpense(@RequestBody AddExpense expense){
        long expenseId = expenseService.addExpense(expense);
        return ResponseEntity.ok(ApiResponse.success(expenseId,"New Expense added successfully"));
    }

    @Operation(
            summary = "Update expense status"
    )
    @PatchMapping("/{expenseId}")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<ApiResponse<String>> updateExpenseStatus(@PathVariable long expenseId, @RequestBody UpdateExpenseStatus expenseStatus){
        Constants.ExpenseStatus status = expenseService.changeExpenseStatus(expenseId, expenseStatus.getStatus(), expenseStatus.getRemarks());
        return ResponseEntity.ok(ApiResponse.successMsg("Status updated successfully: " + status.toString()));
    }

    @Operation(
            summary = "Delete expense"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(@PathVariable long expenseId){
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.ok(ApiResponse.successMsg("Expense deleted successfully"));
    }
}
