"""Unit tests for the price_calculator module.

This module contains comprehensive unit tests for the PriceCalculator class
and main function, following Google Python Style Guidelines.
"""

import unittest
from unittest.mock import patch, MagicMock
import sys
from io import StringIO

# Import the module under test
from price_calculator import PriceCalculator, main


class TestPriceCalculator(unittest.TestCase):
    """Test cases for the PriceCalculator class."""

    def test_calculate_discounted_price_valid_discount(self):
        """Test calculate_discounted_price with valid discount percentage."""
        result = PriceCalculator.calculate_discounted_price(100.0, 10.0)
        self.assertEqual(result, 90.0)
        
        result = PriceCalculator.calculate_discounted_price(50.0, 25.0)
        self.assertEqual(result, 37.5)

    def test_calculate_discounted_price_zero_discount(self):
        """Test calculate_discounted_price with zero discount."""
        result = PriceCalculator.calculate_discounted_price(100.0, 0.0)
        self.assertEqual(result, 100.0)

    def test_calculate_discounted_price_full_discount(self):
        """Test calculate_discounted_price with 100% discount."""
        result = PriceCalculator.calculate_discounted_price(100.0, 100.0)
        self.assertEqual(result, 0.0)

    def test_calculate_discounted_price_negative_discount(self):
        """Test calculate_discounted_price raises ValueError for negative discount."""
        with self.assertRaises(ValueError) as context:
            PriceCalculator.calculate_discounted_price(100.0, -5.0)
        self.assertEqual(str(context.exception), "Discount percentage must be between 0 and 100.")

    def test_calculate_discounted_price_excessive_discount(self):
        """Test calculate_discounted_price raises ValueError for discount > 100."""
        with self.assertRaises(ValueError) as context:
            PriceCalculator.calculate_discounted_price(100.0, 150.0)
        self.assertEqual(str(context.exception), "Discount percentage must be between 0 and 100.")

    def test_calculate_final_price_with_tax_valid_rate(self):
        """Test calculate_final_price_with_tax with valid tax rate."""
        result = PriceCalculator.calculate_final_price_with_tax(100.0, 0.05)
        self.assertEqual(result, 105.0)
        
        result = PriceCalculator.calculate_final_price_with_tax(50.0, 0.10)
        self.assertEqual(result, 55.0)

    def test_calculate_final_price_with_tax_zero_rate(self):
        """Test calculate_final_price_with_tax with zero tax rate."""
        result = PriceCalculator.calculate_final_price_with_tax(100.0, 0.0)
        self.assertEqual(result, 100.0)

    def test_calculate_final_price_with_tax_negative_rate(self):
        """Test calculate_final_price_with_tax raises ValueError for negative tax rate."""
        with self.assertRaises(ValueError) as context:
            PriceCalculator.calculate_final_price_with_tax(100.0, -0.05)
        self.assertEqual(str(context.exception), "Tax rate cannot be negative.")


class TestMainFunction(unittest.TestCase):
    """Test cases for the main function."""

    @patch('builtins.input')
    @patch('builtins.print')
    def test_main_successful_execution(self, mock_print, mock_input):
        """Test main function with valid inputs."""
        mock_input.side_effect = ['100.0', '10.0', '0.05']
        
        main()
        
        # Verify the expected print calls
        expected_calls = [
            unittest.mock.call('Price after 10.00% discount: ₹90.00'),
            unittest.mock.call('Final price with tax: ₹94.50')
        ]
        mock_print.assert_has_calls(expected_calls)

    @patch('builtins.input')
    @patch('builtins.print')
    def test_main_invalid_discount_percentage(self, mock_print, mock_input):
        """Test main function with invalid discount percentage."""
        mock_input.side_effect = ['100.0', '150.0', '0.05']
        
        main()
        
        # Verify error message is printed
        mock_print.assert_called_with('Error: Discount percentage must be between 0 and 100.')

    @patch('builtins.input')
    @patch('builtins.print')
    def test_main_invalid_tax_rate(self, mock_print, mock_input):
        """Test main function with invalid tax rate."""
        mock_input.side_effect = ['100.0', '10.0', '-0.05']
        
        main()
        
        # Verify error message is printed
        mock_print.assert_called_with('Error: Tax rate cannot be negative.')

    @patch('builtins.input')
    @patch('builtins.print')
    def test_main_invalid_input_format(self, mock_print, mock_input):
        """Test main function with non-numeric input."""
        mock_input.side_effect = ['invalid', '10.0', '0.05']
        
        main()
        
        # Verify error message is printed for ValueError
        self.assertTrue(any('Error:' in str(call) for call in mock_print.call_args_list))


if __name__ == '__main__':
    unittest.main()