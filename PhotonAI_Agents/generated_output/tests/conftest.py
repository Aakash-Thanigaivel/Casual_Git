"""Pytest configuration file for test setup and fixtures.

This module provides common test configuration and fixtures
for the converted code test suite.
"""

from __future__ import annotations

import pytest
from fastapi.testclient import TestClient

from spring_code import create_app


@pytest.fixture
def fastapi_client() -> TestClient:
    """Create a test client for FastAPI application.
    
    Returns:
        A TestClient instance for testing FastAPI endpoints.
    """
    app = create_app()
    return TestClient(app)


@pytest.fixture
def sample_prices() -> dict[str, float]:
    """Provide sample price data for testing.
    
    Returns:
        Dictionary containing sample price values for tests.
    """
    return {
        "base_price": 100.0,
        "discounted_price": 90.0,
        "final_price": 94.50,
        "discount_percentage": 10.0,
        "tax_rate": 0.05
    }