package com.marwan.dev.expense_tracker.infrastructure.persistence.implementation;

import static com.marwan.dev.expense_tracker.infrastructure.persistence.util.LockUtils.withReadLock;
import static com.marwan.dev.expense_tracker.infrastructure.persistence.util.LockUtils.withWriteLock;
import static java.util.stream.Collectors.toList;

import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import com.marwan.dev.expense_tracker.domain.budget.repository.BudgetRepositoryI;
import com.marwan.dev.expense_tracker.infrastructure.persistence.util.JsonFileHandlerI;
import com.marwan.dev.expense_tracker.infrastructure.persistence.util.LockUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import org.springframework.stereotype.Repository;

@Repository
public class BudgetRepository implements BudgetRepositoryI {

  private final ReadWriteLock lock;
  private final JsonFileHandlerI fileHandler;
  private final String filePath = String.format("%s/expense-tracker/config.json",
      System.getProperty("user.home"));

  public BudgetRepository(ReadWriteLock lock, JsonFileHandlerI fileHandler) {
    this.fileHandler = fileHandler;
    this.lock = lock;
  }

  /**
   * Save a budget entry. If one exists for the same month and year, replace it.
   *
   * @param budget the budget to save
   * @return the saved budget
   */
  @Override
  public Budget save(Budget budget) {
    return withWriteLock(lock, () -> {
      final List<Budget> budgets = new ArrayList<>(readBudgetFromFile());
      budgets.removeIf(b -> sameMonthAndYear(b, budget));
      budgets.add(budget);
      writeBudgetToFile(budgets);
      return budget;
    });
  }

  /**
   * Find a budget by month and year.
   *
   * @param month the month
   * @param year  the year
   * @return optional budget if found
   */
  @Override
  public Optional<Budget> findByMonthAndYear(Integer month, Integer year) {
    return withReadLock(lock, () -> new ArrayList<>(readBudgetFromFile()).stream()
        .filter(b -> b.getMonth().equals(month) && b.getYear().equals(year)).findFirst());
  }

  /**
   * Find all budgets for a specific year.
   *
   * @param year the year
   * @return list of budgets
   */
  @Override
  public List<Budget> findByYear(Integer year) {
    return withReadLock(lock,
        () -> new ArrayList<>(readBudgetFromFile()).stream().filter(b -> year.equals(b.getYear()))
            .collect(toList()));
  }

  /**
   * Delete budget for a specific month and year.
   *
   * @param month the month
   * @param year  the year
   */
  @Override
  public void deleteByMonthAndYear(Integer month, Integer year) {
    LockUtils.withWriteLock(lock, () -> {
      final List<Budget> budgets = new ArrayList<>(readBudgetFromFile());
      budgets.removeIf(b -> b.getMonth().equals(month) && year.equals(b.getYear()));
      writeBudgetToFile(budgets);
    });
  }

  /**
   * Delete all budget entries.
   */
  @Override
  public void deleteAll() {
    withWriteLock(lock, () -> writeBudgetToFile(new ArrayList<>()));
  }

  /**
   * Count all budget entries.
   */
  @Override
  public int count() {
    return readBudgetFromFile().size();
  }

  /**
   * Utility: Read the list of budgets from the JSON file.
   *
   * @return list of budgets
   */
  private List<Budget> readBudgetFromFile() {
    return fileHandler.read(filePath, Budget.class);
  }


  private void writeBudgetToFile(List<Budget> budgets) {
    fileHandler.write(filePath, budgets);
  }

  /**
   * Utility: Check if two budgets have the same month and year.
   */
  private boolean sameMonthAndYear(Budget a, Budget b) {
    return a.getMonth().equals(b.getMonth()) && a.getYear().equals(b.getYear());
  }
}