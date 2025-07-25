import java.util.Scanner;

/**
 * Price calculator utility for computing discounted prices and tax calculations.
 * 
 * <p>This class provides static methods for calculating discounted prices and final prices
 * with tax. It follows Google Java Style Guidelines and uses JDK 17 features.
 */
public final class PriceCalculator {
  
  /**
   * Result record containing price calculation details.
   * 
   * @param originalPrice the original price before discount
   * @param discountPercentage the discount percentage applied
   * @param discountedPrice the price after discount
   * @param taxRate the tax rate applied
   * @param finalPrice the final price including tax
   */
  public record PriceCalculationResult(
      double originalPrice,
      double discountPercentage, 
      double discountedPrice,
      double taxRate,
      double finalPrice) {}

  private PriceCalculator() {
    // Utility class - prevent instantiation
  }

  /**
   * Calculates the discounted price based on original price and discount percentage.
   *
   * @param price the original price
   * @param discountPercentage the discount percentage (0-100)
   * @return the discounted price
   * @throws IllegalArgumentException if discount percentage is not between 0 and 100
   */
  public static double calculateDiscountedPrice(double price, double discountPercentage) {
    return switch ((int) discountPercentage) {
      case int d when d < 0 || d > 100 -> 
          throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
      default -> price - (price * discountPercentage / 100.0);
    };
  }

  /**
   * Calculates the final price including tax.
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
   * Calculates complete price breakdown including discount and tax.
   *
   * @param originalPrice the original price
   * @param discountPercentage the discount percentage
   * @param taxRate the tax rate
   * @return PriceCalculationResult with all calculation details
   */
  public static PriceCalculationResult calculateCompletePrice(
      double originalPrice, 
      double discountPercentage, 
      double taxRate) {
    var discountedPrice = calculateDiscountedPrice(originalPrice, discountPercentage);
    var finalPrice = calculateFinalPriceWithTax(discountedPrice, taxRate);
    
    return new PriceCalculationResult(
        originalPrice, 
        discountPercentage, 
        discountedPrice, 
        taxRate, 
        finalPrice);
  }

  /**
   * Main method for interactive price calculation.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    var scanner = new Scanner(System.in);

    try {
      var welcomeMessage = """
          Welcome to Price Calculator!
          This tool helps you calculate discounted prices and final prices with tax.
          """;
      System.out.println(welcomeMessage);

      System.out.print("Enter the original price: ");
      var originalPrice = scanner.nextDouble();

      System.out.print("Enter the discount percentage: ");
      var discount = scanner.nextDouble();

      System.out.print("Enter the tax rate (e.g., 0.05 for 5%): ");
      var tax = scanner.nextDouble();

      var result = calculateCompletePrice(originalPrice, discount, tax);

      var outputMessage = """
          
          Price Calculation Results:
          Original Price: ₹%.2f
          Discount: %.2f%%
          Price after discount: ₹%.2f
          Tax rate: %.2f%%
          Final price with tax: ₹%.2f
          """.formatted(
              result.originalPrice(),
              result.discountPercentage(),
              result.discountedPrice(),
              result.taxRate() * 100,
              result.finalPrice());

      System.out.println(outputMessage);

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    } finally {
      scanner.close();
    }
  }
}