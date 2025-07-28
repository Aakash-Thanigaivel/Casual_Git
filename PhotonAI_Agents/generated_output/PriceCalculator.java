import java.util.Scanner;

/**
 * Calculator for price operations including discount and tax calculations.
 * 
 * <p>This class provides utility methods for calculating discounted prices and final prices
 * with tax. All calculations use double precision for monetary values.
 */
public final class PriceCalculator {
  
  private PriceCalculator() {
    // Utility class - prevent instantiation
  }

  /**
   * Calculates the discounted price based on original price and discount percentage.
   *
   * @param price the original price, must be non-negative
   * @param discountPercentage the discount percentage, must be between 0 and 100
   * @return the price after applying the discount
   * @throws IllegalArgumentException if discount percentage is not between 0 and 100
   */
  public static double calculateDiscountedPrice(double price, double discountPercentage) {
    if (discountPercentage < 0 || discountPercentage > 100) {
      throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
    }
    return price - (price * discountPercentage / 100.0);
  }

  /**
   * Calculates the final price including tax.
   *
   * @param price the base price before tax
   * @param taxRate the tax rate as a decimal (e.g., 0.05 for 5%)
   * @return the final price including tax
   * @throws IllegalArgumentException if tax rate is negative
   */
  public static double calculateFinalPriceWithTax(double price, double taxRate) {
    if (taxRate < 0) {
      throw new IllegalArgumentException("Tax rate cannot be negative.");
    }
    return price + (price * taxRate);
  }

  /**
   * Main method to run the price calculator application.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    try (var scanner = new Scanner(System.in)) {
      System.out.print("Enter the original price: ");
      var originalPrice = scanner.nextDouble();

      System.out.print("Enter the discount percentage: ");
      var discount = scanner.nextDouble();

      var discountedPrice = calculateDiscountedPrice(originalPrice, discount);
      System.out.printf("Price after %.2f%% discount: ₹%.2f%n", discount, discountedPrice);

      System.out.print("Enter the tax rate (e.g., 0.05 for 5%): ");
      var tax = scanner.nextDouble();

      var finalPrice = calculateFinalPriceWithTax(discountedPrice, tax);
      System.out.printf("Final price with tax: ₹%.2f%n", finalPrice);

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}