"""Performance and stress tests for spring_code module.

This module contains performance tests, stress tests, and benchmarking
to ensure the ReactiveServiceApplication can handle various load scenarios
and maintains optimal performance characteristics.
"""

import unittest
import asyncio
import time
import threading
from concurrent.futures import ThreadPoolExecutor, as_completed
from unittest.mock import patch
from fastapi.testclient import TestClient
from httpx import AsyncClient
import pytest
from spring_code import ReactiveServiceApplication, create_application


class TestReactiveServicePerformance(unittest.TestCase):
    """Performance test suite for ReactiveServiceApplication."""

    def setUp(self) -> None:
        """Set up test fixtures."""
        self.app_instance = ReactiveServiceApplication()
        self.client = TestClient(self.app_instance.get_app())

    def test_sequential_request_performance(self) -> None:
        """Test performance of sequential requests."""
        num_requests = 1000
        start_time = time.time()
        
        for i in range(num_requests):
            response = self.client.get(f"/user{i}")
            self.assertEqual(response.status_code, 200)
        
        end_time = time.time()
        total_time = end_time - start_time
        requests_per_second = num_requests / total_time
        
        # Should handle at least 100 requests per second
        self.assertGreater(requests_per_second, 100)
        print(f"Sequential performance: {requests_per_second:.2f} requests/second")

    @pytest.mark.asyncio
    async def test_concurrent_request_performance(self) -> None:
        """Test performance of concurrent async requests."""
        num_requests = 100
        
        async with AsyncClient(app=self.app_instance.get_app(), base_url="http://test") as ac:
            start_time = time.time()
            
            # Create concurrent tasks
            tasks = []
            for i in range(num_requests):
                if i % 3 == 0:
                    task = ac.get("/")
                elif i % 3 == 1:
                    task = ac.get(f"/user{i}")
                else:
                    task = ac.get(f"/api/endpoint/{i}")
                tasks.append(task)
            
            # Execute all tasks concurrently
            responses = await asyncio.gather(*tasks)
            
            end_time = time.time()
            total_time = end_time - start_time
            requests_per_second = num_requests / total_time
            
            # Verify all requests succeeded
            success_count = sum(1 for r in responses if r.status_code == 200)
            self.assertEqual(success_count, num_requests)
            
            # Should handle concurrent requests efficiently
            self.assertGreater(requests_per_second, 50)
            print(f"Concurrent performance: {requests_per_second:.2f} requests/second")

    def test_memory_usage_under_load(self) -> None:
        """Test memory usage stability under sustained load."""
        import gc
        import psutil
        import os
        
        # Get initial memory usage
        process = psutil.Process(os.getpid())
        initial_memory = process.memory_info().rss / 1024 / 1024  # MB
        
        # Perform sustained load
        for batch in range(10):
            for i in range(100):
                response = self.client.get(f"/batch{batch}_user{i}")
                self.assertEqual(response.status_code, 200)
            
            # Force garbage collection after each batch
            gc.collect()
        
        # Check final memory usage
        final_memory = process.memory_info().rss / 1024 / 1024  # MB
        memory_increase = final_memory - initial_memory
        
        # Memory increase should be reasonable (less than 50MB for this test)
        self.assertLess(memory_increase, 50)
        print(f"Memory usage: {initial_memory:.2f}MB -> {final_memory:.2f}MB (+{memory_increase:.2f}MB)")

    def test_response_time_consistency(self) -> None:
        """Test that response times remain consistent under load."""
        response_times = []
        num_requests = 500
        
        for i in range(num_requests):
            start_time = time.time()
            response = self.client.get(f"/user{i}")
            end_time = time.time()
            
            self.assertEqual(response.status_code, 200)
            response_times.append(end_time - start_time)
        
        # Calculate statistics
        avg_time = sum(response_times) / len(response_times)
        max_time = max(response_times)
        min_time = min(response_times)
        
        # Response times should be consistently fast
        self.assertLess(avg_time, 0.01)  # Average under 10ms
        self.assertLess(max_time, 0.1)   # Max under 100ms
        
        print(f"Response times - Avg: {avg_time*1000:.2f}ms, "
              f"Min: {min_time*1000:.2f}ms, Max: {max_time*1000:.2f}ms")

    def test_thread_safety(self) -> None:
        """Test thread safety with multiple concurrent threads."""
        num_threads = 10
        requests_per_thread = 50
        results = []
        
        def worker_thread(thread_id: int) -> dict:
            """Worker function for thread safety testing."""
            thread_results = {
                'thread_id': thread_id,
                'success_count': 0,
                'error_count': 0,
                'response_times': []
            }
            
            for i in range(requests_per_thread):
                try:
                    start_time = time.time()
                    response = self.client.get(f"/thread{thread_id}_user{i}")
                    end_time = time.time()
                    
                    if response.status_code == 200:
                        thread_results['success_count'] += 1
                        thread_results['response_times'].append(end_time - start_time)
                    else:
                        thread_results['error_count'] += 1
                        
                except Exception:
                    thread_results['error_count'] += 1
            
            return thread_results
        
        # Execute threads
        with ThreadPoolExecutor(max_workers=num_threads) as executor:
            futures = [executor.submit(worker_thread, i) for i in range(num_threads)]
            
            for future in as_completed(futures):
                results.append(future.result())
        
        # Verify results
        total_success = sum(r['success_count'] for r in results)
        total_errors = sum(r['error_count'] for r in results)
        expected_total = num_threads * requests_per_thread
        
        self.assertEqual(total_success, expected_total)
        self.assertEqual(total_errors, 0)
        
        print(f"Thread safety test: {total_success}/{expected_total} requests successful")

    @pytest.mark.asyncio
    async def test_async_scalability(self) -> None:
        """Test scalability with increasing concurrent load."""
        load_levels = [10, 25, 50, 100, 200]
        results = {}
        
        for load_level in load_levels:
            async with AsyncClient(app=self.app_instance.get_app(), base_url="http://test") as ac:
                start_time = time.time()
                
                # Create tasks for this load level
                tasks = [ac.get(f"/load_test_{i}") for i in range(load_level)]
                responses = await asyncio.gather(*tasks)
                
                end_time = time.time()
                total_time = end_time - start_time
                
                # Calculate metrics
                success_count = sum(1 for r in responses if r.status_code == 200)
                success_rate = success_count / load_level * 100
                throughput = load_level / total_time
                
                results[load_level] = {
                    'success_rate': success_rate,
                    'throughput': throughput,
                    'total_time': total_time
                }
                
                # All requests should succeed
                self.assertEqual(success_count, load_level)
        
        # Print scalability results
        print("Scalability Test Results:")
        for load, metrics in results.items():
            print(f"  Load {load}: {metrics['success_rate']:.1f}% success, "
                  f"{metrics['throughput']:.1f} req/s, {metrics['total_time']:.3f}s")

    def test_large_payload_handling(self) -> None:
        """Test handling of requests with large path parameters."""
        # Test with increasingly large path parameters
        sizes = [100, 500, 1000, 5000]
        
        for size in sizes:
            with self.subTest(size=size):
                large_name = "a" * size
                
                start_time = time.time()
                response = self.client.get(f"/{large_name}")
                end_time = time.time()
                
                self.assertEqual(response.status_code, 200)
                self.assertIn(large_name, response.json()["message"])
                
                # Response time should still be reasonable
                response_time = end_time - start_time
                self.assertLess(response_time, 1.0)  # Under 1 second

    def test_sustained_load_stability(self) -> None:
        """Test stability under sustained load over time."""
        duration_seconds = 10
        start_time = time.time()
        request_count = 0
        error_count = 0
        
        while time.time() - start_time < duration_seconds:
            try:
                response = self.client.get(f"/sustained_{request_count}")
                if response.status_code == 200:
                    request_count += 1
                else:
                    error_count += 1
            except Exception:
                error_count += 1
        
        end_time = time.time()
        actual_duration = end_time - start_time
        requests_per_second = request_count / actual_duration
        error_rate = error_count / (request_count + error_count) * 100
        
        # Should maintain good performance and low error rate
        self.assertGreater(requests_per_second, 50)
        self.assertLess(error_rate, 1.0)  # Less than 1% error rate
        
        print(f"Sustained load: {request_count} requests in {actual_duration:.1f}s "
              f"({requests_per_second:.1f} req/s, {error_rate:.2f}% errors)")

    def test_application_startup_time(self) -> None:
        """Test application startup performance."""
        startup_times = []
        
        # Test multiple application startups
        for i in range(10):
            start_time = time.time()
            app = ReactiveServiceApplication()
            client = TestClient(app.get_app())
            
            # Make a test request to ensure app is ready
            response = client.get("/")
            end_time = time.time()
            
            self.assertEqual(response.status_code, 200)
            startup_times.append(end_time - start_time)
        
        avg_startup_time = sum(startup_times) / len(startup_times)
        max_startup_time = max(startup_times)
        
        # Startup should be fast
        self.assertLess(avg_startup_time, 0.5)  # Average under 500ms
        self.assertLess(max_startup_time, 1.0)  # Max under 1 second
        
        print(f"Startup performance - Avg: {avg_startup_time*1000:.2f}ms, "
              f"Max: {max_startup_time*1000:.2f}ms")


