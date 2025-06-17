package com.marwan.dev.expense_tracker.domain.budget.repository;

import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetRepositoryI {

  Budget save(Budget budget);

  Optional<Budget> findByMonthAndYear(Integer month, Integer year);

  List<Budget> findByYear(Integer year);

  void deleteByMonthAndYear(Integer month, Integer year);

  void deleteAll();

//  boolean existsById(int id);

//  long count();
}