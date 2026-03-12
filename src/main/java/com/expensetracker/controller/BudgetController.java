package com.expensetracker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.expensetracker.entity.Budget;
import com.expensetracker.service.BudgetService;

@RestController
@RequestMapping("/budgets")
@CrossOrigin
public class BudgetController {

    @Autowired
    BudgetService service;

    @PostMapping
    public Budget setBudget(@RequestBody Budget budget) {
        return service.setBudget(budget);
    }

    @GetMapping
    public List<Budget> getAllBudgets() {
        return service.getAllBudgets();
    }

    @GetMapping("/status")
    public List<Map<String, Object>> getBudgetStatus() {
        return service.getBudgetStatus();
    }
}
