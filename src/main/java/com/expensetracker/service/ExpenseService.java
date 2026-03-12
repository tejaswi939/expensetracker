package com.expensetracker.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.dto.ExpenseSaveResponse;
import com.expensetracker.entity.Budget;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.repository.BudgetRepository;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BudgetRepository budgetRepository;

    // Feature 4 - Save expense + Feature 1 - budget alert check
    public ExpenseSaveResponse saveExpense(Expense expense) {

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default user not found. Please register a user first."));

        expense.setUser(user);
        Expense saved = expenseRepository.save(expense);

        String alert = null;
        if (saved.getCategory() != null && saved.getDate() != null) {
            Optional<Budget> budgetOpt = budgetRepository.findByCategoryAndUserId(saved.getCategory(), 1L);
            if (budgetOpt.isPresent()) {
                Double spent = expenseRepository.getMonthlySpendByCategory(
                        saved.getCategory(),
                        saved.getDate().getYear(),
                        saved.getDate().getMonthValue());
                if (spent == null) spent = 0.0;
                Budget b = budgetOpt.get();
                if (spent > b.getMonthlyLimit()) {
                    alert = String.format("Budget exceeded for '%s'! Limit: %.2f, Spent this month: %.2f",
                            saved.getCategory(), b.getMonthlyLimit(), spent);
                }
            }
        }

        return new ExpenseSaveResponse(saved, alert);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Feature 4 - Update
    public Expense updateExpense(Long id, Expense updated) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
        existing.setTitle(updated.getTitle());
        existing.setAmount(updated.getAmount());
        existing.setCategory(updated.getCategory());
        existing.setDate(updated.getDate());
        return expenseRepository.save(existing);
    }

    // Feature 4 - Delete
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    // Feature 2 - Monthly report
    public List<Map<String, Object>> getMonthlyReport() {
        List<Object[]> rows = expenseRepository.getMonthlyTotals();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("month", row[0]);
            entry.put("total", row[1]);
            result.add(entry);
        }
        return result;
    }

    // Feature 2 - Category report
    public List<Map<String, Object>> getCategoryReport() {
        List<Object[]> rows = expenseRepository.getCategoryTotals();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("category", row[0]);
            entry.put("total", row[1]);
            result.add(entry);
        }
        return result;
    }
}
