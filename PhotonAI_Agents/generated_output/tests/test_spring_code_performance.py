"""Performance and load tests for spring_code module.

This module contains performance tests and load testing scenarios
for the FastAPI reactive service application.
"""

import unittest
import asyncio
import time
from concurrent.futures import ThreadPoolExecutor, as_completed
from unittest.mock import patch, MagicMock
from fastapi.testclient import TestClient
import threading

from spring_code import ReactiveServiceApplication, create_application


class TestSpringCodePerformance(unittest.TestCase):
    """Performance test cases for ReactiveServiceApplication."""

    def setUp(self) -> None:
        """Sets up test fixtures before each test method."""
        self.app_instance = ReactiveServiceApplication()
        self.client = TestClient(self.app_instance.get_app())

    def test_response_time_benchmarks(self) -> None:
        """Tests response time benchmarks for all endpoints."""
        endpoints = ["/", "/testuser", "/api/unknown/path"]
        max_response_time = 0.1  # 100ms threshold
        
        for endpoint in endpoints:
            with self.subTest(endpoint=endpoint):
                start_time = time.time()
                response = self.client.get(endpoint)
                end_time = time.time()
                
                response_time = end_time - start_time
                
                self.assertEqual(response.status_code, 200)
                self.assertLess(response_time, max_response_time,
                              f"Response time {response_time:.3f}s exceeded threshold for {endpoint}")

    def test_concurrent_request_handling(self) -> None:
        """Tests handling of multiple concurrent requests."""
        num_requests = 50
        endpoints = ["/", "/user1", "/user2", "/unknown"]
        
        def make_request(endpoint):
            return self.client.get(endpoint)
        
        start_time = time.time()
        
        with ThreadPoolExecutor(max_workers=10) as executor:
            # Submit requests for different endpoints
            futures = []
            for i in range(num_requests):
                endpoint = endpoints[i % len(endpoints)]
                futures.append(executor.submit(make_request, endpoint))
            
            # Collect results
            results = []
            for future in as_completed(futures):
                results.append(future.result())
        
        end_time = time.time()
        total_time = end_time - start_time
        
        # Verify all requests succeeded
        self.assertEqual(len(results), num_requests)
        for result in results:
            self.assertEqual(result.status_code, 200)
            self.assertIn("message", result.json())
        
        # Performance assertion - should handle 50 requests in reasonable time
        self.assertLess(total_time, 5.0, f"Total time {total_time:.3f}s too slow for {num_requests} requests")

    def test_memory_usage_stability(self) -> None:
        """Tests that memory usage remains stable under load."""
        import gc
        import sys
        
        # Force garbage collection before test
        gc.collect()
        initial_objects = len(gc.get_objects())
        
        # Make many requests
        for i in range(100):
            response = self.client.get(f"/user{i}")
            self.assertEqual(response.status_code, 200)
        
        # Force garbage collection after test
        gc.collect()
        final_objects = len(gc.get_objects())
        
        # Memory usage should not grow significantly
        object_growth = final_objects - initial_objects
        self.assertLess(object_growth, 1000, 
                       f"Memory usage grew by {object_growth} objects")

    def test_endpoint_scalability(self) -> None:
        """Tests scalability of endpoints with varying load patterns."""
        load_patterns = [
            (10, 2),   # 10 requests, 2 workers
            (25, 5),   # 25 requests, 5 workers
            (50, 10),  # 50 requests, 10 workers
        ]
        
        for num_requests, num_workers in load_patterns:
            with self.subTest(requests=num_requests, workers=num_workers):
                start_time = time.time()
                
                with ThreadPoolExecutor(max_workers=num_workers) as executor:
                    futures = [
                        executor.submit(self.client.get, f"/user{i}")
                        for i in range(num_requests)
                    ]
                    
                    results = [future.result() for future in as_completed(futures)]
                
                end_time = time.time()
                duration = end_time - start_time
                
                # All requests should succeed
                success_count = sum(1 for r in results if r.status_code == 200)
                self.assertEqual(success_count, num_requests)
                
                # Calculate requests per second
                rps = num_requests / duration
                self.assertGreater(rps, 10, f"RPS {rps:.2f} too low for load pattern")


