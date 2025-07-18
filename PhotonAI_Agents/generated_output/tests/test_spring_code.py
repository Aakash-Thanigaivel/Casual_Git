"""Unit tests for spring_code FastAPI application.

This module contains unit tests for the FastAPI web service endpoints,
testing basic functionality with 5% code coverage.
"""

import unittest
from unittest.mock import patch, MagicMock
import sys
import os
from fastapi.testclient import TestClient

# Add the parent directory to the path to import the module under test
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from spring_code import app, create_app, root, greet_user, fallback_handler


class TestSpringCodeAPI(unittest.TestCase):
    """Test cases for FastAPI web service endpoints."""

    def setUp(self):
        """Set up test client for FastAPI application."""
        self.client = TestClient(app)

    def test_root_endpoint(self):
        """Test the root endpoint returns correct greeting."""
        response = self.client.get("/")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hi"})

    def test_greet_user_endpoint(self):
        """Test the greet user endpoint with a name parameter."""
        response = self.client.get("/john")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "hello, john!"})

    def test_fallback_endpoint(self):
        """Test the fallback endpoint for unmatched routes."""
        response = self.client.get("/some/random/path")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {"message": "fallback"})

    def test_create_app_function(self):
        """Test the create_app function returns FastAPI instance."""
        app_instance = create_app()
        self.assertIsNotNone(app_instance)
        self.assertEqual(app_instance.title, "Reactive Service Application")

    async def test_root_function_direct(self):
        """Test the root function directly."""
        result = await root()
        self.assertEqual(result, {"message": "hi"})

    async def test_greet_user_function_direct(self):
        """Test the greet_user function directly."""
        result = await greet_user("alice")
        self.assertEqual(result, {"message": "hello, alice!"})


if __name__ == "__main__":
    unittest.main()