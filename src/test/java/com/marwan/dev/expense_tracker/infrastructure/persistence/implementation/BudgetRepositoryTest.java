package com.marwan.dev.expense_tracker.infrastructure.persistence.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import com.marwan.dev.expense_tracker.domain.budget.repository.BudgetRepositoryI;
import com.marwan.dev.expense_tracker.infrastructure.persistence.util.JsonFileHandler;
import com.marwan.dev.expense_tracker.infrastructure.persistence.util.JsonFileHandlerI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BudgetRepositoryTest {

  private BudgetRepositoryI repository;
  private JsonFileHandlerI mockHandler;

  @BeforeEach
  protected void setUp() {
    mockHandler = mock(JsonFileHandler.class);
    repository = new BudgetRepository(new ReentrantReadWriteLock(), mockHandler);
  }

  @Test
  void save_saves_a_new_budget() {
    // gavin nothing

    // when
    when(mockHandler.read(any(), eq(Budget.class))).thenReturn(List.of());
    final Budget newBudget = new Budget(1000.0, 6, 2025);
    final Budget result = repository.save(newBudget);

    // then
    assertEquals(newBudget, result);
    verify(mockHandler).write(any(), argThat(list -> list.contains(newBudget) && list.size() == 1));
  }

  @Test
  void save_replaces_existing_budget() {
    // gavin
    final Budget existing = new Budget(500.0, 6, 2025);
    final Budget updated = new Budget(1000.0, 6, 2025);

    // when
    when(mockHandler.read(any(), eq(Budget.class))).thenReturn(List.of(existing));
    final Budget result = repository.save(updated);

    // then
    assertEquals(updated.getAmount(), result.getAmount());
    verify(mockHandler).write(any(), argThat(list -> list.contains(updated) && list.size() == 1));
  }

  @Test
  void find_by_month_and_year_return_budget() {
    // gavin
    final Budget budget = new Budget(1500.0, 7, 2025);

    // when
    when(mockHandler.read(any(), eq(Budget.class))).thenReturn(List.of(budget));
    final Optional<Budget> found = repository.findByMonthAndYear(7, 2025);

    // then
    assertTrue(found.isPresent());
    assertEquals(1500.0, found.get().getAmount());
  }

  @Test
  void find_by_month_and_year_not_found() {
    // gavin
    final Budget budget = new Budget(1500.0, 7, 2025);

    // when
    when(mockHandler.read(any(), eq(Budget.class))).thenReturn(List.of(budget));
    final Optional<Budget> notFound1 = repository.findByMonthAndYear(9, 2025);
    final Optional<Budget> notFound2 = repository.findByMonthAndYear(7, 2029);

    // then
    assertTrue(notFound1.isEmpty());
    assertTrue(notFound2.isEmpty());
  }

  @Test
  void find_by_year_return_budget() {
    // gavin
    final Budget budget1 = new Budget(1500.0, 7, 2025);
    final Budget budget2 = new Budget(1500.0, 7, 2024);

    // when
    when(mockHandler.read(any(), eq(Budget.class))).thenReturn(List.of(budget1, budget2));
    final List<Budget> found = repository.findByYear(2025);

    // then
    assertEquals(List.of(budget1), found);
  }

  @Test
  void find_by_year_not_found() {
    // gavin
    final Budget budget = new Budget(1500.0, 7, 2025);

    // when
    when(mockHandler.read(any(), eq(Budget.class))).thenReturn(List.of(budget));
    final List<Budget> notFound = repository.findByYear(2024);

    // then
    assertEquals(List.of(), notFound);
  }

  @Test
  void count_find_budget() {
    // gavin
    final Budget budget = new Budget(1500.0, 7, 2025);

    // when
    when(mockHandler.read(any(), eq(Budget.class))).thenReturn(List.of(budget));
    final int result = repository.count();

    // then
    assertEquals(1, result);
  }

  @Test
  void count_not_find_any_budget() {
    // gavin
    // when
    when(mockHandler.read(any(), eq(Budget.class))).thenReturn(List.of());
    final int result = repository.count();

    // then
    assertEquals(0, result);
  }

  @Test
  void delete_by_month_and_year() {
    // gavin
    final Budget b1 = new Budget(800.0, 8, 2025);
    final Budget b2 = new Budget(900.0, 9, 2025);

    // when
    when(mockHandler.read(any(), eq(Budget.class))).thenReturn(List.of(b1, b2));
    repository.deleteByMonthAndYear(8, 2025);

    // then
    verify(mockHandler).write(any(), argThat(list -> list.size() == 1));
  }

  @Test
  void delete_all() {
    // gavin
    // when
    repository.deleteAll();
    //then
    verify(mockHandler).write(any(), eq(List.of()));
  }
}