class TestSpringCodeStressTests(unittest.TestCase):
    """Stress test cases for ReactiveServiceApplication."""

    def setUp(self) -> None:
        """Sets up test fixtures before each test method."""
        self.app_instance = ReactiveServiceApplication()
        self.client = TestClient(self.app_instance.get_app())

    def test_high_volume_named_requests(self) -> None:
        """Tests handling of high volume requests to named endpoint."""
        num_requests = 200
        unique_names = [f"user_{i}" for i in range(num_requests)]
        
        start_time = time.time()
        
        with ThreadPoolExecutor(max_workers=20) as executor:
            futures = [
                executor.submit(self.client.get, f"/{name}")
                for name in unique_names
            ]
            
            results = [future.result() for future in as_completed(futures)]
        
        end_time = time.time()
        
        # Verify all requests succeeded with correct responses
        self.assertEqual(len(results), num_requests)
        for i, result in enumerate(results):
            self.assertEqual(result.status_code, 200)
            expected_message = f"hello, user_{i}!"
            # Note: Order might not be preserved, so we check if any result matches
            response_messages = [r.json()["message"] for r in results]
            self.assertIn(expected_message, response_messages)
        
        # Performance check
        duration = end_time - start_time
        self.assertLess(duration, 10.0, f"High volume test took {duration:.3f}s")

    def test_rapid_fire_requests(self) -> None:
        """Tests rapid consecutive requests to the same endpoint."""
        num_requests = 100
        endpoint = "/rapidtest"
        
        start_time = time.time()
        
        # Make rapid consecutive requests
        results = []
        for i in range(num_requests):
            response = self.client.get(endpoint)
            results.append(response)
        
        end_time = time.time()
        duration = end_time - start_time
        
        # All requests should succeed
        for result in results:
            self.assertEqual(result.status_code, 200)
            self.assertEqual(result.json()["message"], "hello, rapidtest!")
        
        # Should handle rapid requests efficiently
        avg_response_time = duration / num_requests
        self.assertLess(avg_response_time, 0.01, 
                       f"Average response time {avg_response_time:.4f}s too slow")

    def test_mixed_endpoint_stress(self) -> None:
        """Tests stress scenario with mixed endpoint usage."""
        endpoints = [
            ("/", "hi"),
            ("/alice", "hello, alice!"),
            ("/bob", "hello, bob!"),
            ("/api/test", "fallback"),
            ("/unknown/path", "fallback"),
        ]
        
        num_iterations = 50
        total_requests = num_iterations * len(endpoints)
        
        start_time = time.time()
        
        with ThreadPoolExecutor(max_workers=15) as executor:
            futures = []
            for _ in range(num_iterations):
                for endpoint, expected_msg in endpoints:
                    futures.append(executor.submit(self.client.get, endpoint))
            
            results = [future.result() for future in as_completed(futures)]
        
        end_time = time.time()
        
        # Verify all requests succeeded
        self.assertEqual(len(results), total_requests)
        success_count = sum(1 for r in results if r.status_code == 200)
        self.assertEqual(success_count, total_requests)
        
        # Performance metrics
        duration = end_time - start_time
        rps = total_requests / duration
        self.assertGreater(rps, 20, f"Mixed stress test RPS {rps:.2f} too low")


class TestApplicationLifecycle(unittest.TestCase):
    """Tests for application lifecycle and resource management."""

    def test_multiple_application_instances(self) -> None:
        """Tests creating and using multiple application instances."""
        apps = [ReactiveServiceApplication() for _ in range(5)]
        clients = [TestClient(app.get_app()) for app in apps]
        
        # Test that all instances work independently
        for i, client in enumerate(clients):
            response = client.get(f"/instance{i}")
            self.assertEqual(response.status_code, 200)
            self.assertEqual(response.json()["message"], f"hello, instance{i}!")

    def test_factory_function_performance(self) -> None:
        """Tests performance of the factory function."""
        num_apps = 20
        
        start_time = time.time()
        apps = [create_application() for _ in range(num_apps)]
        end_time = time.time()
        
        creation_time = end_time - start_time
        
        # Verify all apps were created successfully
        self.assertEqual(len(apps), num_apps)
        for app in apps:
            self.assertIsInstance(app, ReactiveServiceApplication)
        
        # Should create apps quickly
        avg_creation_time = creation_time / num_apps
        self.assertLess(avg_creation_time, 0.1, 
                       f"Average app creation time {avg_creation_time:.4f}s too slow")

    @patch('uvicorn.run')
    def test_run_method_resource_cleanup(self, mock_uvicorn_run) -> None:
        """Tests that run method properly handles resource cleanup."""
        app = ReactiveServiceApplication()
        
        # Simulate run method call
        app.run(host="test", port=8080)
        
        # Verify uvicorn was called with correct parameters
        mock_uvicorn_run.assert_called_once_with(
            app.app,
            host="test",
            port=8080,
            log_level="info"
        )

    def test_thread_safety(self) -> None:
        """Tests thread safety of the application."""
        app = ReactiveServiceApplication()
        client = TestClient(app.get_app())
        
        results = []
        errors = []
        
        def worker_thread(thread_id):
            try:
                for i in range(10):
                    response = client.get(f"/thread{thread_id}_req{i}")
                    results.append((thread_id, i, response.status_code))
            except Exception as e:
                errors.append((thread_id, str(e)))
        
        # Create multiple threads
        threads = []
        for thread_id in range(5):
            thread = threading.Thread(target=worker_thread, args=(thread_id,))
            threads.append(thread)
        
        # Start all threads
        for thread in threads:
            thread.start()
        
        # Wait for all threads to complete
        for thread in threads:
            thread.join()
        
        # Verify no errors occurred
        self.assertEqual(len(errors), 0, f"Thread safety errors: {errors}")
        
        # Verify all requests succeeded
        self.assertEqual(len(results), 50)  # 5 threads * 10 requests each
        for thread_id, req_id, status_code in results:
            self.assertEqual(status_code, 200)


if __name__ == "__main__":
    unittest.main()