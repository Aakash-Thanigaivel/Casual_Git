"""Pytest configuration file for the test suite.

This module contains shared fixtures and configuration for all tests,
following Google Python Style Guidelines.
"""

import pytest
import sys
import os
from pathlib import Path

# Add the parent directory to the Python path so we can import the modules under test
sys.path.insert(0, str(Path(__file__).parent.parent))


@pytest.fixture
def sample_price():
    """Fixture providing a sample price for testing.
    
    Returns:
        float: A sample price value for testing calculations.
    """
    return 100.0


@pytest.fixture
def sample_discount():
    """Fixture providing a sample discount percentage for testing.
    
    Returns:
        float: A sample discount percentage for testing.
    """
    return 10.0


@pytest.fixture
def sample_tax_rate():
    """Fixture providing a sample tax rate for testing.
    
    Returns:
        float: A sample tax rate for testing.
    """
    return 0.05


@pytest.fixture
def price_calculator():
    """Fixture providing a PriceCalculator instance for testing.
    
    Returns:
        PriceCalculator: An instance of the PriceCalculator class.
    """
    from price_calculator import PriceCalculator
    return PriceCalculator()


@pytest.fixture
def reactive_service_app():
    """Fixture providing a ReactiveServiceApplication instance for testing.
    
    Returns:
        ReactiveServiceApplication: An instance of the ReactiveServiceApplication class.
    """
    from spring_code import ReactiveServiceApplication
    return ReactiveServiceApplication()


@pytest.fixture
def test_client():
    """Fixture providing a FastAPI test client for testing.
    
    Returns:
        TestClient: A configured test client for the FastAPI application.
    """
    from fastapi.testclient import TestClient
    from spring_code import create_app
    
    app = create_app()
    return TestClient(app)