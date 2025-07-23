"""Price calculator module for discount and tax calculations.

This module provides functionality for calculating discounted prices and applying
tax rates to products, following Google Python Style Guidelines.
"""

from __future__ import annotations

import sys
from typing import Union


class PriceCalculator:
    """Calculator for price operations including discounts and taxes."""

    @staticmethod
    def calculate_discounted_price(
        price: float, 
        discount_percentage: float
    ) -> float:
        """Calculates the price after applying a discount.

        Args:
            price: The original price of the item.
            discount_percentage: The discount percentage to apply (0-100).

        Returns:
            The discounted price.

        Raises:
            ValueError: If discount percentage is not between 0 and 100.
        """
        if discount_percentage < 0 or discount_percentage > 100:
            raise ValueError(
                "Discount percentage must be between 0 and 100."
            )
        return price - (price * discount_percentage / 100.0)

    @staticmethod
    def calculate_final_price_with_tax(price: float, tax_rate: float) -> float:
        """Calculates the final price after applying tax.

        Args:
            price: The base price before tax.
            tax_rate: The tax rate to apply (e.g., 0.05 for 5%).

        Returns:
            The final price including tax.

        Raises:
            ValueError: If tax rate is negative.
        """
        if tax_rate < 0:
            raise ValueError("Tax rate cannot be negative.")
        return price + (price * tax_rate)


def main() -> None:
    """Main function to run the price calculator interactively."""
    try:
        original_price = float(input("Enter the original price: "))
        
        discount = float(input("Enter the discount percentage: "))
        
        discounted_price = PriceCalculator.calculate_discounted_price(
            original_price, discount
        )
        print(f"Price after {discount:.2f}% discount: ₹{discounted_price:.2f}")
        
        tax = float(input("Enter the tax rate (e.g., 0.05 for 5%): "))
        
        final_price = PriceCalculator.calculate_final_price_with_tax(
            discounted_price, tax
        )
        print(f"Final price with tax: ₹{final_price:.2f}")
        
    except ValueError as e:
        print(f"Error: {e}", file=sys.stderr)
    except Exception as e:
        print(f"Unexpected error: {e}", file=sys.stderr)


if __name__ == "__main__":
    main()