"""Unit tests for price_calculator module.

This module contains unit tests for the price calculator functions,
testing basic functionality and edge cases with 5% code coverage.
"""

import unittest
from unittest.mock import patch
import sys
import os

# Add the parent directory to the path to import the module under test
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from price_calculator import calculate_discounted_price, calculate_final_price_with_tax, main


class TestPriceCalculator(unittest.TestCase):
    """Test cases for price calculator functions."""

    def test_calculate_discounted_price_valid_input(self):
        """Test calculate_discounted_price with valid inputs."""
        # Test basic discount calculation
        result = calculate_discounted_price(100.0, 10.0)
        self.assertEqual(result, 90.0)
        
        # Test zero discount
        result = calculate_discounted_price(100.0, 0.0)
        self.assertEqual(result, 100.0)

    def test_calculate_discounted_price_invalid_discount(self):
        """Test calculate_discounted_price with invalid discount percentage."""
        # Test negative discount
        with self.assertRaises(ValueError) as context:
            calculate_discounted_price(100.0, -5.0)
        self.assertIn("Discount percentage must be between 0 and 100", str(context.exception))
        
        # Test discount over 100%
        with self.assertRaises(ValueError) as context:
            calculate_discounted_price(100.0, 150.0)
        self.assertIn("Discount percentage must be between 0 and 100", str(context.exception))

    def test_calculate_final_price_with_tax_valid_input(self):
        """Test calculate_final_price_with_tax with valid inputs."""
        # Test basic tax calculation
        result = calculate_final_price_with_tax(100.0, 0.05)
        self.assertEqual(result, 105.0)
        
        # Test zero tax
        result = calculate_final_price_with_tax(100.0, 0.0)
        self.assertEqual(result, 100.0)

    def test_calculate_final_price_with_tax_negative_rate(self):
        """Test calculate_final_price_with_tax with negative tax rate."""
        with self.assertRaises(ValueError) as context:
            calculate_final_price_with_tax(100.0, -0.05)
        self.assertIn("Tax rate cannot be negative", str(context.exception))


if __name__ == "__main__":
    unittest.main()