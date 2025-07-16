"""Security and validation tests for the converted applications.

This module contains security-focused tests and input validation tests
for both price_calculator and spring_code modules.
"""

import unittest
from unittest.mock import patch, StringIO
from fastapi.testclient import TestClient
import json

from price_calculator import PriceCalculator
from spring_code import ReactiveServiceApplication


class TestPriceCalculatorSecurity(unittest.TestCase):
    """Security test cases for PriceCalculator."""

    def setUp(self) -> None:
        """Sets up test fixtures before each test method."""
        self.calculator = PriceCalculator()

    def test_input_validation_sql_injection_attempts(self) -> None:
        """Tests that SQL injection attempts are handled safely."""
        # These should raise ValueError or TypeError, not cause security issues
        malicious_inputs = [
            "'; DROP TABLE users; --",
            "1; DELETE FROM accounts;",
            "UNION SELECT * FROM passwords",
        ]
        
        for malicious_input in malicious_inputs:
            with self.subTest(input=malicious_input):
                with self.assertRaises((ValueError, TypeError)):
                    self.calculator.calculate_discounted_price(100, malicious_input)
                
                with self.assertRaises((ValueError, TypeError)):
                    self.calculator.calculate_final_price_with_tax(100, malicious_input)

    def test_input_validation_script_injection_attempts(self) -> None:
        """Tests that script injection attempts are handled safely."""
        script_inputs = [
            "<script>alert('xss')</script>",
            "javascript:alert('xss')",
            "eval('malicious code')",
            "__import__('os').system('rm -rf /')",
        ]
        
        for script_input in script_inputs:
            with self.subTest(input=script_input):
                with self.assertRaises((ValueError, TypeError)):
                    self.calculator.calculate_discounted_price(100, script_input)

    def test_numeric_overflow_protection(self) -> None:
        """Tests protection against numeric overflow attacks."""
        large_values = [
            float('inf'),
            float('-inf'),
            1e308,  # Near float max
            -1e308,
        ]
        
        for large_value in large_values:
            with self.subTest(value=large_value):
                try:
                    result = self.calculator.calculate_discounted_price(large_value, 10)
                    # If it doesn't raise an exception, result should be reasonable
                    self.assertFalse(result != result)  # Check for NaN
                except (ValueError, OverflowError):
                    # These exceptions are acceptable for security
                    pass

    def test_input_sanitization_in_interactive_mode(self) -> None:
        """Tests input sanitization in interactive calculator mode."""
        dangerous_inputs = [
            ['__import__("os").system("ls")', '10', '0.05'],
            ['100', 'eval("print(\'hacked\')")', '0.05'],
            ['100', '10', 'exec("import sys; sys.exit()")'],
        ]
        
        for inputs in dangerous_inputs:
            with self.subTest(inputs=inputs):
                with patch('builtins.input', side_effect=inputs):
                    with patch('sys.stdout', new=StringIO()) as fake_out:
                        # Should handle dangerous input gracefully
                        self.calculator.run_interactive_calculator()
                        output = fake_out.getvalue()
                        # Should show error, not execute dangerous code
                        self.assertIn("error", output.lower())

    def test_denial_of_service_protection(self) -> None:
        """Tests protection against DoS attacks through resource exhaustion."""
        # Test with extremely large discount percentages
        with self.assertRaises(ValueError):
            self.calculator.calculate_discounted_price(100, 1e10)
        
        # Test with extremely large tax rates
        try:
            result = self.calculator.calculate_final_price_with_tax(100, 1e10)
            # If it doesn't raise an exception, should still be computable
            self.assertIsInstance(result, (int, float))
        except (ValueError, OverflowError):
            # These are acceptable responses
            pass


