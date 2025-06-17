package com.marwan.dev.expense_tracker.domain.budget.repository;

import static com.marwan.dev.expense_tracker.util.LockUtils.withReadLock;
import static com.marwan.dev.expense_tracker.util.LockUtils.withWriteLock;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import com.marwan.dev.expense_tracker.util.LockUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import org.springframework.stereotype.Repository;

@Repository
public class BudgetRepository {

  private final ReadWriteLock lock;
  private final ObjectMapper mapper;
  private final String filePath = String.format("%s/expense-tracker/config.json",
      System.getProperty("user.home"));

  public BudgetRepository(ObjectMapper mapper, ReadWriteLock lock) {
    this.mapper = mapper;
    this.lock = lock;
  }

  /**
   * Save a budget entry. If one exists for the same month and year, replace it.
   *
   * @param budget the budget to save
   * @return the saved budget
   */
  public Budget save(Budget budget) {
    return withWriteLock(lock, () -> {
      List<Budget> budgets = readBudgetFromFile();
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
  public Optional<Budget> findByMonthAndYear(Integer month, Integer year) {
    return withReadLock(lock, () -> readBudgetFromFile().stream()
        .filter(e -> e.getMonth().equals(month) && e.getYear().equals(year)).findFirst());
  }

  /**
   * Find all budgets for a specific year.
   *
   * @param year the year
   * @return list of budgets
   */
  public List<Budget> findByYear(Integer year) {
    return withReadLock(lock,
        () -> readBudgetFromFile().stream().filter(e -> year.equals(e.getYear()))
            .collect(toList()));
  }

  /**
   * Delete budget for a specific month and year.
   *
   * @param month the month
   * @param year  the year
   */
  public void deleteByMonthAndYear(Integer month, Integer year) {
    LockUtils.withWriteLock(lock, () -> {
      List<Budget> budgets = readBudgetFromFile();
      budgets.removeIf(b -> b.getMonth().equals(month) && year.equals(b.getYear()));
      writeBudgetToFile(budgets);
    });
  }

  /**
   * Delete all budget entries.
   */
  public void deleteAll() {
    withWriteLock(lock, () -> writeBudgetToFile(new ArrayList<>()));
  }

  /**
   * Utility: Read the list of budgets from the JSON file.
   *
   * @return list of budgets
   */
  private List<Budget> readBudgetFromFile() {
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        return new ArrayList<>();
      }
      return mapper.readValue(file, new TypeReference<>() {
      });
    } catch (IOException e) {
      throw new RuntimeException("Error reading from file", e);
    }
  }

  /**
   * Utility: Write the list of budgets to the JSON file.
   *
   * @param budgets list to write
   */
  private void writeBudgetToFile(List<Budget> budgets) {
    try {
      File file = new File(filePath);
      file.getParentFile().mkdirs();
      mapper.writerWithDefaultPrettyPrinter().writeValue(file, budgets);
    } catch (IOException e) {
      throw new RuntimeException("Error writing budgets to file", e);
    }
  }

  /**
   * Utility: Check if two budgets have the same month and year.
   */
  private boolean sameMonthAndYear(Budget a, Budget b) {
    return a.getMonth().equals(b.getMonth()) && a.getYear().equals(b.getYear());
  }
}