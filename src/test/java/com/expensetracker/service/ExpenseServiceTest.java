package com.expensetracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.expensetracker.dto.ExpenseSaveResponse;
import com.expensetracker.entity.Budget;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.repository.BudgetRepository;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock ExpenseRepository expenseRepository;
    @Mock UserRepository userRepository;
    @Mock BudgetRepository budgetRepository;
    @InjectMocks ExpenseService expenseService;

    @Test
    void saveExpense_noBudgetConfigured_returnsResponseWithNoAlert() {
        User user = new User(); user.setId(1L);
        Expense expense = buildExpense("Lunch", 200.0, "Food", LocalDate.of(2026, 3, 12));
        Expense saved  = buildExpense("Lunch", 200.0, "Food", LocalDate.of(2026, 3, 12)); saved.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepository.save(expense)).thenReturn(saved);
        when(budgetRepository.findByCategoryAndUserId("Food", 1L)).thenReturn(Optional.empty());

        ExpenseSaveResponse response = expenseService.saveExpense(expense);

        assertNotNull(response.getExpense());
        assertNull(response.getBudgetAlert());
    }

    @Test
    void saveExpense_budgetExceeded_returnsAlert() {
        User user = new User(); user.setId(1L);
        Expense expense = buildExpense("Dinner", 500.0, "Food", LocalDate.of(2026, 3, 12));
        Expense saved  = buildExpense("Dinner", 500.0, "Food", LocalDate.of(2026, 3, 12)); saved.setId(2L);

        Budget budget = new Budget();
        budget.setCategory("Food"); budget.setMonthlyLimit(300.0); budget.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepository.save(expense)).thenReturn(saved);
        when(budgetRepository.findByCategoryAndUserId("Food", 1L)).thenReturn(Optional.of(budget));
        when(expenseRepository.getMonthlySpendByCategory("Food", 2026, 3)).thenReturn(500.0);

        ExpenseSaveResponse response = expenseService.saveExpense(expense);

        assertNotNull(response.getBudgetAlert());
        assertTrue(response.getBudgetAlert().contains("exceeded"));
    }

    @Test
    void saveExpense_budgetWithinLimit_returnsNoAlert() {
        User user = new User(); user.setId(1L);
        Expense expense = buildExpense("Coffee", 50.0, "Food", LocalDate.of(2026, 3, 12));
        Expense saved  = buildExpense("Coffee", 50.0, "Food", LocalDate.of(2026, 3, 12)); saved.setId(3L);

        Budget budget = new Budget();
        budget.setCategory("Food"); budget.setMonthlyLimit(1000.0); budget.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepository.save(expense)).thenReturn(saved);
        when(budgetRepository.findByCategoryAndUserId("Food", 1L)).thenReturn(Optional.of(budget));
        when(expenseRepository.getMonthlySpendByCategory("Food", 2026, 3)).thenReturn(200.0);

        ExpenseSaveResponse response = expenseService.saveExpense(expense);

        assertNull(response.getBudgetAlert());
    }

    @Test
    void deleteExpense_existing_deletesSuccessfully() {
        when(expenseRepository.existsById(1L)).thenReturn(true);

        expenseService.deleteExpense(1L);

        verify(expenseRepository).deleteById(1L);
    }

    @Test
    void deleteExpense_notFound_throwsRuntimeException() {
        when(expenseRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> expenseService.deleteExpense(99L));
        verify(expenseRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateExpense_existing_updatesFields() {
        Expense existing = buildExpense("Old Title", 100.0, "Food", LocalDate.of(2026, 1, 1));
        existing.setId(1L);
        Expense payload  = buildExpense("New Title", 250.0, "Transport", LocalDate.of(2026, 3, 10));

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(expenseRepository.save(existing)).thenReturn(existing);

        Expense result = expenseService.updateExpense(1L, payload);

        assertEquals("New Title", result.getTitle());
        assertEquals(250.0, result.getAmount());
        assertEquals("Transport", result.getCategory());
    }

    @Test
    void updateExpense_notFound_throwsRuntimeException() {
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> expenseService.updateExpense(99L, new Expense()));
    }

    @Test
    void getCategoryReport_returnsMappedResults() {
        List<Object[]> categoryRows = new java.util.ArrayList<>();
        categoryRows.add(new Object[]{"Food", 1500.0});
        categoryRows.add(new Object[]{"Transport", 800.0});
        when(expenseRepository.getCategoryTotals()).thenReturn(categoryRows);

        List<java.util.Map<String, Object>> report = expenseService.getCategoryReport();

        assertEquals(2, report.size());
        assertEquals("Food", report.get(0).get("category"));
        assertEquals(1500.0, report.get(0).get("total"));
    }

    @Test
    void getMonthlyReport_returnsMappedResults() {
        List<Object[]> monthlyRows = new java.util.ArrayList<>();
        monthlyRows.add(new Object[]{"2026-03", 3200.0});
        when(expenseRepository.getMonthlyTotals()).thenReturn(monthlyRows);

        List<java.util.Map<String, Object>> report = expenseService.getMonthlyReport();

        assertEquals(1, report.size());
        assertEquals("2026-03", report.get(0).get("month"));
    }

    // --- helper ---
    private Expense buildExpense(String title, double amount, String category, LocalDate date) {
        Expense e = new Expense();
        e.setTitle(title); e.setAmount(amount); e.setCategory(category); e.setDate(date);
        return e;
    }
}
