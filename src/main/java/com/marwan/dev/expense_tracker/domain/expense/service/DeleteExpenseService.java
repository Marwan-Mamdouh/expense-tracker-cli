package com.marwan.dev.expense_tracker.domain.expense.service;

import com.marwan.dev.expense_tracker.CommandInterface;
import com.marwan.dev.expense_tracker.domain.expense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteExpenseService implements CommandInterface<Integer, String> {

  private final ExpenseRepository expenseRepository;

  public DeleteExpenseService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  @Override
  public String execute(Integer input) {
    try {
      expenseRepository.deleteById(input);
      return "expense deleted.";
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }
  }
}