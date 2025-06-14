package com.marwan.dev.expense_tracker.domain.expense.service;

import com.marwan.dev.expense_tracker.CommandInterface;
import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.Expense;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.domain.expense.repository.ExpenseRepository;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class ListExpensesService implements CommandInterface<SearchArgsForList, ArrayList<Expense>> {

  private final ExpenseRepository expenseRepository;

  public ListExpensesService(ExpenseRepository expenseRepository) {
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