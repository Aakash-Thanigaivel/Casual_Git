"""Test runner and configuration for the complete test suite.

This module provides a comprehensive test runner that executes all test cases
for the price_calculator module with proper reporting and coverage analysis.
"""

import unittest
import sys
import os
from typing import Dict, Any
import time

# Add the parent directory to the path
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from test_utils import TestDataProvider, BaseTestCase


class TestPriceCalculatorComprehensive(BaseTestCase):
    """Comprehensive test suite using data-driven testing approach."""
    
    def test_all_valid_discount_scenarios(self) -> None:
        """Test all valid discount calculation scenarios."""
        test_cases = TestDataProvider.get_valid_discount_test_cases()
        self.run_test_cases(
            self.calculator.calculate_discounted_price,
            test_cases
        )
    
    def test_all_invalid_discount_scenarios(self) -> None:
        """Test all invalid discount calculation scenarios."""
        test_cases = TestDataProvider.get_invalid_discount_test_cases()
        
        for price, invalid_discount in test_cases:
            with self.subTest(price=price, discount=invalid_discount):
                with self.assertRaises(ValueError):
                    self.calculator.calculate_discounted_price(price, invalid_discount)
    
    def test_all_valid_tax_scenarios(self) -> None:
        """Test all valid tax calculation scenarios."""
        test_cases = TestDataProvider.get_valid_tax_test_cases()
        self.run_test_cases(
            self.calculator.calculate_final_price_with_tax,
            test_cases
        )
    
    def test_all_invalid_tax_scenarios(self) -> None:
        """Test all invalid tax calculation scenarios."""
        test_cases = TestDataProvider.get_invalid_tax_test_cases()
        
        for price, invalid_tax in test_cases:
            with self.subTest(price=price, tax=invalid_tax):
                with self.assertRaises(ValueError):
                    self.calculator.calculate_final_price_with_tax(price, invalid_tax)


class TestRunner:
    """Advanced test runner with reporting capabilities."""
    
    def __init__(self):
        self.start_time = None
        self.end_time = None
        self.results = {}
    
    def run_test_suite(self) -> Dict[str, Any]:
        """Run the complete test suite and return results."""
        self.start_time = time.time()
        
        # Create test suite
        loader = unittest.TestLoader()
        suite = unittest.TestSuite()
        
        # Add all test classes
        test_classes = [
            TestPriceCalculatorComprehensive,
        ]
        
        for test_class in test_classes:
            tests = loader.loadTestsFromTestCase(test_class)
            suite.addTests(tests)
        
        # Run tests
        runner = unittest.TextTestRunner(
            verbosity=2,
            stream=sys.stdout,
            buffer=True
        )
        
        result = runner.run(suite)
        
        self.end_time = time.time()
        
        # Compile results
        self.results = {
            'tests_run': result.testsRun,
            'failures': len(result.failures),
            'errors': len(result.errors),
            'skipped': len(result.skipped) if hasattr(result, 'skipped') else 0,
            'success_rate': self._calculate_success_rate(result),
            'execution_time': self.end_time - self.start_time,
            'failure_details': [str(failure[1]) for failure in result.failures],
            'error_details': [str(error[1]) for error in result.errors]
        }
        
        return self.results
    
    def _calculate_success_rate(self, result) -> float:
        """Calculate the success rate of test execution."""
        if result.testsRun == 0:
            return 0.0
        
        successful = result.testsRun - len(result.failures) - len(result.errors)
        return (successful / result.testsRun) * 100
    
    def print_summary_report(self) -> None:
        """Print a detailed summary report of test results."""
        if not self.results:
            print("No test results available. Run tests first.")
            return
        
        print("\n" + "="*60)
        print("PRICE CALCULATOR TEST SUITE SUMMARY REPORT")
        print("="*60)
        
        print(f"Total Tests Run: {self.results['tests_run']}")
        print(f"Successful: {self.results['tests_run'] - self.results['failures'] - self.results['errors']}")
        print(f"Failures: {self.results['failures']}")
        print(f"Errors: {self.results['errors']}")
        print(f"Skipped: {self.results['skipped']}")
        print(f"Success Rate: {self.results['success_rate']:.1f}%")
        print(f"Execution Time: {self.results['execution_time']:.3f} seconds")
        
        if self.results['failures']:
            print(f"\nFAILURE DETAILS:")
            for i, failure in enumerate(self.results['failure_details'], 1):
                print(f"  {i}. {failure}")
        
        if self.results['errors']:
            print(f"\nERROR DETAILS:")
            for i, error in enumerate(self.results['error_details'], 1):
                print(f"  {i}. {error}")
        
        print("\n" + "="*60)
        
        # Determine overall status
        if self.results['success_rate'] == 100.0:
            print("✅ ALL TESTS PASSED - CODE READY FOR PRODUCTION")
        elif self.results['success_rate'] >= 95.0:
            print("⚠️  MOSTLY PASSING - MINOR ISSUES TO ADDRESS")
        else:
            print("❌ SIGNIFICANT ISSUES FOUND - REQUIRES ATTENTION")
        
        print("="*60)


def main():
    """Main entry point for running the comprehensive test suite."""
    print("Starting Price Calculator Comprehensive Test Suite...")
    
    runner = TestRunner()
    results = runner.run_test_suite()
    runner.print_summary_report()
    
    # Exit with appropriate code
    if results['success_rate'] == 100.0:
        sys.exit(0)  # Success
    else:
        sys.exit(1)  # Failure


if __name__ == '__main__':
    main()