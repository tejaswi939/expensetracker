package com.expensetracker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.entity.Budget;
import com.expensetracker.repository.BudgetRepository;
import com.expensetracker.repository.ExpenseRepository;

@Service
public class BudgetService {

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    // Create or update a budget limit for a category
    public Budget setBudget(Budget budget) {
        budget.setUserId(1L);
        Optional<Budget> existing = budgetRepository.findByCategoryAndUserId(budget.getCategory(), 1L);
        if (existing.isPresent()) {
            Budget b = existing.get();
            b.setMonthlyLimit(budget.getMonthlyLimit());
            return budgetRepository.save(b);
        }
        return budgetRepository.save(budget);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    // Current month's spend vs limit for every configured category
    public List<Map<String, Object>> getBudgetStatus() {
        List<Budget> budgets = budgetRepository.findByUserId(1L);
        LocalDate now = LocalDate.now();
        List<Map<String, Object>> status = new ArrayList<>();

        for (Budget b : budgets) {
            Double spent = expenseRepository.getMonthlySpendByCategory(
                    b.getCategory(), now.getYear(), now.getMonthValue());
            if (spent == null) spent = 0.0;

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("category", b.getCategory());
            entry.put("limit", b.getMonthlyLimit());
            entry.put("spent", spent);
            entry.put("remaining", b.getMonthlyLimit() - spent);
            entry.put("exceeded", spent > b.getMonthlyLimit());
            status.add(entry);
        }
        return status;
    }
}
