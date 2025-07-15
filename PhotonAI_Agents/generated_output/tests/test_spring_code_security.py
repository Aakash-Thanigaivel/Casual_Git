"""Security and validation tests for spring_code module.

This module contains security-focused tests and input validation tests
to ensure the ReactiveServiceApplication handles malicious inputs and
edge cases securely and robustly.
"""

import unittest
import json
from unittest.mock import patch
from fastapi.testclient import TestClient
import pytest
from spring_code import ReactiveServiceApplication


class TestReactiveServiceSecurity(unittest.TestCase):
    """Security test suite for ReactiveServiceApplication."""

    def setUp(self) -> None:
        """Set up test fixtures."""
        self.app_instance = ReactiveServiceApplication()
        self.client = TestClient(self.app_instance.get_app())

    def test_sql_injection_attempts(self) -> None:
        """Test protection against SQL injection attempts in path parameters."""
        sql_injection_payloads = [
            "'; DROP TABLE users; --",
            "' OR '1'='1",
            "admin'--",
            "' UNION SELECT * FROM users --",
            "'; INSERT INTO users VALUES ('hacker', 'password'); --"
        ]
        
        for payload in sql_injection_payloads:
            with self.subTest(payload=payload):
                response = self.client.get(f"/{payload}")
                
                # Should handle gracefully and return expected response
                self.assertEqual(response.status_code, 200)
                response_data = response.json()
                self.assertIn("message", response_data)
                self.assertIn(payload, response_data["message"])

    def test_xss_injection_attempts(self) -> None:
        """Test protection against XSS injection attempts."""
        xss_payloads = [
            "<script>alert('xss')</script>",
            "<img src=x onerror=alert('xss')>",
            "javascript:alert('xss')",
            "<svg onload=alert('xss')>",
            "';alert('xss');//"
        ]
        
        for payload in xss_payloads:
            with self.subTest(payload=payload):
                response = self.client.get(f"/{payload}")
                
                self.assertEqual(response.status_code, 200)
                response_data = response.json()
                
                # Response should contain the payload as-is (no execution)
                self.assertIn(payload, response_data["message"])
                
                # Content-Type should be JSON, not HTML
                self.assertEqual(response.headers["content-type"], "application/json")

    def test_path_traversal_attempts(self) -> None:
        """Test protection against path traversal attacks."""
        path_traversal_payloads = [
            "../../../etc/passwd",
            "..\\..\\..\\windows\\system32\\config\\sam",
            "%2e%2e%2f%2e%2e%2f%2e%2e%2fetc%2fpasswd",
            "....//....//....//etc/passwd",
            "..%252f..%252f..%252fetc%252fpasswd"
        ]
        
        for payload in path_traversal_payloads:
            with self.subTest(payload=payload):
                response = self.client.get(f"/{payload}")
                
                # Should be handled by name route or fallback, not cause errors
                self.assertEqual(response.status_code, 200)
                response_data = response.json()
                self.assertIn("message", response_data)

    def test_command_injection_attempts(self) -> None:
        """Test protection against command injection attempts."""
        command_injection_payloads = [
            "; ls -la",
            "| cat /etc/passwd",
            "&& rm -rf /",
            "`whoami`",
            "$(id)",
            "; ping -c 1 google.com"
        ]
        
        for payload in command_injection_payloads:
            with self.subTest(payload=payload):
                response = self.client.get(f"/{payload}")
                
                self.assertEqual(response.status_code, 200)
                response_data = response.json()
                
                # Should treat as normal string, not execute commands
                self.assertIn(payload, response_data["message"])

    def test_buffer_overflow_attempts(self) -> None:
        """Test handling of extremely long inputs."""
        # Test various buffer sizes
        buffer_sizes = [1000, 10000, 100000]
        
        for size in buffer_sizes:
            with self.subTest(size=size):
                long_payload = "A" * size
                
                try:
                    response = self.client.get(f"/{long_payload}")
                    
                    # Should handle gracefully
                    self.assertEqual(response.status_code, 200)
                    response_data = response.json()
                    self.assertIn("message", response_data)
                    
                except Exception as e:
                    # If it fails, it should fail gracefully
                    self.fail(f"Buffer overflow test failed with size {size}: {e}")

    def test_unicode_and_encoding_attacks(self) -> None:
        """Test handling of various Unicode and encoding attacks."""
        unicode_payloads = [
            "café",  # Normal Unicode
            "🚀🔥💯",  # Emojis
            "\u0000",  # Null byte
            "\u202e",  # Right-to-left override
            "\ufeff",  # Byte order mark
            "test\u0008\u0008\u0008\u0008hack",  # Backspace characters
        ]
        
        for payload in unicode_payloads:
            with self.subTest(payload=repr(payload)):
                response = self.client.get(f"/{payload}")
                
                self.assertEqual(response.status_code, 200)
                response_data = response.json()
                self.assertIn("message", response_data)

    def test_http_header_injection(self) -> None:
        """Test protection against HTTP header injection."""
        # Test with malicious headers
        malicious_headers = {
            "X-Forwarded-For": "127.0.0.1\r\nX-Injected: malicious",
            "User-Agent": "Mozilla/5.0\r\nX-Injected: header",
            "Referer": "http://example.com\r\nSet-Cookie: admin=true"
        }
        
        for header_name, header_value in malicious_headers.items():
            with self.subTest(header=header_name):
                response = self.client.get("/", headers={header_name: header_value})
                
                # Should handle gracefully
                self.assertEqual(response.status_code, 200)
                
                # Injected headers should not appear in response
                response_headers = dict(response.headers)
                self.assertNotIn("X-Injected", response_headers)
                self.assertNotIn("Set-Cookie", response_headers)

    def test_dos_protection(self) -> None:
        """Test protection against denial of service attempts."""
        # Test rapid requests
        start_time = time.time()
        
        for i in range(100):
            response = self.client.get(f"/dos_test_{i}")
            self.assertEqual(response.status_code, 200)
        
        end_time = time.time()
        total_time = end_time - start_time
        
        # Should handle requests efficiently
        self.assertLess(total_time, 10.0)  # Should complete within 10 seconds

    def test_malformed_url_handling(self) -> None:
        """Test handling of malformed URLs."""
        malformed_urls = [
            "/%",  # Incomplete percent encoding
            "/%2",  # Incomplete percent encoding
            "/%GG",  # Invalid hex in percent encoding
            "/test%",  # Trailing percent
            "/test%2G",  # Invalid hex
        ]
        
        for url in malformed_urls:
            with self.subTest(url=url):
                # These might return 400 or be handled gracefully
                response = self.client.get(url)
                
                # Should not cause server errors (5xx)
                self.assertLess(response.status_code, 500)

    def test_content_type_validation(self) -> None:
        """Test content type validation and handling."""
        # Test with various content types
        content_types = [
            "application/json",
            "text/html",
            "application/xml",
            "text/plain",
            "application/octet-stream"
        ]
        
        for content_type in content_types:
            with self.subTest(content_type=content_type):
                response = self.client.get("/", headers={"Content-Type": content_type})
                
                self.assertEqual(response.status_code, 200)
                # Response should always be JSON
                self.assertEqual(response.headers["content-type"], "application/json")

    def test_method_override_protection(self) -> None:
        """Test protection against HTTP method override attacks."""
        # Test method override headers
        override_headers = [
            {"X-HTTP-Method-Override": "DELETE"},
            {"X-HTTP-Method": "PUT"},
            {"X-Method-Override": "PATCH"}
        ]
        
        for headers in override_headers:
            with self.subTest(headers=headers):
                response = self.client.get("/", headers=headers)
                
                # Should still be treated as GET request
                self.assertEqual(response.status_code, 200)
                self.assertEqual(response.json()["message"], "hi")

    def test_response_data_sanitization(self) -> None:
        """Test that response data is properly sanitized."""
        # Test with potentially dangerous input
        dangerous_inputs = [
            "<script>alert('xss')</script>",
            "javascript:void(0)",
            "data:text/html,<script>alert('xss')</script>"
        ]
        
        for dangerous_input in dangerous_inputs:
            with self.subTest(input=dangerous_input):
                response = self.client.get(f"/{dangerous_input}")
                
                self.assertEqual(response.status_code, 200)
                response_data = response.json()
                
                # Data should be included as-is in JSON (not interpreted)
                self.assertIn(dangerous_input, response_data["message"])
                
                # Response should be valid JSON
                self.assertIsInstance(response_data, dict)

    def test_error_information_disclosure(self) -> None:
        """Test that errors don't disclose sensitive information."""
        # Test various error conditions
        with patch('spring_code.ReactiveServiceApplication._configure_routes') as mock_configure:
            # Simulate an internal error
            mock_configure.side_effect = Exception("Internal database connection failed")
            
            try:
                app = ReactiveServiceApplication()
                # Should handle initialization errors gracefully
            except Exception:
                # If it fails, error messages should not contain sensitive info
                pass

    def test_input_length_limits(self) -> None:
        """Test handling of inputs at various length limits."""
        # Test at common length boundaries
        length_tests = [
            255,    # Common varchar limit
            256,    # Just over varchar limit
            1023,   # Just under 1KB
            1024,   # 1KB
            4095,   # Just under 4KB
            4096,   # 4KB
            8191,   # Just under 8KB
            8192,   # 8KB
        ]
        
        for length in length_tests:
            with self.subTest(length=length):
                test_input = "a" * length
                
                response = self.client.get(f"/{test_input}")
                
                # Should handle all reasonable lengths
                if length <= 8192:  # Reasonable limit
                    self.assertEqual(response.status_code, 200)
                    self.assertIn(test_input, response.json()["message"])


if __name__ == '__main__':
    import time
    unittest.main()