class TestResourceUtilization(unittest.TestCase):
    """Test resource utilization and efficiency."""

    def test_cpu_usage_efficiency(self) -> None:
        """Test CPU usage during request processing."""
        import psutil
        import os
        
        process = psutil.Process(os.getpid())
        app_instance = ReactiveServiceApplication()
        client = TestClient(app_instance.get_app())
        
        # Measure CPU usage during load
        cpu_percent_before = process.cpu_percent()
        
        # Generate load
        for i in range(1000):
            response = client.get(f"/cpu_test_{i}")
            self.assertEqual(response.status_code, 200)
        
        cpu_percent_after = process.cpu_percent()
        
        # CPU usage should be reasonable
        print(f"CPU usage: {cpu_percent_before:.1f}% -> {cpu_percent_after:.1f}%")

    def test_file_descriptor_usage(self) -> None:
        """Test that file descriptors are properly managed."""
        import psutil
        import os
        
        process = psutil.Process(os.getpid())
        initial_fds = process.num_fds() if hasattr(process, 'num_fds') else 0
        
        # Create and destroy multiple applications
        for i in range(50):
            app = ReactiveServiceApplication()
            client = TestClient(app.get_app())
            response = client.get("/fd_test")
            self.assertEqual(response.status_code, 200)
            
            # Clean up references
            del client
            del app
        
        # Force garbage collection
        import gc
        gc.collect()
        
        final_fds = process.num_fds() if hasattr(process, 'num_fds') else 0
        
        if initial_fds > 0 and final_fds > 0:
            fd_increase = final_fds - initial_fds
            # File descriptor increase should be minimal
            self.assertLess(fd_increase, 10)
            print(f"File descriptors: {initial_fds} -> {final_fds} (+{fd_increase})")


if __name__ == '__main__':
    # Run with pytest to handle async tests
    pytest.main([__file__, "-v", "-s"])