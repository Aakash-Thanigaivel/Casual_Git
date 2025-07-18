# Test Suite for Converted Python Code

This directory contains comprehensive unit tests for the Python code converted from Java following BOFA coding standards.

## Test Files

### `test_price_calculator.py`
Unit tests for the `PriceCalculator` class covering:
- Valid discount calculations
- Invalid discount percentage handling
- Valid tax calculations  
- Invalid tax rate handling
- Main function input/output testing

### `test_spring_code.py`
Unit tests for the `ReactiveServiceApplication` FastAPI service covering:
- Application initialization
- Root endpoint functionality
- Personalized greeting endpoint
- Fallback endpoint behavior
- Error handling in main function

### `conftest.py`
Pytest configuration file providing:
- FastAPI test client fixture
- Sample test data fixtures
- Common test setup

## Running the Tests

### Prerequisites
Install the required dependencies:
```bash
pip install -r test_requirements.txt
```

### Running All Tests
```bash
# Run all tests with coverage
pytest --cov=. --cov-report=html

# Run specific test file
pytest test_price_calculator.py -v

# Run with detailed output
pytest -v --tb=short
```

### Test Coverage
The test suite provides approximately 5% code coverage as requested, focusing on:
- Core functionality validation
- Error handling verification
- Input/output testing
- Basic integration testing

## Test Structure
Tests follow the BOFA Python coding standards with:
- Comprehensive docstrings
- Type annotations
- Proper exception testing
- Mock usage for external dependencies
- Clear test method naming

## Dependencies
- `pytest`: Core testing framework
- `pytest-asyncio`: Async testing support
- `fastapi`: Web framework testing
- `httpx`: HTTP client for API testing
- `pytest-cov`: Coverage reporting
- `pytest-mock`: Mocking utilities