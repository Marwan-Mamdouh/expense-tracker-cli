# Expense Tracker

A simple command-line expense tracking application built with Spring Boot and Spring Shell. This
project serves as a learning exercise for exploring Spring Boot's CLI capabilities while providing a
practical tool for personal expense management with category-based organization.

## Features

- **Add Expenses**: Record expenses with description, amount, and category
- **Categorized Expenses**: Organize expenses into predefined categories for better management
- **List Expenses**: View all expenses or filter by month with category display
- **Delete Expenses**: Remove expenses by ID
- **Expense Summary**: Get total expenses for all time or specific months
- **Persistent Storage**: Data is stored in JSON format in your home directory
- **Thread-Safe Operations**: Concurrent access protection with read-write locks

## Available Categories

The application supports the following expense categories:

- **FOOD** - Restaurant meals, takeout, groceries
- **FRUITS** - Fresh fruits and produce
- **INTERNET_BILL** - Internet service charges
- **TELEPHONE_BILL** - Phone service charges
- **ELECTRICITY_BILL** - Electrical utility bills
- **WATER_BILL** - Water utility bills
- **GAS_BILL** - Gas utility bills
- **CLEANING** - Cleaning supplies and services
- **GARBAGE** - Waste management fees
- **DEBTS** - Debt payments and loans
- **OTHER** - Miscellaneous expenses

note: `also know that you can write Categories capital or small case the cli will handle both`

## Requirements

- **Java 21** or higher
- **Maven 3.6+** (if building from source)

## Quick Start

### Option 1: Using Pre-built JAR

1. Download the latest JAR file from the repository
2. Run the application:
   ```bash
   java -jar expense-tracker-1.1-alpha.jar
   ```

### Option 2: Building from Source

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd expense-tracker
   ```

2. Build the project:
   ```bash
   ./mvnw clean package
   ```

3. Run the application:
   ```bash
   java -jar target/expense-tracker-1.1-alpha.jar
   ```

### Option 3: Native Executable (Advanced)

For the fastest startup time, you can build a native executable using GraalVM:

1. Install GraalVM with native-image support
2. Build the native executable:
   ```bash
   ./mvnw -Pnative native:compile
   ```
3. Run the native executable:
   ```bash
   ./target/expense-tracker
   ```

## Usage

The application runs as a command-line tool. Use the following commands with their arguments:

### Adding Expenses

All three parameters are required when adding an expense:

```bash
add --description "Grocery shopping" --amount 45.50 --category FOOD
# or using short forms
add -d "Internet monthly fee" -a 29.99 -c INTERNET_BILL
add -d "Coffee and pastry" -a 8.75 -c food
add -d "Loan payment" -a 350.00 -c DEBTS
```

### Listing Expenses

```bash
# List all expenses (shows ID, Date, Category, Description, Amount)
list

# List expenses for a specific month (1-12)
list --month 3
# or
list -m 3
```

**Example output:**

```
ID   Date         Category     Description          Amount
1    2024-03-15   FOOD         Grocery shopping     $45.50
2    2024-03-16   INTERNET_BILL Internet monthly fee $29.99
3    2024-03-17   FOOD         Coffee and pastry    $8.75
```

### Viewing Summaries

```bash
# Total of all expenses
summery

# Total for a specific month
summery --month 3
# or
summery -m 3
```

### Deleting Expenses

```bash
# Delete expense by ID
delete --id 5
# or
delete -i 5
```

### Other Commands

```bash
# Show help and available commands
help

# Exit the application
exit

# Show version information
version
```

## Data Storage

- Expenses are stored in `~/expense.json` in your home directory
- The file is created automatically when you add your first expense
- Data includes: ID, creation date, update date, description, amount, and category
- **Backup Recommendation**: Regularly backup your `~/expense.json` file to preserve your expense
  data

## Example Workflow

```bash
# Add some expenses
java -jar expense-tracker-1.1-alpha.jar add -d "Weekly groceries" -a 85.50 -c FOOD
java -jar expense-tracker-1.1-alpha.jar add -d "Electric bill" -a 120.00 -c ELECTRICITY_BILL
java -jar expense-tracker-1.1-alpha.jar add -d "Morning coffee" -a 4.25 -c FOOD

# View all expenses
java -jar expense-tracker-1.1-alpha.jar list

# Get total summary
java -jar expense-tracker-1.1-alpha.jar summery

# View expenses for current month only
java -jar expense-tracker-1.1-alpha.jar list -m 6

# Delete an expense by ID
java -jar expense-tracker-1.1-alpha.jar delete -i 2
```

## Development

### Project Structure

```
src/
├── main/java/com/marwan/dev/expense_tracker/
│   ├── commands/         # Spring Shell command definitions
│   │   └── ExpensesCommands.java
│   ├── config/          # Application configuration
│   │   └── Config.java
│   ├── exceptions/      # Custom exceptions
│   │   └── NotFoundCategoryException.java
│   ├── model/           # Data models and DTOs
│   │   ├── Category.java (enum)
│   │   ├── Expense.java
│   │   └── CreateExpenseDTO.java
│   ├── repository/      # Data access layer
│   │   └── ExpenseRepository.java
│   └── services/        # Business logic
│       ├── AddExpenseService.java
│       ├── DeleteExpenseService.java
│       ├── ListService.java
│       └── SummeryService.java
└── main/resources/
    └── application.yml  # Application configuration
```

### Building

```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Create JAR
./mvnw clean package
```

## Technical Details

- **Framework**: Spring Boot 3.4.6
- **CLI Framework**: Spring Shell 3.4.0
- **Data Format**: JSON with Jackson serialization
- **Build Tool**: Maven
- **Java Version**: 21
- **Native Compilation**: GraalVM support included
- **Concurrency**: Thread-safe with ReadWriteLock
- **Validation**: Input validation with Bean Validation

## Planned Features

Future enhancements being considered:

1. **Budget Management**: Set monthly budgets per category with overspending warnings
2. **Export Functionality**: Export expenses to CSV format with category filtering
3. **Expense Editing**: Update existing expenses
4. **Category Statistics**: Detailed breakdown of spending by category
5. **Date Range Filtering**: Filter expenses by custom date ranges

## Error Handling

The application handles various error scenarios:

[//]: # (- **Invalid Category**: Shows available categories when an invalid one is entered)
- **Missing Required Fields**: Prompts for required description, amount, and category
- **File Access Issues**: Graceful handling of file read/write permissions
- **Invalid IDs**: Proper error messages for non-existent expense IDs

## Troubleshooting

### Common Issues

**Application won't start**

- Ensure you have Java 21 or higher installed
- Check that the JAR file is not corrupted

**Data not persisting**

- Verify write permissions to your home directory
- Check if `~/expense.json` exists and is readable

**Category not found error**

- Use only the predefined categories listed above
- Category names are case-insensitive but must match exactly

**Commands not working**

- Use `help` to see available commands
- Ensure you're using the correct parameter names (--description, --amount, --category)
- All three parameters are required for the `add` command

## Contributing

This is primarily a learning project. Feel free to fork and experiment with your own modifications!

---

*This project was created as a learning exercise to explore Spring Boot's command-line capabilities,
category-based expense management, and practical application development.*
