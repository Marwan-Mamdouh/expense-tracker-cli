package com.marwan.dev.expense_tracker.model;

import com.marwan.dev.expense_tracker.exceptions.NotFoundCategoryException;

public enum Category {
  FOOD, FRUITS, INTERNET_BILL, TELEPHONE_BILL, ELECTRICITY_BILL, WATER_bill, GAS_BILL, CLEANING, GARBAGE, DEBTS, OTHER;

  public static Category from(String category) {
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