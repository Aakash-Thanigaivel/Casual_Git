import java.util.Scanner;

/**
 * A calculator for computing discounted prices and applying tax rates.
 * 
 * <p>This class provides methods to calculate discounted prices and final prices
 * with tax, following Google Java Style Guidelines.
 */
public class PriceCalculator {
  
  /**
   * Calculates the price after applying a discount percentage.
   *
   * @param price the original price
   * @param discountPercentage the discount percentage (0-100)
   * @return the discounted price
   * @throws IllegalArgumentException if discount percentage is not between 0 and 100
   */
  public static double calculateDiscountedPrice(double price, double discountPercentage) {
    if (discountPercentage < 0 || discountPercentage > 100) {
      throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
    }
    return price - (price * discountPercentage / 100.0);
  }

  /**
   * Calculates the final price after applying tax.
   *
   * @param price the base price
   * @param taxRate the tax rate (e.g., 0.05 for 5%)
   * @return the final price with tax
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