package com.marwan.dev.expense_tracker.infrastructure.persistence.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.Expense;
import com.marwan.dev.expense_tracker.domain.expense.repository.ExpenseRepositoryI;
import com.marwan.dev.expense_tracker.infrastructure.persistence.util.JsonFileHandler;
import com.marwan.dev.expense_tracker.infrastructure.persistence.util.JsonFileHandlerI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExpenseRepositoryTest {

  private ExpenseRepositoryI repository;
  private JsonFileHandlerI mockHandler;

  @BeforeEach
  protected void setUp() {
    mockHandler = mock(JsonFileHandler.class);
    repository = new ExpenseRepository(new ReentrantReadWriteLock(), mockHandler);
  }

  @Test
  void save_saves_a_new_expense() {
    // gavin
    final Expense newExpense = new Expense("test", 500.3, Category.from("food"));

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(List.of());
    final Expense result = repository.save(newExpense);

    // then
    assertEquals(newExpense, result);
    verify(mockHandler).write(any(),
        argThat(list -> list.contains(newExpense) && list.size() == 1));
  }

  @Test
  void save_saves_expense_with_other_expenses() {
    // gavin mothing
    final Expense existingExpense = new Expense("test", 500.3, Category.from("food"));
    final Expense newExpense = new Expense("test", 200.0, Category.from("food"));

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(List.of(existingExpense));
    repository.save(newExpense);

    // then
    verify(mockHandler).write(any(),
        argThat(list -> list.contains(newExpense) && list.size() == 2));
  }

  @Test
  void existById_find_expense() {
    // gavin mothing
    final Expense expense1 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final Expense expense2 = new Expense(2, LocalDate.now(), null, "test", 250.0,
        Category.from("FRUITS"));

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(List.of(expense1, expense2));
    final boolean result = repository.existsById(1);

    // then
    assertTrue(result);
  }

  @Test
  void existById_do_not_find_expense() {
    // gavin mothing
    final Expense expense1 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final Expense expense2 = new Expense(2, LocalDate.now(), null, "test", 250.0,
        Category.from("FRUITS"));

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(List.of(expense1, expense2));
    final boolean result = repository.existsById(5);

    // then
    assertFalse(result);
  }

  @Test
  void find_by_id_do_not_found() {
    // gavin
    final Expense expense = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(List.of(expense));
    final Optional<Expense> notFound = repository.findById(9);

    // then
    assertTrue(notFound.isEmpty());
  }

  @Test
  void find_by_id_find_expense() {
    // gavin
    final Expense expense = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(List.of(expense));
    final Optional<Expense> found = repository.findById(1);

    // then
    assertTrue(found.isPresent());
  }

  @Test
  void findByMonth_find_expense() {
    // gavin
    final Expense expense1 = new Expense("test", 500.3, Category.from("food"));
    final Expense expense2 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final var expenses = List.of(expense1, expense2);

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(expenses);
    final List<Expense> result = repository.findByMonth(6);

    // then
    assertEquals(expenses, result);
  }

  @Test
  void findByMonth_do_not_find_expense() {
    // gavin
    final Expense expense1 = new Expense("test", 500.3, Category.from("food"));
    final Expense expense2 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final var expenses = List.of(expense1, expense2);

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(expenses);
    final List<Expense> result = repository.findByMonth(10);

    // then
    assertEquals(List.of(), result);
  }

  @Test
  void findByCategory_find_expense() {
    // gavin
    final Expense expense1 = new Expense("test", 500.3, Category.from("food"));
    final Expense expense2 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final var expenses = List.of(expense1, expense2);

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(expenses);
    final List<Expense> result = repository.findByCategory(Category.FOOD);

    // then
    assertEquals(expenses, result);
  }

  @Test
  void findByCategory_do_not_find_expense() {
    // gavin
    final Expense expense1 = new Expense("test", 500.3, Category.from("food"));
    final Expense expense2 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final var expenses = List.of(expense1, expense2);

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(expenses);
    final List<Expense> result = repository.findByCategory(Category.INTERNET_BILL);

    // then
    assertEquals(List.of(), result);
  }

  @Test
  void findByMonthAndCategory_find_expenses() {
    // gavin
    final Expense expense1 = new Expense("test", 500.3, Category.from("food"));
    final Expense expense2 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final var expenses = List.of(expense1, expense2);

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(expenses);
    final List<Expense> result = repository.findByMonthAndCategory(6, Category.FOOD);

    // then
    assertEquals(expenses, result);
  }

  @Test
  void findByMonthAndCategory_find_one_expense_from_existing_list() {
    // gavin
    final Expense expense1 = new Expense("test", 500.3, Category.from("INTERNET_BILL"));
    final Expense expense2 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final var expenses = List.of(expense1, expense2);

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(expenses);
    final List<Expense> result = repository.findByMonthAndCategory(6, Category.FOOD);

    // then
    assertEquals(List.of(expense2), result);
  }

  @Test
  void findByMonthAndCategory_do_not_find_expense() {
    // gavin
    final Expense expense1 = new Expense("test", 500.3, Category.from("food"));
    final Expense expense2 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final var expenses = List.of(expense1, expense2);

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(expenses);
    final List<Expense> result1 = repository.findByMonthAndCategory(9, Category.INTERNET_BILL);
    final List<Expense> result2 = repository.findByMonthAndCategory(9, Category.FOOD);

    // then
    assertEquals(List.of(), result1);
    assertEquals(List.of(), result2);
  }

  @Test
  void findAll_finds_all_expenses() {
    // gavin
    final Expense expense1 = new Expense("test", 500.3, Category.INTERNET_BILL);
    final Expense expense2 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final var expenses = List.of(expense1, expense2);

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(expenses);
    final List<Expense> result = repository.findAll();

    // then
    assertEquals(expenses, result);
  }

  @Test
  void summary_all_summaries_by_month() {
    // gavin
    final Expense expense1 = new Expense("test", 500.3, Category.INTERNET_BILL);
    final Expense expense2 = new Expense(1, LocalDate.now(), null, "test", 500.3,
        Category.from("food"));
    final var expenses = List.of(expense1, expense2);

    // when
    when(mockHandler.read(any(), eq(Expense.class))).thenReturn(expenses);
    final List<Expense> result = repository.findAll();

    // then
    assertEquals(expenses, result);
  }
}