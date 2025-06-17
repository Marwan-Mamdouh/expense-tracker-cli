package com.marwan.dev.expense_tracker.domain.expense.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an expense entry, including description, amount, category, and timestamps.
 * <p>
 * Used to store and manipulate expense records, supports serialization for JSON persistence.
 * </p>
 */
@Getter
@Setter
@EqualsAndHashCode
public class Expense {

  /**
   * The date the expense was created.
   */
  private final LocalDate createdAt;

  /**
   * The unique identifier of the expense.
   */
  private Integer id;

  /**
   * The date the expense was last updated.
   */
  private LocalDate updatedAt;

  /**
   * The textual description of the expense.
   */
  private String description;

  /**
   * The amount of money spent.
   */
  private Double amount;

  /**
   * The category to which this expense belongs.
   */
  private Category category;

  /**
   * Constructs a new {@code Expense} with full parameters. This constructor is typically used by
   * Jackson during deserialization.
   *
   * @param id          the ID of the expense
   * @param createdAt   the creation date
   * @param updatedAt   the last update date
   * @param description the description
   * @param amount      the amount spent
   * @param category    the category
   */
  @JsonCreator
  public Expense(@JsonProperty("expenseId") Integer id,
      @JsonProperty("createAt") LocalDate createdAt, @JsonProperty("updatedAt") LocalDate updatedAt,
      @JsonProperty("description") String description, @JsonProperty("amount") Double amount,
      @JsonProperty("category") Category category) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.description = description;
    this.amount = amount;
    this.category = category;
  }

  /**
   * Constructs a new {@code Expense} using minimal user input. Automatically assigns ID 0, current
   * date, and null update timestamp.
   *
   * @param description the description of the expense
   * @param amount      the amount spent
   * @param category    the category of the expense
   */
  public Expense(String description, Double amount, Category category) {
    this.id = 0;
    this.createdAt = LocalDate.now();
    this.updatedAt = null;
    this.description = description;
    this.amount = amount;
    this.category = category;
  }

  /**
   * Returns a formatted string representation of the expense. Useful for displaying expenses in a
   * CLI table format.
   *
   * @return a formatted string with ID, date, category, description, and amount
   */
  @Override
  public String toString() {
    return String.format("%-4d %-12s %-12s %-20s %10s", id,
        createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), category,
        truncateDescription(description, 20), String.format("$%.2f", amount));
  }

  /**
   * Truncates long descriptions to fit within the table display.
   *
   * @param desc      the original description
   * @param maxLength the maximum length allowed
   * @return the possibly truncated description
   */
  private String truncateDescription(String desc, int maxLength) {
    if (desc.length() <= maxLength) {
      return desc;
    }
    return desc.substring(0, maxLength - 3) + "...";
  }
}