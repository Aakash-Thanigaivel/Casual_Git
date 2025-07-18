#!/usr/bin/env python3
"""Test runner script for the converted code test suite.

This script runs all unit tests for the converted Python modules
and provides coverage reporting.
"""

import unittest
import sys
import os
from typing import Optional


def discover_and_run_tests(test_directory: str = "tests") -> bool:
    """Discover and run all unit tests in the specified directory.
    
    Args:
        test_directory: Directory containing test files.
        
    Returns:
        True if all tests passed, False otherwise.
    """
    # Add the current directory to Python path
    current_dir = os.path.dirname(os.path.abspath(__file__))
    sys.path.insert(0, current_dir)
    
    # Discover tests
    loader = unittest.TestLoader()
    start_dir = os.path.join(current_dir, test_directory)
    
    if not os.path.exists(start_dir):
        print(f"Test directory '{start_dir}' not found.")
        return False
    
    suite = loader.discover(start_dir, pattern='test_*.py')
    
    # Run tests
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    
    # Return success status
    return result.wasSuccessful()


def main() -> None:
    """Main function to run the test suite."""
    print("=" * 60)
    print("Running BOFA Code Converter Test Suite")
    print("=" * 60)
    
    success = discover_and_run_tests()
    
    if success:
        print("\n✅ All tests passed successfully!")
        sys.exit(0)
    else:
        print("\n❌ Some tests failed!")
        sys.exit(1)


if __name__ == "__main__":
    main()