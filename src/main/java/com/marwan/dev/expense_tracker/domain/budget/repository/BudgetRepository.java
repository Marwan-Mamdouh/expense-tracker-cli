package com.marwan.dev.expense_tracker.domain.budget.repository;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;
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

  public Budget save(Budget budget) {
    lock.writeLock().lock();
    try {
      final List<Budget> budgets = readBudgetFromFile();
      final Optional<Budget> exitingBudget = budgets.stream().filter(
              b -> b.getMonth().equals(budget.getMonth()) && b.getYear().equals(budget.getYear()))
          .findFirst();
      if (exitingBudget.isPresent()) {
        updateExistingBudget(budget, budgets);
      } else {
        budgets.add(budget);
      }
      writeBudgetToFile(budgets);
      return budget;
    } finally {
      lock.writeLock().unlock();
    }
  }

  public Optional<Budget> findByMonthAndYear(Integer month, Integer year) {
    return withReadLock(() -> readBudgetFromFile().stream()
        .filter(e -> e.getMonth().equals(month) && e.getYear().equals(year)).findFirst());
  }

  public List<Budget> findByYear(Integer year) {
    return withReadLock(() -> readBudgetFromFile().stream().filter(e -> year.equals(e.getYear()))
        .collect(toList()));
  }

  public void deleteByMonthAndYear(Integer month, Integer year) {
    lock.writeLock().lock();
    try {
      final List<Budget> budgets = readBudgetFromFile();
      budgets.removeIf(budget -> budget.getMonth().equals(month) && year.equals(budget.getYear()));
      writeBudgetToFile(budgets);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void deleteAll() {
    lock.writeLock().lock();
    try {
      writeBudgetToFile(new ArrayList<>());
    } finally {
      lock.writeLock().unlock();
    }
  }


  private void updateExistingBudget(Budget budget, List<Budget> budgets) {
    budgets.removeIf(
        b -> b.getMonth().equals(budget.getMonth()) && b.getYear().equals(budget.getYear()));
    budgets.add(budget);
  }

  private List<Budget> readBudgetFromFile() {
    try {
      final File file = new File(filePath);
      if (!file.exists()) {
        return new ArrayList<>();
      }
      return mapper.readValue(file, new TypeReference<ArrayList<Budget>>() {
      });
    } catch (IOException e) {
      throw new RuntimeException("Error reading from file", e);
    }
  }

  private void writeBudgetToFile(List<Budget> budgets) {
    try {
      final File file = new File(filePath);
      file.getParentFile().mkdirs();
      mapper.writerWithDefaultPrettyPrinter().writeValue(file, budgets);
    } catch (IOException e) {
      throw new RuntimeException("Error writing personal config to file: ", e);
    }
  }

  private <R> R withReadLock(Supplier<R> work) {
    lock.readLock().lock();
    try {
      return work.get();
    } finally {
      lock.readLock().unlock();
    }
  }
}