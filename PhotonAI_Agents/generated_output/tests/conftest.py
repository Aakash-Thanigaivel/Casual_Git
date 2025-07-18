"""Pytest configuration and shared fixtures.

This module contains pytest configuration and shared test fixtures
that can be used across multiple test modules.
"""

import pytest
from fastapi.testclient import TestClient
from spring_code import ReactiveServiceApplication, create_application
from price_calculator import PriceCalculator


@pytest.fixture
def price_calculator():
    """Fixture that provides a PriceCalculator instance for testing."""
    return PriceCalculator()


@pytest.fixture
def reactive_service():
    """Fixture that provides a ReactiveServiceApplication instance for testing."""
    return ReactiveServiceApplication()


@pytest.fixture
def test_client():
    """Fixture that provides a FastAPI test client for testing endpoints."""
    app = create_application()
    return TestClient(app)


@pytest.fixture
def sample_prices():
    """Fixture that provides sample price data for testing."""
    return {
        "base_price": 100.0,
        "discount_10_percent": 10.0,
        "discount_25_percent": 25.0,
        "tax_rate_5_percent": 0.05,
        "tax_rate_10_percent": 0.10
    }