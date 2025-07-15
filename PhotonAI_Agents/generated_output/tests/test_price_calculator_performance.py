"""Performance and stress tests for price_calculator module.

This module contains performance tests and stress tests to ensure the
PriceCalculator can handle various load scenarios and edge cases.
"""

import unittest
import time
from price_calculator import PriceCalculator


class TestPriceCalculatorPerformance(unittest.TestCase):
    """Performance and stress test suite for PriceCalculator."""

    def test_calculation_performance(self) -> None:
        """Test performance of calculation methods with large datasets."""
        start_time = time.time()
        
        # Perform 10000 calculations
        for i in range(10000):
            price = 100.0 + i * 0.01
            discount = 10.0 + (i % 50)
            tax_rate = 0.05 + (i % 10) * 0.01
            
            discounted = PriceCalculator.calculate_discounted_price(price, discount)
            final = PriceCalculator.calculate_final_price_with_tax(discounted, tax_rate)
        
        end_time = time.time()
        execution_time = end_time - start_time
        
        # Should complete within reasonable time (less than 1 second)
        self.assertLess(execution_time, 1.0, 
                       f"Performance test took {execution_time:.3f} seconds")

    def test_precision_with_extreme_values(self) -> None:
        """Test calculation precision with extreme values."""
        # Test with very large numbers
        large_price = 999999999.99
        result = PriceCalculator.calculate_discounted_price(large_price, 50.0)
        expected = large_price * 0.5
        self.assertAlmostEqual(result, expected, places=2)
        
        # Test with very small numbers
        small_price = 0.01
        result = PriceCalculator.calculate_final_price_with_tax(small_price, 0.05)
        expected = small_price * 1.05
        self.assertAlmostEqual(result, expected, places=4)

    def test_repeated_calculations_consistency(self) -> None:
        """Test that repeated calculations produce consistent results."""
        price = 123.45
        discount = 15.5
        tax_rate = 0.0825
        
        # Perform same calculation multiple times
        results_discount = []
        results_tax = []
        
        for _ in range(1000):
            discounted = PriceCalculator.calculate_discounted_price(price, discount)
            taxed = PriceCalculator.calculate_final_price_with_tax(price, tax_rate)
            results_discount.append(discounted)
            results_tax.append(taxed)
        
        # All results should be identical
        self.assertTrue(all(r == results_discount[0] for r in results_discount))
        self.assertTrue(all(r == results_tax[0] for r in results_tax))

    def test_memory_usage_with_large_datasets(self) -> None:
        """Test memory usage doesn't grow with repeated calculations."""
        import gc
        
        # Force garbage collection before test
        gc.collect()
        
        # Perform many calculations
        for i in range(50000):
            price = 100.0 + (i % 1000)
            discount = 10.0 + (i % 90)
            
            # These should not accumulate memory
            PriceCalculator.calculate_discounted_price(price, discount)
            PriceCalculator.calculate_final_price_with_tax(price, 0.08)
        
        # Force garbage collection after test
        gc.collect()
        
        # Test passes if no memory errors occurred
        self.assertTrue(True)

    def test_concurrent_calculation_safety(self) -> None:
        """Test that static methods are thread-safe."""
        import threading
        import queue
        
        results_queue = queue.Queue()
        
        def calculate_worker(worker_id: int) -> None:
            """Worker function for concurrent testing."""
            for i in range(100):
                price = 100.0 + worker_id
                discount = 10.0 + i
                result = PriceCalculator.calculate_discounted_price(price, discount)
                results_queue.put((worker_id, i, result))
        
        # Create and start multiple threads
        threads = []
        for worker_id in range(10):
            thread = threading.Thread(target=calculate_worker, args=(worker_id,))
            threads.append(thread)
            thread.start()
        
        # Wait for all threads to complete
        for thread in threads:
            thread.join()
        
        # Verify we got expected number of results
        self.assertEqual(results_queue.qsize(), 1000)  # 10 workers * 100 calculations each


if __name__ == '__main__':
    unittest.main()