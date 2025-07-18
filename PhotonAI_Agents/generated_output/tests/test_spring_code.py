"""Unit tests for the spring_code module.

This module contains comprehensive unit tests for the ReactiveServiceApplication class
and its FastAPI endpoints, ensuring proper functionality and response handling.
"""

import unittest
from unittest.mock import patch, MagicMock
import pytest
from fastapi.testclient import TestClient
from spring_code import ReactiveServiceApplication, create_application, main


class TestReactiveServiceApplication(unittest.TestCase):
    """Test cases for the ReactiveServiceApplication class."""

    def setUp(self) -> None:
        """Set up test fixtures before each test method."""
        self.service = ReactiveServiceApplication()
        self.client = TestClient(self.service.get_app())

    def test_init_creates_fastapi_app(self) -> None:
        """Test that initialization creates a FastAPI application."""
        self.assertIsNotNone(self.service.app)
        self.assertEqual(self.service.app.title, "Reactive Service Application")
        self.assertEqual(self.service.app.description, "A reactive web service with multiple endpoints")
        self.assertEqual(self.service.app.version, "1.0.0")

    def test_get_app_returns_fastapi_instance(self) -> None:
        """Test that get_app returns the FastAPI application instance."""
        app = self.service.get_app()
        self.assertEqual(app, self.service.app)

    def test_root_endpoint(self) -> None:
        """Test the root endpoint returns correct response."""
        response = self.client.get("/")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hi"})

    def test_name_endpoint(self) -> None:
        """Test the parameterized name endpoint returns correct response."""
        response = self.client.get("/john")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hello, john!"})
        
        # Test with different name
        response = self.client.get("/alice")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hello, alice!"})

    def test_fallback_endpoint(self) -> None:
        """Test the fallback endpoint for unmatched routes."""
        response = self.client.get("/some/random/path")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "fallback"})
        
        # Test another fallback path
        response = self.client.get("/api/v1/users")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "fallback"})

    @patch('uvicorn.run')
    def test_run_method(self, mock_uvicorn_run: MagicMock) -> None:
        """Test that run method calls uvicorn.run with correct parameters."""
        self.service.run(host="127.0.0.1", port=8000)
        
        mock_uvicorn_run.assert_called_once_with(
            self.service.app, 
            host="127.0.0.1", 
            port=8000
        )

    @patch('uvicorn.run')
    def test_run_method_default_parameters(self, mock_uvicorn_run: MagicMock) -> None:
        """Test that run method uses default parameters when none provided."""
        self.service.run()
        
        mock_uvicorn_run.assert_called_once_with(
            self.service.app, 
            host="0.0.0.0", 
            port=3000
        )


class TestModuleFunctions(unittest.TestCase):
    """Test cases for module-level functions."""

    def test_create_application_returns_fastapi_app(self) -> None:
        """Test that create_application returns a FastAPI application."""
        app = create_application()
        self.assertIsNotNone(app)
        self.assertEqual(app.title, "Reactive Service Application")

    @patch('spring_code.ReactiveServiceApplication')
    def test_main_function(self, mock_service_class: MagicMock) -> None:
        """Test that main function creates service and runs it."""
        mock_service_instance = MagicMock()
        mock_service_class.return_value = mock_service_instance
        
        main()
        
        mock_service_class.assert_called_once()
        mock_service_instance.run.assert_called_once_with(host="0.0.0.0", port=3000)


if __name__ == '__main__':
    unittest.main()