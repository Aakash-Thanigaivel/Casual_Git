"""Unit tests for the spring_code module.

This module contains comprehensive unit tests for the ReactiveServiceApplication
class and related functions, following Google Python Style Guidelines.
"""

import unittest
from unittest.mock import patch, MagicMock
import pytest
from fastapi.testclient import TestClient

# Import the module under test
from spring_code import ReactiveServiceApplication, create_app, main


class TestReactiveServiceApplication(unittest.TestCase):
    """Test cases for the ReactiveServiceApplication class."""

    def setUp(self):
        """Set up test fixtures before each test method."""
        self.service = ReactiveServiceApplication()
        self.client = TestClient(self.service.get_app())

    def test_init_creates_fastapi_app(self):
        """Test that __init__ creates a FastAPI application."""
        self.assertIsNotNone(self.service.app)
        self.assertEqual(self.service.app.title, "Reactive Service Application")
        self.assertEqual(self.service.app.description, "A reactive web service converted from Spring Boot")
        self.assertEqual(self.service.app.version, "1.0.0")

    def test_get_app_returns_fastapi_instance(self):
        """Test that get_app returns the FastAPI application instance."""
        app = self.service.get_app()
        self.assertEqual(app, self.service.app)

    def test_root_endpoint(self):
        """Test the root endpoint returns correct response."""
        response = self.client.get("/")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hi"})

    def test_greet_user_endpoint(self):
        """Test the greet user endpoint with name parameter."""
        response = self.client.get("/john")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hello, john!"})

    def test_greet_user_endpoint_with_special_characters(self):
        """Test the greet user endpoint with special characters in name."""
        response = self.client.get("/user-123")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hello, user-123!"})

    def test_fallback_endpoint(self):
        """Test the fallback endpoint for unmatched routes."""
        response = self.client.get("/some/random/path")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "fallback"})

    def test_fallback_endpoint_with_query_params(self):
        """Test the fallback endpoint with query parameters."""
        response = self.client.get("/unknown/path?param=value")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "fallback"})


class TestCreateApp(unittest.TestCase):
    """Test cases for the create_app function."""

    def test_create_app_returns_fastapi_instance(self):
        """Test that create_app returns a FastAPI application."""
        app = create_app()
        self.assertIsNotNone(app)
        self.assertEqual(app.title, "Reactive Service Application")

    def test_create_app_configures_routes(self):
        """Test that create_app properly configures all routes."""
        app = create_app()
        client = TestClient(app)
        
        # Test all endpoints are accessible
        root_response = client.get("/")
        self.assertEqual(root_response.status_code, 200)
        
        name_response = client.get("/testuser")
        self.assertEqual(name_response.status_code, 200)
        
        fallback_response = client.get("/any/other/path")
        self.assertEqual(fallback_response.status_code, 200)


class TestMainFunction(unittest.TestCase):
    """Test cases for the main function."""

    @patch('spring_code.uvicorn.run')
    @patch('spring_code.create_app')
    def test_main_calls_uvicorn_with_correct_params(self, mock_create_app, mock_uvicorn_run):
        """Test that main function calls uvicorn.run with correct parameters."""
        mock_app = MagicMock()
        mock_create_app.return_value = mock_app
        
        main()
        
        mock_create_app.assert_called_once()
        mock_uvicorn_run.assert_called_once_with(
            mock_app,
            host="0.0.0.0",
            port=3000,
            log_level="info"
        )

    @patch('spring_code.uvicorn.run')
    def test_main_creates_app_instance(self, mock_uvicorn_run):
        """Test that main function creates an app instance."""
        main()
        
        # Verify uvicorn.run was called (indicating app was created)
        mock_uvicorn_run.assert_called_once()
        args, kwargs = mock_uvicorn_run.call_args
        
        # Verify the correct parameters
        self.assertEqual(kwargs['host'], "0.0.0.0")
        self.assertEqual(kwargs['port'], 3000)
        self.assertEqual(kwargs['log_level'], "info")


class TestIntegration(unittest.TestCase):
    """Integration tests for the complete application."""

    def setUp(self):
        """Set up test fixtures for integration tests."""
        self.app = create_app()
        self.client = TestClient(self.app)

    def test_complete_application_flow(self):
        """Test the complete application flow with multiple requests."""
        # Test root endpoint
        response = self.client.get("/")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hi"})
        
        # Test named greeting
        response = self.client.get("/alice")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hello, alice!"})
        
        # Test fallback
        response = self.client.get("/api/v1/users")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "fallback"})

    def test_application_handles_concurrent_requests(self):
        """Test that application can handle multiple concurrent requests."""
        responses = []
        
        # Simulate concurrent requests
        for i in range(5):
            response = self.client.get(f"/user{i}")
            responses.append(response)
        
        # Verify all responses are successful
        for i, response in enumerate(responses):
            self.assertEqual(response.status_code, 200)
            self.assertEqual(response.json(), {"message": f"hello, user{i}!"})


if __name__ == '__main__':
    unittest.main()