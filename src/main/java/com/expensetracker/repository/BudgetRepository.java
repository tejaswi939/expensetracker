package com.expensetracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.expensetracker.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByCategoryAndUserId(String category, Long userId);

    List<Budget> findByUserId(Long userId);
}
