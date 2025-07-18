"""Unit tests for price_calculator module.

This module contains comprehensive unit tests for the PriceCalculator class
and its methods, ensuring proper functionality and error handling.
"""

from __future__ import annotations

import unittest
from unittest.mock import patch
from io import StringIO
import sys

from price_calculator import PriceCalculator, main


class TestPriceCalculator(unittest.TestCase):
    """Test cases for PriceCalculator class."""

    def test_calculate_discounted_price_valid_discount(self) -> None:
        """Test calculate_discounted_price with valid discount percentage."""
        # Test with 10% discount on $100
        result = PriceCalculator.calculate_discounted_price(100.0, 10.0)
        self.assertEqual(result, 90.0)
        
        # Test with 0% discount
        result = PriceCalculator.calculate_discounted_price(100.0, 0.0)
        self.assertEqual(result, 100.0)

    def test_calculate_discounted_price_invalid_discount(self) -> None:
        """Test calculate_discounted_price with invalid discount percentage."""
        # Test negative discount
        with self.assertRaises(ValueError) as context:
            PriceCalculator.calculate_discounted_price(100.0, -5.0)
        self.assertIn("Discount percentage must be between 0 and 100", str(context.exception))
        
        # Test discount over 100%
        with self.assertRaises(ValueError) as context:
            PriceCalculator.calculate_discounted_price(100.0, 150.0)
        self.assertIn("Discount percentage must be between 0 and 100", str(context.exception))

    def test_calculate_final_price_with_tax_valid_rate(self) -> None:
        """Test calculate_final_price_with_tax with valid tax rate."""
        # Test with 5% tax on $100
        result = PriceCalculator.calculate_final_price_with_tax(100.0, 0.05)
        self.assertEqual(result, 105.0)
        
        # Test with 0% tax
        result = PriceCalculator.calculate_final_price_with_tax(100.0, 0.0)
        self.assertEqual(result, 100.0)

    def test_calculate_final_price_with_tax_invalid_rate(self) -> None:
        """Test calculate_final_price_with_tax with invalid tax rate."""
        # Test negative tax rate
        with self.assertRaises(ValueError) as context:
            PriceCalculator.calculate_final_price_with_tax(100.0, -0.05)
        self.assertIn("Tax rate cannot be negative", str(context.exception))


class TestMainFunction(unittest.TestCase):
    """Test cases for main function."""

    @patch('builtins.input', side_effect=['100', '10', '0.05'])
    @patch('sys.stdout', new_callable=StringIO)
    def test_main_function_valid_inputs(self, mock_stdout, mock_input) -> None:
        """Test main function with valid inputs."""
        main()
        output = mock_stdout.getvalue()
        self.assertIn("Price after 10.00% discount: ₹90.00", output)
        self.assertIn("Final price with tax: ₹94.50", output)

    @patch('builtins.input', side_effect=['invalid'])
    @patch('sys.stdout', new_callable=StringIO)
    def test_main_function_invalid_input(self, mock_stdout, mock_input) -> None:
        """Test main function with invalid input."""
        main()
        output = mock_stdout.getvalue()
        self.assertIn("Error:", output)


if __name__ == '__main__':
    unittest.main()