class TestSpringCodeSecurity(unittest.TestCase):
    """Security test cases for ReactiveServiceApplication."""

    def setUp(self) -> None:
        """Sets up test fixtures before each test method."""
        self.app_instance = ReactiveServiceApplication()
        self.client = TestClient(self.app_instance.get_app())

    def test_path_traversal_protection(self) -> None:
        """Tests protection against path traversal attacks."""
        path_traversal_attempts = [
            "../../../etc/passwd",
            "..\\..\\..\\windows\\system32\\config\\sam",
            "%2e%2e%2f%2e%2e%2f%2e%2e%2fetc%2fpasswd",  # URL encoded
            "....//....//....//etc/passwd",
            "/etc/passwd",
            "\\etc\\passwd",
        ]
        
        for malicious_path in path_traversal_attempts:
            with self.subTest(path=malicious_path):
                response = self.client.get(f"/{malicious_path}")
                # Should return fallback response, not expose file system
                self.assertEqual(response.status_code, 200)
                self.assertEqual(response.json()["message"], f"hello, {malicious_path}!")

    def test_xss_protection_in_responses(self) -> None:
        """Tests XSS protection in API responses."""
        xss_payloads = [
            "<script>alert('xss')</script>",
            "javascript:alert('xss')",
            "<img src=x onerror=alert('xss')>",
            "';alert('xss');//",
            "<svg onload=alert('xss')>",
        ]
        
        for payload in xss_payloads:
            with self.subTest(payload=payload):
                response = self.client.get(f"/{payload}")
                self.assertEqual(response.status_code, 200)
                
                # Response should contain the payload as plain text, not executable
                response_data = response.json()
                self.assertEqual(response_data["message"], f"hello, {payload}!")
                
                # Verify content type is JSON, not HTML
                self.assertEqual(response.headers["content-type"], "application/json")

    def test_sql_injection_protection(self) -> None:
        """Tests SQL injection protection in URL parameters."""
        sql_injection_attempts = [
            "'; DROP TABLE users; --",
            "1' OR '1'='1",
            "admin'--",
            "1; DELETE FROM accounts;",
            "UNION SELECT * FROM passwords",
        ]
        
        for sql_payload in sql_injection_attempts:
            with self.subTest(payload=sql_payload):
                response = self.client.get(f"/{sql_payload}")
                self.assertEqual(response.status_code, 200)
                # Should treat as normal name parameter
                self.assertEqual(response.json()["message"], f"hello, {sql_payload}!")

    def test_http_method_security(self) -> None:
        """Tests security of HTTP method restrictions."""
        dangerous_methods = ['POST', 'PUT', 'DELETE', 'PATCH']
        test_endpoints = ['/', '/testuser', '/api/test']
        
        for method in dangerous_methods:
            for endpoint in test_endpoints:
                with self.subTest(method=method, endpoint=endpoint):
                    response = self.client.request(method, endpoint)
                    # Should return 405 Method Not Allowed
                    self.assertEqual(response.status_code, 405)

    def test_large_payload_protection(self) -> None:
        """Tests protection against large payload attacks."""
        # Test with extremely long path
        long_path = "a" * 10000
        response = self.client.get(f"/{long_path}")
        
        # Should handle gracefully, not crash
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()["message"], f"hello, {long_path}!")

    def test_special_character_handling(self) -> None:
        """Tests handling of special characters in URLs."""
        special_chars = [
            "%00",  # Null byte
            "%0a",  # Line feed
            "%0d",  # Carriage return
            "%09",  # Tab
            "%20",  # Space
            "%2f",  # Forward slash
            "%5c",  # Backslash
        ]
        
        for char in special_chars:
            with self.subTest(char=char):
                response = self.client.get(f"/user{char}test")
                self.assertEqual(response.status_code, 200)
                # Should handle URL decoding safely
                self.assertIn("hello,", response.json()["message"])

    def test_concurrent_request_security(self) -> None:
        """Tests security under concurrent request load."""
        import threading
        import time
        
        results = []
        errors = []
        
        def make_requests():
            try:
                for i in range(10):
                    response = self.client.get(f"/concurrent_test_{i}")
                    results.append(response.status_code)
                    time.sleep(0.01)  # Small delay
            except Exception as e:
                errors.append(str(e))
        
        # Create multiple threads
        threads = [threading.Thread(target=make_requests) for _ in range(5)]
        
        # Start all threads
        for thread in threads:
            thread.start()
        
        # Wait for completion
        for thread in threads:
            thread.join()
        
        # Verify no security errors occurred
        self.assertEqual(len(errors), 0, f"Security errors under load: {errors}")
        self.assertEqual(len(results), 50)  # 5 threads * 10 requests
        
        # All requests should succeed
        for status_code in results:
            self.assertEqual(status_code, 200)


class TestInputValidationEdgeCases(unittest.TestCase):
    """Edge case tests for input validation across both modules."""

    def test_unicode_handling_price_calculator(self) -> None:
        """Tests Unicode character handling in price calculator."""
        unicode_inputs = ['∞', '½', '²', '€', '¥', '₹']
        
        for unicode_char in unicode_inputs:
            with self.subTest(char=unicode_char):
                with self.assertRaises((ValueError, TypeError)):
                    PriceCalculator.calculate_discounted_price(100, unicode_char)

    def test_unicode_handling_spring_service(self) -> None:
        """Tests Unicode character handling in Spring service."""
        app = ReactiveServiceApplication()
        client = TestClient(app.get_app())
        
        unicode_names = ['José', '北京', 'Москва', '🚀', '💰', 'café']
        
        for name in unicode_names:
            with self.subTest(name=name):
                response = client.get(f"/{name}")
                self.assertEqual(response.status_code, 200)
                self.assertEqual(response.json()["message"], f"hello, {name}!")

    def test_empty_and_whitespace_inputs(self) -> None:
        """Tests handling of empty and whitespace-only inputs."""
        app = ReactiveServiceApplication()
        client = TestClient(app.get_app())
        
        whitespace_inputs = ['', ' ', '\t', '\n', '\r', '   ']
        
        for input_val in whitespace_inputs:
            with self.subTest(input=input_val):
                response = client.get(f"/{input_val}")
                self.assertEqual(response.status_code, 200)
                # Should handle whitespace gracefully
                self.assertIn("hello,", response.json()["message"])

    def test_boundary_value_security(self) -> None:
        """Tests security at boundary values."""
        calculator = PriceCalculator()
        
        # Test at exact boundaries
        boundary_tests = [
            (100, 0.0),    # Minimum discount
            (100, 100.0),  # Maximum discount
            (100, 0.0),    # Minimum tax
        ]
        
        for price, rate in boundary_tests:
            with self.subTest(price=price, rate=rate):
                if rate <= 100:  # Discount test
                    result = calculator.calculate_discounted_price(price, rate)
                    self.assertGreaterEqual(result, 0)
                    self.assertLessEqual(result, price)
                else:  # Tax test
                    result = calculator.calculate_final_price_with_tax(price, rate)
                    self.assertGreaterEqual(result, price)


if __name__ == "__main__":
    unittest.main()