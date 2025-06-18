package com.marwan.dev.expense_tracker.infrastructure.persistence.implementation;

import static com.marwan.dev.expense_tracker.infrastructure.persistence.util.LockUtils.withReadLock;
import static com.marwan.dev.expense_tracker.infrastructure.persistence.util.LockUtils.withWriteLock;

import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.Expense;
import com.marwan.dev.expense_tracker.domain.expense.repository.ExpenseRepositoryI;
import com.marwan.dev.expense_tracker.infrastructure.persistence.util.JsonFileHandlerI;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Predicate;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Expense data backed by a JSON file with thread-safe operations.
 */
@Repository
public class ExpenseRepository implements ExpenseRepositoryI {

  private static Integer maxId = 0;
  private final ReadWriteLock lock;
  private final JsonFileHandlerI fileHandler;
  private final String filePath = String.format("%s/expense-tracker/expense.json",
      System.getProperty("user.home"));

  /**
   * Constructs a new ExpenseRepository instance.
   *
   * @param lock        the read-write lock used for thread-safe operations
   * @param fileHandler the Jackson object mapper for JSON serialization
   */
  public ExpenseRepository(ReadWriteLock lock, JsonFileHandlerI fileHandler) {
    this.fileHandler = fileHandler;
    this.lock = lock;
  }

  /**
   * Initializes the max ID counter from the persisted expenses after bean construction.
   */
  @PostConstruct
  public void initializeMaxId() {
    final List<Expense> expenses = readExpenseFromFile();
    maxId = expenses.stream().mapToInt(Expense::getId).max().orElse(0);
  }

  /**
   * Checks if an expense exists by its ID.
   */
  @Override
  public boolean existsById(Integer id) {
    return withReadLock(lock,
        () -> readExpenseFromFile().stream().anyMatch(expense -> expense.getId().equals(id)));
  }

  /**
   * Saves an expense. If the ID is zero or not found, a new ID is assigned.
   *
   * @param expense the expense to save
   * @return the saved expense
   */
  @Override
  public Expense save(Expense expense) {
    return withWriteLock(lock, () -> {
      final var expenses = new ArrayList<>(readExpenseFromFile());
      final var existingExpense = findExpenseById(expenses, expense.getId());

      if (existingExpense.isPresent() && expense.getId() != 0) {
        updateExistingExpense(expense, expenses);
      } else {
        assignNewIdToExpense(expense);
        expenses.add(expense);
      }
      writeExpensesToFile(expenses);
      return expense;
    });
  }

  /**
   * Finds an expense by ID.
   */
  @Override
  public Optional<Expense> findById(Integer id) {
    return withReadLock(lock,
        () -> readExpenseFromFile().stream().filter(expense -> expense.getId().equals(id))
            .findFirst());
  }

  /**
   * Returns all saved expenses.
   */
  @Override
  public List<Expense> findAll() {
    return withReadLock(lock, this::readExpenseFromFile);
  }

  /**
   * Finds expenses by month.
   */
  @Override
  public List<Expense> findByMonth(Integer month) {
    return findFiltered(expense -> month.equals(expense.getCreatedAt().getMonthValue()));
  }

  /**
   * Finds expenses by category.
   */
  @Override
  public List<Expense> findByCategory(Category category) {
    return findFiltered(expense -> expense.getCategory().equals(category));
  }

  /**
   * Finds expenses by both month and category.
   */
  @Override
  public List<Expense> findByMonthAndCategory(Integer month, Category category) {
    return findFiltered(expense -> expense.getCategory().equals(category) && month.equals(
        expense.getCreatedAt().getMonthValue()));
  }

  /**
   * Returns the total sum of all expenses.
   */
  @Override
  public Double summeryAll() {
    return sumFiltered(expense -> true);
  }

  /**
   * Returns the total sum of expenses filtered by month.
   */
  @Override
  public Double summeryByMonth(Integer month) {
    return sumFiltered(expense -> expense.getCreatedAt().getMonthValue() == month);
  }

  /**
   * Returns the total sum of expenses filtered by category.
   */
  @Override
  public Double summeryByCategory(Category category) {
    return sumFiltered(expense -> expense.getCategory().equals(category));
  }

  /**
   * Returns the total sum of expenses filtered by both month and category.
   */
  @Override
  public Double summeryByMonthAndCategory(Integer month, Category category) {
    return sumFiltered(
        expense -> month.equals(expense.getCreatedAt().getMonthValue()) && expense.getCategory()
            .equals(category));
  }

  /**
   * Deletes an expense by ID.
   */
  @Override
  public void deleteById(Integer id) {
    withWriteLock(lock, () -> {
      final var expenses = readExpenseFromFile();
      expenses.removeIf(expense -> expense.getId().equals(id));
      writeExpensesToFile(expenses);
    });
  }

  /**
   * Deletes all expenses and resets the ID counter.
   */
  @Override
  public void deleteAll() {
    withWriteLock(lock, () -> {
      writeExpensesToFile(new ArrayList<>());
      maxId = 0;
    });
  }

  // ================== PRIVATE HELPERS ==================

  private List<Expense> readExpenseFromFile() {
    return fileHandler.read(filePath, Expense.class);
  }

  private Optional<Expense> findExpenseById(List<Expense> expenses, int id) {
    return expenses.stream().filter(t -> t.getId().equals(id)).findFirst();
  }

  private List<Expense> findFiltered(Predicate<Expense> condition) {
    return withReadLock(lock, () -> readExpenseFromFile().stream().filter(condition).toList());
  }

  private Double sumFiltered(Predicate<Expense> condition) {
    return withReadLock(lock,
        () -> readExpenseFromFile().stream().filter(condition).mapToDouble(Expense::getAmount)
            .sum());
  }

  private void updateExistingExpense(Expense expense, List<Expense> expenses) {
    expenses.removeIf(t -> t.getId().equals(expense.getId()));
    expense.setUpdatedAt(LocalDate.now());
    expenses.add(expense);
  }

  private void assignNewIdToExpense(Expense expense) {
    if (expense.getId() == 0) {
      expense.setId(++maxId);
    }
  }

  private void writeExpensesToFile(List<Expense> expenses) {
    fileHandler.write(filePath, expenses);
  }
}