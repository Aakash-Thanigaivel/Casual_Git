"""Unit tests for the price_calculator module.

This module contains comprehensive unit tests for the PriceCalculator class
and its methods, ensuring proper functionality and error handling.
"""

import unittest
import sys
from io import StringIO
from unittest.mock import patch, MagicMock
from price_calculator import PriceCalculator


class TestPriceCalculator(unittest.TestCase):
    """Test cases for the PriceCalculator class."""

    def setUp(self) -> None:
        """Set up test fixtures before each test method."""
        self.calculator = PriceCalculator()

    def test_calculate_discounted_price_valid_input(self) -> None:
        """Test calculate_discounted_price with valid inputs."""
        # Test normal discount calculation
        result = PriceCalculator.calculate_discounted_price(100.0, 10.0)
        self.assertEqual(result, 90.0)
        
        # Test zero discount
        result = PriceCalculator.calculate_discounted_price(100.0, 0.0)
        self.assertEqual(result, 100.0)
        
        # Test maximum discount
        result = PriceCalculator.calculate_discounted_price(100.0, 100.0)
        self.assertEqual(result, 0.0)

    def test_calculate_discounted_price_invalid_discount(self) -> None:
        """Test calculate_discounted_price with invalid discount percentages."""
        # Test negative discount
        with self.assertRaises(ValueError) as context:
            PriceCalculator.calculate_discounted_price(100.0, -5.0)
        self.assertEqual(str(context.exception), "Discount percentage must be between 0 and 100.")
        
        # Test discount over 100%
        with self.assertRaises(ValueError) as context:
            PriceCalculator.calculate_discounted_price(100.0, 105.0)
        self.assertEqual(str(context.exception), "Discount percentage must be between 0 and 100.")

    def test_calculate_final_price_with_tax_valid_input(self) -> None:
        """Test calculate_final_price_with_tax with valid inputs."""
        # Test normal tax calculation
        result = PriceCalculator.calculate_final_price_with_tax(100.0, 0.05)
        self.assertEqual(result, 105.0)
        
        # Test zero tax
        result = PriceCalculator.calculate_final_price_with_tax(100.0, 0.0)
        self.assertEqual(result, 100.0)

    def test_calculate_final_price_with_tax_invalid_tax(self) -> None:
        """Test calculate_final_price_with_tax with invalid tax rates."""
        # Test negative tax rate
        with self.assertRaises(ValueError) as context:
            PriceCalculator.calculate_final_price_with_tax(100.0, -0.05)
        self.assertEqual(str(context.exception), "Tax rate cannot be negative.")

    @patch('builtins.input')
    @patch('builtins.print')
    def test_run_interactive_calculator_success(self, mock_print: MagicMock, mock_input: MagicMock) -> None:
        """Test successful execution of interactive calculator."""
        # Mock user inputs
        mock_input.side_effect = ['100.0', '10.0', '0.05']
        
        # Run the interactive calculator
        self.calculator.run_interactive_calculator()
        
        # Verify print calls
        mock_print.assert_any_call("Price after 10.00% discount: ₹90.00")
        mock_print.assert_any_call("Final price with tax: ₹94.50")

    @patch('builtins.input')
    @patch('sys.stderr', new_callable=StringIO)
    def test_run_interactive_calculator_value_error(self, mock_stderr: StringIO, mock_input: MagicMock) -> None:
        """Test interactive calculator with invalid input causing ValueError."""
        # Mock invalid input that causes ValueError
        mock_input.side_effect = ['100.0', '150.0']  # Invalid discount percentage
        
        # Run the interactive calculator
        self.calculator.run_interactive_calculator()
        
        # Check that error was written to stderr
        error_output = mock_stderr.getvalue()
        self.assertIn("Error: Discount percentage must be between 0 and 100.", error_output)


if __name__ == '__main__':
    unittest.main()