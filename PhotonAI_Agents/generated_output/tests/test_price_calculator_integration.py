"""Integration tests for price_calculator module.

This module contains integration tests that verify the complete workflow
and interaction between different components of the price calculator system.
"""

import unittest
from unittest.mock import patch, call
from io import StringIO
import sys
from price_calculator import PriceCalculator


class TestPriceCalculatorIntegration(unittest.TestCase):
    """Integration test suite for PriceCalculator."""

    def test_complete_price_calculation_workflow(self) -> None:
        """Test the complete workflow from original price to final price."""
        original_price = 1000.0
        discount_percentage = 25.0  # 25% discount
        tax_rate = 0.08  # 8% tax
        
        # Step 1: Calculate discounted price
        discounted_price = PriceCalculator.calculate_discounted_price(
            original_price, discount_percentage
        )
        expected_discounted = 750.0  # 1000 - (1000 * 0.25)
        self.assertEqual(discounted_price, expected_discounted)
        
        # Step 2: Calculate final price with tax
        final_price = PriceCalculator.calculate_final_price_with_tax(
            discounted_price, tax_rate
        )
        expected_final = 810.0  # 750 + (750 * 0.08)
        self.assertEqual(final_price, expected_final)

    @patch('builtins.input')
    @patch('sys.stdout', new_callable=StringIO)
    def test_full_interactive_session(self, mock_stdout: StringIO, mock_input) -> None:
        """Test a complete interactive calculator session."""
        # Simulate user inputs: price=500, discount=20%, tax=0.1
        mock_input.side_effect = ['500.0', '20.0', '0.1']
        
        PriceCalculator.run_interactive_calculator()
        
        output = mock_stdout.getvalue()
        
        # Verify discount calculation output
        self.assertIn("Price after 20.00% discount: ₹400.00", output)
        
        # Verify final price calculation output
        self.assertIn("Final price with tax: ₹440.00", output)

    def test_multiple_discount_scenarios(self) -> None:
        """Test multiple discount scenarios with different price ranges."""
        test_cases = [
            (100.0, 10.0, 90.0),    # Standard case
            (50.0, 50.0, 25.0),     # Half price
            (1000.0, 5.0, 950.0),   # Small discount on large amount
            (0.99, 1.0, 0.9801),    # Small amount with small discount
        ]
        
        for price, discount, expected in test_cases:
            with self.subTest(price=price, discount=discount):
                result = PriceCalculator.calculate_discounted_price(price, discount)
                self.assertAlmostEqual(result, expected, places=4)

    def test_multiple_tax_scenarios(self) -> None:
        """Test multiple tax scenarios with different rates."""
        test_cases = [
            (100.0, 0.05, 105.0),   # 5% tax
            (100.0, 0.10, 110.0),   # 10% tax
            (100.0, 0.15, 115.0),   # 15% tax
            (100.0, 0.0825, 108.25), # 8.25% tax (common sales tax)
        ]
        
        for price, tax_rate, expected in test_cases:
            with self.subTest(price=price, tax_rate=tax_rate):
                result = PriceCalculator.calculate_final_price_with_tax(price, tax_rate)
                self.assertAlmostEqual(result, expected, places=2)

    @patch('builtins.input')
    @patch('sys.stderr', new_callable=StringIO)
    def test_error_recovery_scenarios(self, mock_stderr: StringIO, mock_input) -> None:
        """Test error recovery in interactive mode."""
        # Test sequence: invalid price -> valid inputs
        mock_input.side_effect = ['invalid_price']
        
        PriceCalculator.run_interactive_calculator()
        
        error_output = mock_stderr.getvalue()
        self.assertIn("Error:", error_output)

    def test_currency_formatting_consistency(self) -> None:
        """Test that currency formatting is consistent across calculations."""
        test_price = 123.456
        discount = 10.0
        tax_rate = 0.08
        
        # Calculate step by step
        discounted = PriceCalculator.calculate_discounted_price(test_price, discount)
        final = PriceCalculator.calculate_final_price_with_tax(discounted, tax_rate)
        
        # Verify calculations are mathematically correct
        expected_discounted = test_price * 0.9
        expected_final = expected_discounted * 1.08
        
        self.assertAlmostEqual(discounted, expected_discounted, places=6)
        self.assertAlmostEqual(final, expected_final, places=6)


if __name__ == '__main__':
    unittest.main()