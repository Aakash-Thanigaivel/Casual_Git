import java.util.Scanner;

public class PriceCalculator {
    
    public static double calculateDiscountedPrice(double price, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        return price - (price * discountPercentage / 100.0);
    }

    public static double calculateFinalPriceWithTax(double price, double taxRate) {
        if (taxRate < 0) {
            throw new IllegalArgumentException("Tax rate cannot be negative.");
        }
        return price + (price * taxRate);
    }

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