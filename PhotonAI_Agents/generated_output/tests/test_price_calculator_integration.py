"""Integration tests for price_calculator module.

This module contains integration tests that test the price calculator
functionality end-to-end, including edge cases and error scenarios.
"""

import unittest
from unittest.mock import patch, StringIO
import sys
import io
from contextlib import redirect_stdout, redirect_stderr

from price_calculator import PriceCalculator, main


class TestPriceCalculatorIntegration(unittest.TestCase):
    """Integration test cases for PriceCalculator functionality."""

    def setUp(self) -> None:
        """Sets up test fixtures before each test method."""
        self.calculator = PriceCalculator()

    def test_complete_price_calculation_workflow(self) -> None:
        """Tests complete price calculation workflow with realistic values."""
        # Test realistic e-commerce scenario
        original_price = 299.99
        discount_percentage = 15.0
        tax_rate = 0.08  # 8% tax
        
        # Calculate discounted price
        discounted_price = self.calculator.calculate_discounted_price(
            original_price, discount_percentage
        )
        expected_discounted = 299.99 * 0.85  # 254.9915
        self.assertAlmostEqual(discounted_price, expected_discounted, places=2)
        
        # Calculate final price with tax
        final_price = self.calculator.calculate_final_price_with_tax(
            discounted_price, tax_rate
        )
        expected_final = discounted_price * 1.08
        self.assertAlmostEqual(final_price, expected_final, places=2)

    def test_boundary_value_analysis(self) -> None:
        """Tests boundary values for discount percentages and tax rates."""
        test_price = 100.0
        
        # Test boundary values for discount percentage
        boundary_discounts = [0.0, 0.1, 99.9, 100.0]
        for discount in boundary_discounts:
            with self.subTest(discount=discount):
                result = self.calculator.calculate_discounted_price(test_price, discount)
                expected = test_price * (1 - discount / 100)
                self.assertAlmostEqual(result, expected, places=2)
        
        # Test boundary values for tax rate
        boundary_taxes = [0.0, 0.001, 0.5, 1.0, 2.0]
        for tax in boundary_taxes:
            with self.subTest(tax=tax):
                result = self.calculator.calculate_final_price_with_tax(test_price, tax)
                expected = test_price * (1 + tax)
                self.assertAlmostEqual(result, expected, places=2)

    def test_precision_and_rounding(self) -> None:
        """Tests precision handling and rounding behavior."""
        # Test with values that might cause floating point precision issues
        test_cases = [
            (99.99, 33.33),  # Repeating decimal
            (0.01, 50.0),    # Very small price
            (999999.99, 0.01),  # Large price, small discount
        ]
        
        for price, discount in test_cases:
            with self.subTest(price=price, discount=discount):
                result = self.calculator.calculate_discounted_price(price, discount)
                # Ensure result is reasonable and not affected by floating point errors
                self.assertGreaterEqual(result, 0)
                self.assertLessEqual(result, price)

    @patch('builtins.input', side_effect=['150.50', '20', '0.075'])
    def test_interactive_calculator_realistic_scenario(self, mock_input) -> None:
        """Tests interactive calculator with realistic input values."""
        with patch('sys.stdout', new=StringIO()) as fake_out:
            self.calculator.run_interactive_calculator()
            output = fake_out.getvalue()
            
            # Verify expected calculations appear in output
            self.assertIn("Price after 20.00% discount: ₹120.40", output)
            self.assertIn("Final price with tax: ₹129.43", output)

    def test_error_propagation_in_interactive_mode(self) -> None:
        """Tests error handling and propagation in interactive mode."""
        error_scenarios = [
            (['100', '150', '0.05'], "Discount percentage must be between 0 and 100"),
            (['100', '10', '-0.05'], "Tax rate cannot be negative"),
            (['abc'], "Unexpected error:"),
            (['100', 'xyz'], "Unexpected error:"),
        ]
        
        for inputs, expected_error in error_scenarios:
            with self.subTest(inputs=inputs):
                with patch('builtins.input', side_effect=inputs):
                    with patch('sys.stdout', new=StringIO()) as fake_out:
                        self.calculator.run_interactive_calculator()
                        output = fake_out.getvalue()
                        self.assertIn(expected_error, output)

    def test_currency_formatting_consistency(self) -> None:
        """Tests that currency formatting is consistent across different values."""
        test_values = [0.01, 1.0, 10.5, 100.99, 1000.0, 9999.99]
        
        for value in test_values:
            with self.subTest(value=value):
                with patch('builtins.input', side_effect=[str(value), '0', '0']):
                    with patch('sys.stdout', new=StringIO()) as fake_out:
                        self.calculator.run_interactive_calculator()
                        output = fake_out.getvalue()
                        
                        # Check that currency symbol and formatting are present
                        self.assertIn('₹', output)
                        self.assertIn(f'{value:.2f}', output)


class TestMainFunctionIntegration(unittest.TestCase):
    """Integration tests for the main function."""

    @patch('price_calculator.PriceCalculator')
    def test_main_function_integration(self, mock_calculator_class) -> None:
        """Tests main function creates and runs calculator properly."""
        mock_instance = mock_calculator_class.return_value
        
        # Call main function
        main()
        
        # Verify calculator was created and run method was called
        mock_calculator_class.assert_called_once()
        mock_instance.run_interactive_calculator.assert_called_once()

    @patch('builtins.input', side_effect=['100', '10', '0.05'])
    def test_main_function_end_to_end(self, mock_input) -> None:
        """Tests main function end-to-end execution."""
        with patch('sys.stdout', new=StringIO()) as fake_out:
            main()
            output = fake_out.getvalue()
            
            # Verify the complete workflow executed
            self.assertIn("Enter the original price:", output)
            self.assertIn("Price after 10.00% discount: ₹90.00", output)
            self.assertIn("Final price with tax: ₹94.50", output)


class TestStaticMethodBehavior(unittest.TestCase):
    """Tests for static method behavior and class independence."""

    def test_static_methods_class_independence(self) -> None:
        """Tests that static methods work independently of class instances."""
        # Test calling methods without creating instance
        result1 = PriceCalculator.calculate_discounted_price(100, 10)
        result2 = PriceCalculator.calculate_final_price_with_tax(90, 0.05)
        
        self.assertEqual(result1, 90.0)
        self.assertEqual(result2, 94.5)
        
        # Test that multiple instances don't interfere
        calc1 = PriceCalculator()
        calc2 = PriceCalculator()
        
        result3 = calc1.calculate_discounted_price(200, 25)
        result4 = calc2.calculate_final_price_with_tax(150, 0.1)
        
        self.assertEqual(result3, 150.0)
        self.assertEqual(result4, 165.0)

    def test_method_consistency_across_instances(self) -> None:
        """Tests that methods return consistent results across different instances."""
        calc1 = PriceCalculator()
        calc2 = PriceCalculator()
        
        test_cases = [
            (100, 10, 0.05),
            (250.75, 15.5, 0.08),
            (999.99, 50, 0.12),
        ]
        
        for price, discount, tax in test_cases:
            with self.subTest(price=price, discount=discount, tax=tax):
                # Test discount calculation consistency
                result1 = calc1.calculate_discounted_price(price, discount)
                result2 = calc2.calculate_discounted_price(price, discount)
                self.assertEqual(result1, result2)
                
                # Test tax calculation consistency
                result3 = calc1.calculate_final_price_with_tax(price, tax)
                result4 = calc2.calculate_final_price_with_tax(price, tax)
                self.assertEqual(result3, result4)


if __name__ == "__main__":
    unittest.main()