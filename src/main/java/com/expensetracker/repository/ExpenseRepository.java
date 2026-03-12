package com.expensetracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expensetracker.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Total spend per category (for reports)
    @Query(value = "SELECT category, SUM(amount) FROM expense GROUP BY category ORDER BY SUM(amount) DESC", nativeQuery = true)
    List<Object[]> getCategoryTotals();

    // Total spend per month (for reports)
    @Query(value = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, SUM(amount) AS total FROM expense GROUP BY DATE_FORMAT(date, '%Y-%m') ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlyTotals();

    // Total spend for a specific category in a specific month (for budget alerts)
    @Query(value = "SELECT SUM(amount) FROM expense WHERE category = :category AND YEAR(date) = :year AND MONTH(date) = :month", nativeQuery = true)
    Double getMonthlySpendByCategory(@Param("category") String category, @Param("year") int year, @Param("month") int month);
}
