package com.marwan.dev.expense_tracker.domain.expense.service;

import com.marwan.dev.expense_tracker.CommandInterface;
import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.Expense;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.CreateExpenseRequest;
import com.marwan.dev.expense_tracker.domain.expense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class AddExpenseService implements CommandInterface<CreateExpenseRequest, Expense> {

  private final ExpenseRepository expenseRepository;

  public AddExpenseService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  @Override
  public Expense execute(CreateExpenseRequest input) {
    return expenseRepository.save(
        new Expense(input.description(), input.amount(), Category.from(input.category())));
  }
}