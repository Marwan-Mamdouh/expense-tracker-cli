package com.marwan.dev.expense_tracker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marwan.dev.expense_tracker.model.Expense;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ExpenseRepository {

  private static int maxId = 0;
  private final ReadWriteLock lock;
  private final ObjectMapper objectMapper;
  private final String filePath = System.getProperty("user.home") + "/expense.json";

  public ExpenseRepository(ObjectMapper objectMapper, ReadWriteLock lock) {
    this.objectMapper = objectMapper;
    this.lock = lock;
  }

  @PostConstruct
  public void initializeMaxId() {
    final ArrayList<Expense> expenses = readExpenseFromFile();
    maxId = expenses.stream().mapToInt(Expense::getId).max().orElse(0);
  }

  public Expense save(Expense expense) {
    lock.writeLock().lock();
    try {
      final ArrayList<Expense> expenses = readExpenseFromFile();

      final Optional<Expense> existingExpense = expenses.stream()
          .filter(t -> t.getId().equals(expense.getId())).findFirst();

      if (existingExpense.isPresent() && expense.getId() != 0) {
        updateExistingExpense(expense, expenses);
      } else {
        assignNewIdToExpense(expense);
        expenses.add(expense);
      }
      writeExpensesToFile(expenses);
      return expense;
    } finally {
      lock.writeLock().unlock();
    }
  }

  public Optional<Expense> findById(Integer id) {
    lock.readLock().lock();
    try {
      final ArrayList<Expense> expenses = readExpenseFromFile();
      return expenses.stream().filter(expense -> expense.getId().equals(id)).findFirst();
    } finally {
      lock.readLock().unlock();
    }
  }

  public ArrayList<Expense> findAll() {
    lock.readLock().lock();
    try {
      return readExpenseFromFile();
    } finally {
      lock.readLock().unlock();
    }
  }

  public ArrayList<Expense> findByMonth(Integer month) {
    lock.readLock().lock();
    try {
      final ArrayList<Expense> expenses = readExpenseFromFile();
      return expenses.stream().filter(expense -> expense.getCreatedAt().getMonthValue() == month)
          .collect(Collectors.toCollection(ArrayList::new));
    } finally {
      lock.readLock().unlock();
    }
  }

  public Double summeryAll() {
    lock.readLock().lock();
    try {
      return readExpenseFromFile().stream().mapToDouble(Expense::getAmount).sum();
    } finally {
      lock.readLock().unlock();
    }
  }

  public Double summeryByMonth(Integer month) {
    lock.readLock().lock();
    try {
      return readExpenseFromFile().stream()
          .filter(expense -> expense.getCreatedAt().getMonthValue() == month)
          .mapToDouble(Expense::getAmount).sum();
    } finally {
      lock.readLock().unlock();
    }
  }
  // i want an elegant way to write things down in a json file after compile a native version of my java app bc when i make a native version there is no place call app,
  //
  //ask me if i am not very clear of what i want, and answer me when you have time

  public void deleteById(Integer id) {
    lock.writeLock().lock();
    try {
      final ArrayList<Expense> expenses = readExpenseFromFile();
      expenses.removeIf(expense -> expense.getId().equals(id));
      writeExpensesToFile(expenses);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public boolean existsById(Integer id) {
    lock.readLock().lock();
    try {
      final ArrayList<Expense> expenses = readExpenseFromFile();
      return expenses.stream().anyMatch(expense -> expense.getId().equals(id));
    } finally {
      lock.readLock().unlock();
    }
  }

  public void deleteAll() {
    lock.writeLock().lock();
    try {
      writeExpensesToFile(new ArrayList<>());
      maxId = 0;
    } finally {
      lock.writeLock().unlock();
    }
  }

  private ArrayList<Expense> readExpenseFromFile() {
    try {
      final File file = new File(filePath);// TODO need to complete file path
      if (!file.exists()) {
        return new ArrayList<>();
      }
      return objectMapper.readValue(file, new TypeReference<ArrayList<Expense>>() {
      });
    } catch (IOException e) {
      throw new RuntimeException("Error reading from file", e); // TODO: need to complete file path
    }
  }

  private void updateExistingExpense(Expense expense, ArrayList<Expense> expenses) {
    expenses.removeIf(t -> t.getId().equals(expense.getId()));
    expense.setUpdatedAt(LocalDate.now());
    expenses.add(expense);
  }

  private void assignNewIdToExpense(Expense expense) {
    if (expense.getId() == 0) {
      expense.setId(++maxId);
    }
  }

  private void writeExpensesToFile(ArrayList<Expense> expenses) {
    try {
      final File file = new File(filePath); // TODO need to put file path
      file.getParentFile().mkdirs();
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, expenses);
    } catch (IOException e) {
      throw new RuntimeException("Error writing tasks to file: ", e);
    }
  }
}