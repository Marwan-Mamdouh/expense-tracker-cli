package com.marwan.dev.expense_tracker.services;

import com.marwan.dev.expense_tracker.exceptions.NotFoundCategoryException;
import com.marwan.dev.expense_tracker.model.Category;
import com.marwan.dev.expense_tracker.model.CreateExpenseDTO;
import com.marwan.dev.expense_tracker.model.Expense;
import com.marwan.dev.expense_tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class AddExpenseService implements CommandInterface<CreateExpenseDTO, Expense> {

  private final ExpenseRepository expenseRepository;

  public AddExpenseService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  @Override
  public Expense execute(CreateExpenseDTO input) {
    return expenseRepository.save(
        new Expense(input.description(), input.amount(), handleString(input.category())));
  }

  private Category handleString(String category) {
    return switch (category.toUpperCase()) {
      case "FOOD" -> Category.FOOD;
      case "FRUITS" -> Category.FRUITS;
      case "INTERNET_BILL" -> Category.INTERNET_BILL;
      case "TELEPHONE_BILL" -> Category.TELEPHONE_BILL;
      case "ELECTRICITY_BILL" -> Category.ELECTRICITY_BILL;
      case "WATER_BILL" -> Category.WATER_bill;
      case "GAS_BILL" -> Category.GAS_BILL;
      case "CLEANING" -> Category.CLEANING;
      case "GARBAGE" -> Category.GARBAGE;
      case "DEBTS" -> Category.DEBTS;
      case "OTHER" -> Category.OTHER;
      default -> throw new NotFoundCategoryException(category);
    };
  }
}