# Expense Tracker

A simple command-line expense tracking application built with Spring Boot and Spring Shell. This project serves as a learning exercise for exploring Spring Boot's CLI capabilities while providing a practical tool for personal expense management.

## Features

- **Add Expenses**: Record expenses with description and amount
- **List Expenses**: View all expenses or filter by month
- **Delete Expenses**: Remove expenses by ID
- **Expense Summary**: Get total expenses for all time or specific months
- **Persistent Storage**: Data is stored in JSON format in your home directory

## Requirements

- **Java 21** or higher
- **Maven 3.6+** (if building from source)

## Quick Start

### Option 1: Using Pre-built JAR

1. Download the latest JAR file from the repository
2. Run the application:
   ```bash
   java -jar expense-tracker-1.0-alpha.jar
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
   java -jar target/expense-tracker-1.0-alpha.jar
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

The application not provides an interactive shell so run it with the following commands as a command line args:

### Adding Expenses

```bash
add --description "Grocery shopping" --amount 45.50
# or using short forms
add -d "Coffee" -a 5.25
```

### Listing Expenses

```bash
# List all expenses
list

# List expenses for a specific month (1-12)
list --month 3
# or
list -m 3
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
# Show help
help

# Exit the application
exit

# Show version
vserion
```

## Data Storage

- Expenses are stored in `~/expense.json` in your home directory
- The file is created automatically when you add your first expense
- **Backup Recommendation**: Regularly backup your `~/expense.json` file to preserve your expense data

## Development

### Project Structure

```
src/
├── main/java/com/marwan/dev/expense_tracker/
│   ├── commands/         # Spring Shell command definitions
│   ├── config/          # Application configuration
│   ├── model/           # Data models and DTOs
│   ├── repository/      # Data access layer
│   └── services/        # Business logic
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

## Planned Features

Future enhancements being considered:

1. **Expense Categories**: Add categories and filtering capabilities
2. **Budget Management**: Set monthly budgets with overspending warnings
3. **Export Functionality**: Export expenses to CSV format

## Technical Details

- **Framework**: Spring Boot 3.4.6
- **CLI Framework**: Spring Shell 3.4.0
- **Data Format**: JSON
- **Build Tool**: Maven
- **Java Version**: 21
- **Native Compilation**: GraalVM support included

## Troubleshooting

### Common Issues

**Application won't start**
- Ensure you have Java 21 or higher installed
- Check that the JAR file is not corrupted

**Data not persisting**
- Verify write permissions to your home directory
- Check if `~/expense.json` exists and is readable

**Commands not working**
- Use `help` to see available commands
- Ensure you're using the correct parameter names (--description, --amount, etc.)

## Contributing

This is primarily a learning project. Feel free to fork and experiment with your own modifications!

---

*This project was created as a learning exercise to explore Spring Boot's command-line capabilities and practical application development.*
