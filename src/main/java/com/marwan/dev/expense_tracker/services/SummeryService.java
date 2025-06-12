package com.marwan.dev.expense_tracker.services;

import com.marwan.dev.expense_tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class SummeryService implements CommandInterface<Integer, Double> {

  private final ExpenseRepository expenseRepository;

  public SummeryService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  @Override
  public Double execute(Integer input) {
    if (input != null) {
      return expenseRepository.summeryByMonth(input);
    } else {
      return expenseRepository.summeryAll();
    }
  }
}