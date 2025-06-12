package com.marwan.dev.expense_tracker.services;

import com.marwan.dev.expense_tracker.model.Expense;
import com.marwan.dev.expense_tracker.repository.ExpenseRepository;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class ListService implements CommandInterface<Integer, ArrayList<Expense>> {

  private final ExpenseRepository expenseRepository;

  public ListService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  @Override
  public ArrayList<Expense> execute(Integer input) {
    if (input != null) {
      return expenseRepository.findByMonth(input);
    } else {
      return expenseRepository.findAll();
    }
  }
}