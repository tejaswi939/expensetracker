package com.expensetracker.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expensetracker.dto.ExpenseSaveResponse;
import com.expensetracker.entity.Expense;
import com.expensetracker.service.ExpenseService;

@RestController
@RequestMapping("/expenses")
@CrossOrigin
public class ExpenseController {

    @Autowired
    ExpenseService service;

    // Feature 4 - Create (now returns ExpenseSaveResponse with optional budget alert)
    @PostMapping
    public ExpenseSaveResponse addExpense(@RequestBody Expense expense) {
        return service.saveExpense(expense);
    }

    @GetMapping
    public List<Expense> getExpenses() {
        return service.getAllExpenses();
    }

    // Feature 4 - Update
    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        return service.updateExpense(id, expense);
    }

    // Feature 4 - Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        service.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // Feature 2 - Monthly report
    @GetMapping("/report/monthly")
    public List<Map<String, Object>> getMonthlyReport() {
        return service.getMonthlyReport();
    }

    // Feature 2 - Category report
    @GetMapping("/report/category")
    public List<Map<String, Object>> getCategoryReport() {
        return service.getCategoryReport();
    }

    // Feature 3 - CSV export
    @GetMapping("/export/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.csv");

        PrintWriter writer = response.getWriter();
        writer.println("ID,Title,Amount,Category,Date");

        for (Expense e : service.getAllExpenses()) {
            writer.printf("%d,%s,%.2f,%s,%s%n",
                    e.getId(),
                    escapeCsv(e.getTitle()),
                    e.getAmount() != null ? e.getAmount() : 0.0,
                    escapeCsv(e.getCategory()),
                    e.getDate() != null ? e.getDate().toString() : "");
        }
        writer.flush();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
