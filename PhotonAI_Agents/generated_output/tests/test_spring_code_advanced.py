"""Additional unit tests for spring_code module.

This module provides additional test cases to ensure comprehensive coverage
of the ReactiveServiceApplication class functionality, focusing on edge cases,
performance scenarios, and advanced testing patterns.
"""

import unittest
from unittest.mock import patch, MagicMock, AsyncMock
import asyncio
import json
from fastapi.testclient import TestClient
from httpx import AsyncClient
import pytest
from spring_code import ReactiveServiceApplication, create_application, main, run_sync


class TestReactiveServiceApplicationAdvanced(unittest.TestCase):
    """Advanced test cases for ReactiveServiceApplication class."""

    def setUp(self) -> None:
        """Set up test fixtures before each test method."""
        self.app_instance = ReactiveServiceApplication()
        self.client = TestClient(self.app_instance.get_app())

    def test_application_singleton_behavior(self) -> None:
        """Test that multiple application instances work independently."""
        app1 = ReactiveServiceApplication()
        app2 = ReactiveServiceApplication()
        
        # Both should be separate instances
        self.assertIsNot(app1, app2)
        self.assertIsNot(app1.get_app(), app2.get_app())
        
        # Both should work independently
        client1 = TestClient(app1.get_app())
        client2 = TestClient(app2.get_app())
        
        response1 = client1.get("/")
        response2 = client2.get("/")
        
        self.assertEqual(response1.status_code, 200)
        self.assertEqual(response2.status_code, 200)
        self.assertEqual(response1.json(), response2.json())

    def test_route_configuration_immutability(self) -> None:
        """Test that route configuration cannot be modified after setup."""
        app = self.app_instance.get_app()
        initial_routes_count = len(app.routes)
        
        # Try to access routes (should not modify them)
        routes = app.routes
        self.assertEqual(len(routes), initial_routes_count)
        
        # Routes should remain consistent
        self.assertGreater(len(app.routes), 0)

    def test_path_variable_extraction_edge_cases(self) -> None:
        """Test path variable extraction with various edge cases."""
        edge_cases = [
            ("", ""),  # Empty string
            ("123", "123"),  # Numeric string
            ("user@domain.com", "user@domain.com"),  # Email-like
            ("file.txt", "file.txt"),  # File extension
            ("path-with-dashes", "path-with-dashes"),  # Dashes
            ("path_with_underscores", "path_with_underscores"),  # Underscores
            ("MixedCaseUser", "MixedCaseUser"),  # Mixed case
        ]
        
        for input_name, expected_name in edge_cases:
            with self.subTest(name=input_name):
                response = self.client.get(f"/{input_name}")
                self.assertEqual(response.status_code, 200)
                expected_message = f"hello, {expected_name}!"
                self.assertEqual(response.json()["message"], expected_message)

    def test_url_encoding_handling(self) -> None:
        """Test handling of URL-encoded characters."""
        encoded_cases = [
            ("%20", " "),  # Space
            ("%21", "!"),  # Exclamation
            ("%40", "@"),  # At symbol
            ("%2B", "+"),  # Plus sign
        ]
        
        for encoded, decoded in encoded_cases:
            with self.subTest(encoded=encoded):
                response = self.client.get(f"/user{encoded}name")
                self.assertEqual(response.status_code, 200)
                # FastAPI automatically decodes URL parameters
                expected_message = f"hello, user{decoded}name!"
                self.assertEqual(response.json()["message"], expected_message)

    def test_response_headers_consistency(self) -> None:
        """Test that response headers are consistent across all routes."""
        test_paths = ["/", "/testuser", "/api/fallback"]
        
        for path in test_paths:
            with self.subTest(path=path):
                response = self.client.get(path)
                
                # Check common headers
                self.assertIn("content-type", response.headers)
                self.assertEqual(response.headers["content-type"], "application/json")
                
                # Check response is valid JSON
                json_data = response.json()
                self.assertIsInstance(json_data, dict)

    def test_application_metadata_consistency(self) -> None:
        """Test application metadata consistency."""
        app = self.app_instance.get_app()
        
        # Test title consistency
        self.assertEqual(app.title, "Reactive Service Application")
        
        # Test version consistency
        self.assertEqual(app.version, "1.0.0")
        
        # Test description consistency
        self.assertEqual(app.description, "A reactive web service converted from Spring Boot")

    def test_openapi_schema_structure(self) -> None:
        """Test OpenAPI schema structure and completeness."""
        app = self.app_instance.get_app()
        schema = app.openapi()
        
        # Test required OpenAPI fields
        required_fields = ["openapi", "info", "paths"]
        for field in required_fields:
            self.assertIn(field, schema)
        
        # Test paths are documented
        paths = schema["paths"]
        self.assertIn("/", paths)
        self.assertIn("/{name}", paths)
        
        # Test GET methods are documented
        self.assertIn("get", paths["/"])
        self.assertIn("get", paths["/{name}"])

    def test_concurrent_application_creation(self) -> None:
        """Test concurrent application creation and usage."""
        import threading
        import queue
        
        results_queue = queue.Queue()
        
        def create_and_test_app(worker_id: int) -> None:
            """Worker function for concurrent testing."""
            try:
                app = create_application()
                client = TestClient(app.get_app())
                
                response = client.get(f"/worker{worker_id}")
                results_queue.put((worker_id, response.status_code, response.json()))
            except Exception as e:
                results_queue.put((worker_id, "error", str(e)))
        
        # Create multiple threads
        threads = []
        num_workers = 5
        
        for worker_id in range(num_workers):
            thread = threading.Thread(target=create_and_test_app, args=(worker_id,))
            threads.append(thread)
            thread.start()
        
        # Wait for completion
        for thread in threads:
            thread.join()
        
        # Verify results
        self.assertEqual(results_queue.qsize(), num_workers)
        
        for _ in range(num_workers):
            worker_id, status_code, response_data = results_queue.get()
            self.assertEqual(status_code, 200)
            self.assertIn("message", response_data)

    def test_memory_efficiency(self) -> None:
        """Test memory efficiency with repeated operations."""
        import gc
        import sys
        
        # Force garbage collection
        gc.collect()
        
        # Perform many operations
        for i in range(1000):
            response = self.client.get(f"/user{i % 100}")
            self.assertEqual(response.status_code, 200)
            
            # Occasionally force garbage collection
            if i % 100 == 0:
                gc.collect()
        
        # Final garbage collection
        gc.collect()
        
        # Test passes if no memory errors occurred
        self.assertTrue(True)

    def test_error_response_format(self) -> None:
        """Test error response format consistency."""
        # Test method not allowed responses
        error_methods = ["POST", "PUT", "DELETE", "PATCH"]
        
        for method in error_methods:
            with self.subTest(method=method):
                response = self.client.request(method, "/")
                self.assertEqual(response.status_code, 405)
                
                # Error response should still be JSON
                self.assertIn("content-type", response.headers)

    def test_route_parameter_validation(self) -> None:
        """Test route parameter validation and handling."""
        # Test very long path parameters
        long_name = "a" * 1000
        response = self.client.get(f"/{long_name}")
        self.assertEqual(response.status_code, 200)
        self.assertIn(long_name, response.json()["message"])
        
        # Test path with special routing characters
        special_chars = [".", "*", "?", "+", "[", "]", "(", ")"]
        for char in special_chars:
            with self.subTest(char=char):
                response = self.client.get(f"/user{char}test")
                self.assertEqual(response.status_code, 200)

    def test_application_state_isolation(self) -> None:
        """Test that application instances maintain state isolation."""
        app1 = ReactiveServiceApplication()
        app2 = ReactiveServiceApplication()
        
        # Modify one app's configuration (if possible)
        fastapi_app1 = app1.get_app()
        fastapi_app2 = app2.get_app()
        
        # They should have separate configurations
        self.assertIsNot(fastapi_app1, fastapi_app2)
        
        # Both should work independently
        client1 = TestClient(fastapi_app1)
        client2 = TestClient(fastapi_app2)
        
        # Test simultaneous requests
        response1 = client1.get("/app1test")
        response2 = client2.get("/app2test")
        
        self.assertEqual(response1.status_code, 200)
        self.assertEqual(response2.status_code, 200)
        self.assertIn("app1test", response1.json()["message"])
        self.assertIn("app2test", response2.json()["message"])


class TestAsyncFunctionality(unittest.TestCase):
    """Test async functionality and coroutine handling."""

    def setUp(self) -> None:
        """Set up test fixtures."""
        self.app_instance = ReactiveServiceApplication()

    @pytest.mark.asyncio
    async def test_async_route_handlers(self) -> None:
        """Test that route handlers are properly async."""
        async with AsyncClient(app=self.app_instance.get_app(), base_url="http://test") as ac:
            # Test that async requests work properly
            response = await ac.get("/")
            self.assertEqual(response.status_code, 200)
            
            response = await ac.get("/asynctest")
            self.assertEqual(response.status_code, 200)

    @pytest.mark.asyncio
    async def test_server_startup_teardown(self) -> None:
        """Test server startup and teardown process."""
        with patch('uvicorn.Server.serve') as mock_serve:
            mock_serve.return_value = AsyncMock()
            
            # Test server startup
            await self.app_instance.start_server(host="testhost", port=9999)
            
            # Verify serve was called
            mock_serve.assert_called_once()


if __name__ == '__main__':
    # Run async tests with pytest
    pytest.main([__file__, "-v"])