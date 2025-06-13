package com.marwan.dev.expense_tracker.services;

import com.marwan.dev.expense_tracker.model.Category;
import com.marwan.dev.expense_tracker.model.Expense;
import com.marwan.dev.expense_tracker.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.repository.ExpenseRepository;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class ListService implements CommandInterface<SearchArgsForList, ArrayList<Expense>> {

  private final ExpenseRepository expenseRepository;

  public ListService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  @Override
  public ArrayList<Expense> execute(SearchArgsForList input) {
    if (input == null) {
      return expenseRepository.findAll();
    }
    return handleNullArgsHelper(input.month() == null, input.category() == null, input);
  }

  private ArrayList<Expense> handleNullArgsHelper(boolean isMonthNull, boolean isCategoryNull,
      SearchArgsForList input) {
    if (!isMonthNull && isCategoryNull) {
      return expenseRepository.findByMonth(input.month());
    } else if (!isCategoryNull && isMonthNull) {
      return expenseRepository.findByCategory(Category.from(input.category()));
    } else {
      return expenseRepository.findByMonthAndCategory(input);
    }
  }
}