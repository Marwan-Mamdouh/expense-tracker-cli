package com.marwan.dev.expense_tracker.services;

import com.marwan.dev.expense_tracker.model.Category;
import com.marwan.dev.expense_tracker.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class SummeryService implements CommandInterface<SearchArgsForList, Double> {

  private final ExpenseRepository expenseRepository;

  public SummeryService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  @Override
  public Double execute(SearchArgsForList input) {
    if (input == null) {
      return expenseRepository.summeryAll();
    }
    return handleNullArgsHelper(input.month() == null, input.category() == null, input);
  }

  private Double handleNullArgsHelper(boolean isMonthNull, boolean isCategoryNull,
      SearchArgsForList input) {
    if (!isMonthNull && isCategoryNull) {
      return expenseRepository.summeryByMonth(input.month());
    } else if (!isCategoryNull && isMonthNull) {
      return expenseRepository.summeryByCategory(Category.from(input.category()));
    } else {
      return expenseRepository.summeryByMonthAndCategory(input);
    }
  }
}