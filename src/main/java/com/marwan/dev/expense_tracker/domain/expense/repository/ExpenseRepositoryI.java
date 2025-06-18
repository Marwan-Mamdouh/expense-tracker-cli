package com.marwan.dev.expense_tracker.domain.expense.repository;

import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.Expense;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepositoryI {

  Expense save(Expense expense);

  boolean existsById(Integer id);

  Optional<Expense> findById(Integer id);

  List<Expense> findByMonth(Integer month);

  List<Expense> findByCategory(Category category);

  List<Expense> findByMonthAndCategory(Integer month, Category category);

  List<Expense> findAll();

  Double summeryByMonth(Integer month);

  Double summeryByCategory(Category category);

  Double summeryByMonthAndCategory(Integer month, Category category);

  Double summeryAll();

  void deleteById(Integer id);

  void deleteAll();
}