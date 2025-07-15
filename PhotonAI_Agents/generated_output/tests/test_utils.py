"""Test configuration and utilities for price_calculator tests.

This module provides test configuration, fixtures, and utility functions
for the price_calculator test suite.
"""

import unittest
from typing import List, Tuple, Any
import sys
import os

# Add the parent directory to the path so we can import price_calculator
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from price_calculator import PriceCalculator


class TestDataProvider:
    """Provides test data for various test scenarios."""
    
    @staticmethod
    def get_valid_discount_test_cases() -> List[Tuple[float, float, float]]:
        """Return test cases for valid discount calculations.
        
        Returns:
            List of tuples (price, discount_percentage, expected_result)
        """
        return [
            (100.0, 0.0, 100.0),      # No discount
            (100.0, 10.0, 90.0),      # 10% discount
            (100.0, 25.0, 75.0),      # 25% discount
            (100.0, 50.0, 50.0),      # 50% discount
            (100.0, 100.0, 0.0),      # 100% discount
            (250.0, 20.0, 200.0),     # Different price
            (99.99, 15.0, 84.9915),   # Decimal price
            (1000.0, 5.5, 945.0),     # Decimal discount
        ]
    
    @staticmethod
    def get_invalid_discount_test_cases() -> List[Tuple[float, float]]:
        """Return test cases for invalid discount calculations.
        
        Returns:
            List of tuples (price, invalid_discount_percentage)
        """
        return [
            (100.0, -1.0),      # Negative discount
            (100.0, -10.0),     # Large negative discount
            (100.0, 101.0),     # Over 100% discount
            (100.0, 150.0),     # Large over 100% discount
            (100.0, 1000.0),    # Extremely large discount
        ]
    
    @staticmethod
    def get_valid_tax_test_cases() -> List[Tuple[float, float, float]]:
        """Return test cases for valid tax calculations.
        
        Returns:
            List of tuples (price, tax_rate, expected_result)
        """
        return [
            (100.0, 0.0, 100.0),      # No tax
            (100.0, 0.05, 105.0),     # 5% tax
            (100.0, 0.10, 110.0),     # 10% tax
            (100.0, 0.0825, 108.25),  # 8.25% tax
            (100.0, 0.15, 115.0),     # 15% tax
            (250.0, 0.08, 270.0),     # Different price
            (99.99, 0.06, 105.9894),  # Decimal price
            (1000.0, 0.125, 1125.0),  # 12.5% tax
        ]
    
    @staticmethod
    def get_invalid_tax_test_cases() -> List[Tuple[float, float]]:
        """Return test cases for invalid tax calculations.
        
        Returns:
            List of tuples (price, invalid_tax_rate)
        """
        return [
            (100.0, -0.01),     # Small negative tax
            (100.0, -0.1),      # Negative tax
            (100.0, -1.0),      # Large negative tax
        ]


class BaseTestCase(unittest.TestCase):
    """Base test case class with common utilities."""
    
    def setUp(self) -> None:
        """Set up common test fixtures."""
        self.calculator = PriceCalculator()
        self.precision = 6  # Default precision for float comparisons
    
    def assertAlmostEqualList(self, list1: List[float], list2: List[float], 
                             places: int = None) -> None:
        """Assert that two lists of floats are almost equal.
        
        Args:
            list1: First list of floats
            list2: Second list of floats
            places: Number of decimal places for comparison
        """
        places = places or self.precision
        self.assertEqual(len(list1), len(list2), "Lists have different lengths")
        
        for i, (val1, val2) in enumerate(zip(list1, list2)):
            self.assertAlmostEqual(val1, val2, places=places, 
                                 msg=f"Values differ at index {i}")
    
    def run_test_cases(self, test_method, test_cases: List[Tuple], 
                      expected_exception=None) -> None:
        """Run a series of test cases for a given method.
        
        Args:
            test_method: The method to test
            test_cases: List of test case tuples
            expected_exception: Exception type expected to be raised
        """
        for i, test_case in enumerate(test_cases):
            with self.subTest(case=i, inputs=test_case):
                if expected_exception:
                    with self.assertRaises(expected_exception):
                        test_method(*test_case)
                else:
                    # Last element is expected result for valid cases
                    inputs = test_case[:-1]
                    expected = test_case[-1]
                    result = test_method(*inputs)
                    self.assertAlmostEqual(result, expected, places=self.precision)


class TestSuiteRunner:
    """Utility class for running comprehensive test suites."""
    
    @staticmethod
    def run_all_tests() -> None:
        """Run all test modules in the test suite."""
        # Discover and run all tests
        loader = unittest.TestLoader()
        start_dir = os.path.dirname(__file__)
        suite = loader.discover(start_dir, pattern='test_*.py')
        
        runner = unittest.TextTestRunner(verbosity=2)
        result = runner.run(suite)
        
        # Print summary
        print(f"\nTest Summary:")
        print(f"Tests run: {result.testsRun}")
        print(f"Failures: {len(result.failures)}")
        print(f"Errors: {len(result.errors)}")
        print(f"Success rate: {((result.testsRun - len(result.failures) - len(result.errors)) / result.testsRun * 100):.1f}%")


if __name__ == '__main__':
    TestSuiteRunner.run_all_tests()