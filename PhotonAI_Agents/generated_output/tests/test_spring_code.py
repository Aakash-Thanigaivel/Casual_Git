"""Unit tests for spring_code module.

This module contains comprehensive unit tests for the ReactiveServiceApplication
class and its FastAPI endpoints, ensuring proper functionality and responses.
"""

from __future__ import annotations

import unittest
from unittest.mock import patch, MagicMock
import pytest
from fastapi.testclient import TestClient

from spring_code import ReactiveServiceApplication, create_app, main


class TestReactiveServiceApplication(unittest.TestCase):
    """Test cases for ReactiveServiceApplication class."""

    def setUp(self) -> None:
        """Set up test fixtures."""
        self.service = ReactiveServiceApplication()
        self.client = TestClient(self.service.get_app())

    def test_initialization(self) -> None:
        """Test ReactiveServiceApplication initialization."""
        self.assertIsNotNone(self.service.app)
        self.assertEqual(self.service.app.title, "Reactive Service Application")
        self.assertEqual(self.service.app.version, "1.0.0")

    def test_root_endpoint(self) -> None:
        """Test the root endpoint returns correct response."""
        response = self.client.get("/")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hi"})

    def test_greet_user_endpoint(self) -> None:
        """Test the personalized greeting endpoint."""
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

    def test_get_app_method(self) -> None:
        """Test get_app method returns FastAPI instance."""
        app = self.service.get_app()
        self.assertEqual(app, self.service.app)


class TestCreateApp(unittest.TestCase):
    """Test cases for create_app function."""

    def test_create_app_returns_fastapi_instance(self) -> None:
        """Test create_app function returns a FastAPI instance."""
        app = create_app()
        self.assertIsNotNone(app)
        self.assertEqual(app.title, "Reactive Service Application")


class TestMainFunction(unittest.TestCase):
    """Test cases for main function."""

    @patch('spring_code.uvicorn.run')
    @patch('spring_code.create_app')
    def test_main_function_success(self, mock_create_app, mock_uvicorn_run) -> None:
        """Test main function runs successfully."""
        mock_app = MagicMock()
        mock_create_app.return_value = mock_app
        
        main()
        
        mock_create_app.assert_called_once()
        mock_uvicorn_run.assert_called_once_with(
            mock_app,
            host="0.0.0.0",
            port=3000,
            log_level="info",
            access_log=True
        )

    @patch('spring_code.uvicorn.run')
    @patch('spring_code.create_app')
    @patch('builtins.print')
    def test_main_function_exception_handling(self, mock_print, mock_create_app, mock_uvicorn_run) -> None:
        """Test main function handles exceptions properly."""
        mock_app = MagicMock()
        mock_create_app.return_value = mock_app
        mock_uvicorn_run.side_effect = Exception("Server error")
        
        with self.assertRaises(Exception):
            main()
        
        mock_print.assert_called_with("Error starting server: Server error")


if __name__ == '__main__':
    unittest.main()