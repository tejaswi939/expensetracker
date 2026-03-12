package com.expensetracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.expensetracker.entity.Budget;
import com.expensetracker.repository.BudgetRepository;
import com.expensetracker.repository.ExpenseRepository;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock BudgetRepository budgetRepository;
    @Mock ExpenseRepository expenseRepository;
    @InjectMocks BudgetService budgetService;

    @Test
    void setBudget_newCategory_savesNewRecord() {
        Budget budget = new Budget();
        budget.setCategory("Food"); budget.setMonthlyLimit(5000.0);

        when(budgetRepository.findByCategoryAndUserId("Food", 1L)).thenReturn(Optional.empty());
        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget result = budgetService.setBudget(budget);

        assertEquals("Food", result.getCategory());
        assertEquals(5000.0, result.getMonthlyLimit());
        assertEquals(1L, result.getUserId());
        verify(budgetRepository).save(budget);
    }

    @Test
    void setBudget_existingCategory_updatesLimit() {
        Budget existing = new Budget();
        existing.setId(1L); existing.setCategory("Food");
        existing.setMonthlyLimit(3000.0); existing.setUserId(1L);

        Budget incoming = new Budget();
        incoming.setCategory("Food"); incoming.setMonthlyLimit(7000.0);

        when(budgetRepository.findByCategoryAndUserId("Food", 1L)).thenReturn(Optional.of(existing));
        when(budgetRepository.save(existing)).thenReturn(existing);

        Budget result = budgetService.setBudget(incoming);

        assertEquals(7000.0, result.getMonthlyLimit());
        verify(budgetRepository).save(existing);
    }

    @Test
    void getAllBudgets_returnsList() {
        when(budgetRepository.findAll()).thenReturn(List.of(new Budget(), new Budget()));

        List<Budget> result = budgetService.getAllBudgets();

        assertEquals(2, result.size());
    }

    @Test
    void getBudgetStatus_withinLimit_exceededIsFalse() {
        Budget b = new Budget();
        b.setCategory("Food"); b.setMonthlyLimit(5000.0); b.setUserId(1L);

        when(budgetRepository.findByUserId(1L)).thenReturn(List.of(b));
        when(expenseRepository.getMonthlySpendByCategory(eq("Food"), anyInt(), anyInt()))
                .thenReturn(2000.0);

        List<Map<String, Object>> status = budgetService.getBudgetStatus();

        assertEquals(1, status.size());
        assertEquals(false, status.get(0).get("exceeded"));
        assertEquals(3000.0, (Double) status.get(0).get("remaining"), 0.01);
    }

    @Test
    void getBudgetStatus_exceeded_exceededIsTrue() {
        Budget b = new Budget();
        b.setCategory("Transport"); b.setMonthlyLimit(1000.0); b.setUserId(1L);

        when(budgetRepository.findByUserId(1L)).thenReturn(List.of(b));
        when(expenseRepository.getMonthlySpendByCategory(eq("Transport"), anyInt(), anyInt()))
                .thenReturn(1500.0);

        List<Map<String, Object>> status = budgetService.getBudgetStatus();

        assertEquals(true, status.get(0).get("exceeded"));
        assertEquals(-500.0, (Double) status.get(0).get("remaining"), 0.01);
    }

    @Test
    void getBudgetStatus_nullSpend_treatedAsZero() {
        Budget b = new Budget();
        b.setCategory("Health"); b.setMonthlyLimit(2000.0); b.setUserId(1L);

        when(budgetRepository.findByUserId(1L)).thenReturn(List.of(b));
        when(expenseRepository.getMonthlySpendByCategory(eq("Health"), anyInt(), anyInt()))
                .thenReturn(null);

        List<Map<String, Object>> status = budgetService.getBudgetStatus();

        assertEquals(0.0, (Double) status.get(0).get("spent"), 0.01);
        assertEquals(false, status.get(0).get("exceeded"));
    }
}
