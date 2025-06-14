package com.marwan.dev.expense_tracker.domain.expense.service;

import com.marwan.dev.expense_tracker.CommandInterface;
import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.domain.expense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class SummeryExpensesService implements CommandInterface<SearchArgsForList, Double> {

  private final ExpenseRepository expenseRepository;

  public SummeryExpensesService(ExpenseRepository expenseRepository) {
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