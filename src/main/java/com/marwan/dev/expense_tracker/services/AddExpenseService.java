package com.marwan.dev.expense_tracker.services;

import com.marwan.dev.expense_tracker.model.CreateExpenseDTO;
import com.marwan.dev.expense_tracker.model.Expense;
import com.marwan.dev.expense_tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class AddExpenseService implements CommandInterface<CreateExpenseDTO, Expense> {

  private final ExpenseRepository expenseRepository;

  public AddExpenseService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  @Override
  public Expense execute(CreateExpenseDTO input) {
    return expenseRepository.save(new Expense(input.description(), input.amount()));
  }
}