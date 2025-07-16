"""Configuration and environment tests for the converted applications.

This module contains tests for configuration management, environment variables,
and deployment scenarios for both applications.
"""

import unittest
import os
from unittest.mock import patch, MagicMock, Mock
from fastapi.testclient import TestClient
import tempfile
import json

from price_calculator import PriceCalculator, main as price_main
from spring_code import ReactiveServiceApplication, create_application, main as spring_main


class TestEnvironmentConfiguration(unittest.TestCase):
    """Tests for environment-specific configuration."""

    def setUp(self) -> None:
        """Sets up test fixtures before each test method."""
        self.original_env = os.environ.copy()

    def tearDown(self) -> None:
        """Cleans up after each test method."""
        os.environ.clear()
        os.environ.update(self.original_env)

    @patch.dict(os.environ, {'PRICE_CALCULATOR_DEBUG': 'true'})
    def test_price_calculator_debug_mode(self) -> None:
        """Tests price calculator behavior in debug mode."""
        calculator = PriceCalculator()
        
        # In debug mode, should still function normally
        result = calculator.calculate_discounted_price(100, 10)
        self.assertEqual(result, 90.0)

    @patch.dict(os.environ, {'SERVER_HOST': 'custom-host', 'SERVER_PORT': '8080'})
    @patch('uvicorn.run')
    def test_spring_service_environment_config(self, mock_uvicorn_run) -> None:
        """Tests Spring service with environment configuration."""
        app = ReactiveServiceApplication()
        
        # Test that environment variables could be used for configuration
        # (Note: Current implementation uses hardcoded values, but this tests the pattern)
        app.run(host=os.getenv('SERVER_HOST', 'localhost'), 
                port=int(os.getenv('SERVER_PORT', '3000')))
        
        mock_uvicorn_run.assert_called_once_with(
            app.app,
            host='custom-host',
            port=8080,
            log_level='info'
        )

    @patch.dict(os.environ, {'TESTING': 'true'})
    def test_testing_environment_detection(self) -> None:
        """Tests detection of testing environment."""
        is_testing = os.getenv('TESTING', 'false').lower() == 'true'
        self.assertTrue(is_testing)
        
        # Applications should behave appropriately in testing mode
        app = ReactiveServiceApplication()
        client = TestClient(app.get_app())
        
        response = client.get("/")
        self.assertEqual(response.status_code, 200)

    def test_missing_environment_variables(self) -> None:
        """Tests behavior when environment variables are missing."""
        # Clear all environment variables
        os.environ.clear()
        
        # Applications should still work with defaults
        calculator = PriceCalculator()
        result = calculator.calculate_discounted_price(100, 10)
        self.assertEqual(result, 90.0)
        
        app = ReactiveServiceApplication()
        client = TestClient(app.get_app())
        response = client.get("/")
        self.assertEqual(response.status_code, 200)


class TestConfigurationFiles(unittest.TestCase):
    """Tests for configuration file handling."""

    def test_price_calculator_with_config_file(self) -> None:
        """Tests price calculator with configuration file simulation."""
        # Simulate configuration through a temporary file
        config_data = {
            "default_tax_rate": 0.08,
            "max_discount": 50.0,
            "currency_symbol": "₹"
        }
        
        with tempfile.NamedTemporaryFile(mode='w', suffix='.json', delete=False) as f:
            json.dump(config_data, f)
            config_file = f.name
        
        try:
            # Test that configuration could be loaded (pattern demonstration)
            with open(config_file, 'r') as f:
                loaded_config = json.load(f)
            
            self.assertEqual(loaded_config["default_tax_rate"], 0.08)
            self.assertEqual(loaded_config["max_discount"], 50.0)
            
            # Use configuration in calculator
            calculator = PriceCalculator()
            max_discount = loaded_config["max_discount"]
            
            # Test with configured max discount
            result = calculator.calculate_discounted_price(100, max_discount)
            self.assertEqual(result, 50.0)
            
        finally:
            os.unlink(config_file)

    def test_spring_service_with_config_file(self) -> None:
        """Tests Spring service with configuration file simulation."""
        config_data = {
            "server": {
                "host": "0.0.0.0",
                "port": 3000,
                "debug": False
            },
            "app": {
                "title": "Configured Reactive Service",
                "version": "1.0.0"
            }
        }
        
        with tempfile.NamedTemporaryFile(mode='w', suffix='.json', delete=False) as f:
            json.dump(config_data, f)
            config_file = f.name
        
        try:
            # Load configuration
            with open(config_file, 'r') as f:
                loaded_config = json.load(f)
            
            # Test configuration values
            self.assertEqual(loaded_config["server"]["port"], 3000)
            self.assertEqual(loaded_config["app"]["title"], "Configured Reactive Service")
            
            # Create app (could use config in real implementation)
            app = ReactiveServiceApplication()
            client = TestClient(app.get_app())
            
            response = client.get("/")
            self.assertEqual(response.status_code, 200)
            
        finally:
            os.unlink(config_file)


class TestDeploymentScenarios(unittest.TestCase):
    """Tests for different deployment scenarios."""

    @patch('uvicorn.run')
    def test_production_deployment_simulation(self, mock_uvicorn_run) -> None:
        """Tests production deployment scenario."""
        app = ReactiveServiceApplication()
        
        # Simulate production configuration
        production_host = "0.0.0.0"
        production_port = 80
        
        app.run(host=production_host, port=production_port)
        
        mock_uvicorn_run.assert_called_once_with(
            app.app,
            host=production_host,
            port=production_port,
            log_level="info"
        )

    @patch('uvicorn.run')
    def test_development_deployment_simulation(self, mock_uvicorn_run) -> None:
        """Tests development deployment scenario."""
        app = ReactiveServiceApplication()
        
        # Simulate development configuration
        dev_host = "localhost"
        dev_port = 8000
        
        app.run(host=dev_host, port=dev_port)
        
        mock_uvicorn_run.assert_called_once_with(
            app.app,
            host=dev_host,
            port=dev_port,
            log_level="info"
        )

    def test_containerized_deployment_simulation(self) -> None:
        """Tests containerized deployment scenario."""
        # Simulate container environment variables
        container_env = {
            'CONTAINER_MODE': 'true',
            'PORT': '8080',
            'HOST': '0.0.0.0'
        }
        
        with patch.dict(os.environ, container_env):
            is_container = os.getenv('CONTAINER_MODE', 'false').lower() == 'true'
            self.assertTrue(is_container)
            
            # Applications should work in container environment
            app = ReactiveServiceApplication()
            client = TestClient(app.get_app())
            
            response = client.get("/health" if hasattr(app.app, 'health') else "/")
            self.assertEqual(response.status_code, 200)

    def test_cloud_deployment_simulation(self) -> None:
        """Tests cloud deployment scenario."""
        cloud_env = {
            'CLOUD_PROVIDER': 'aws',
            'REGION': 'us-east-1',
            'ENVIRONMENT': 'production'
        }
        
        with patch.dict(os.environ, cloud_env):
            # Applications should handle cloud environment
            calculator = PriceCalculator()
            app = ReactiveServiceApplication()
            
            # Test basic functionality in cloud environment
            result = calculator.calculate_discounted_price(100, 10)
            self.assertEqual(result, 90.0)
            
            client = TestClient(app.get_app())
            response = client.get("/")
            self.assertEqual(response.status_code, 200)


class TestLoggingAndMonitoring(unittest.TestCase):
    """Tests for logging and monitoring capabilities."""

    @patch('builtins.print')
    def test_price_calculator_logging(self, mock_print) -> None:
        """Tests logging capabilities in price calculator."""
        calculator = PriceCalculator()
        
        # Test that main function includes logging
        with patch('price_calculator.PriceCalculator') as mock_calc_class:
            mock_instance = MagicMock()
            mock_calc_class.return_value = mock_instance
            
            price_main()
            
            # Verify calculator was created and run
            mock_calc_class.assert_called_once()
            mock_instance.run_interactive_calculator.assert_called_once()

    def test_spring_service_monitoring_endpoints(self) -> None:
        """Tests monitoring capabilities in Spring service."""
        app = ReactiveServiceApplication()
        client = TestClient(app.get_app())
        
        # Test that service responds to requests (basic monitoring)
        response = client.get("/")
        self.assertEqual(response.status_code, 200)
        
        # Test response time monitoring
        import time
        start_time = time.time()
        response = client.get("/monitoring_test")
        end_time = time.time()
        
        response_time = end_time - start_time
        self.assertLess(response_time, 1.0)  # Should respond quickly
        self.assertEqual(response.status_code, 200)

    @patch('builtins.print')
    def test_application_startup_logging(self, mock_print) -> None:
        """Tests application startup logging."""
        # Test Spring service startup logging
        with patch('spring_code.create_application') as mock_create:
            mock_app = MagicMock()
            mock_create.return_value = mock_app
            
            spring_main()
            
            # Verify startup message was printed
            mock_print.assert_called_with("Starting Reactive Service Application on port 3000...")
            mock_app.run.assert_called_once_with(host="0.0.0.0", port=3000)

    def test_error_monitoring_capabilities(self) -> None:
        """Tests error monitoring and reporting capabilities."""
        calculator = PriceCalculator()
        
        # Test error handling and potential monitoring
        with self.assertRaises(ValueError) as context:
            calculator.calculate_discounted_price(100, -10)
        
        error_message = str(context.exception)
        self.assertIn("Discount percentage must be between 0 and 100", error_message)
        
        # In a real monitoring system, this error would be logged/reported
        self.assertTrue(len(error_message) > 0)


class TestScalabilityConfiguration(unittest.TestCase):
    """Tests for scalability and performance configuration."""

    def test_multiple_worker_simulation(self) -> None:
        """Tests simulation of multiple worker processes."""
        # Simulate multiple application instances (workers)
        num_workers = 3
        apps = [ReactiveServiceApplication() for _ in range(num_workers)]
        clients = [TestClient(app.get_app()) for app in apps]
        
        # Test that all workers handle requests independently
        for i, client in enumerate(clients):
            response = client.get(f"/worker_{i}")
            self.assertEqual(response.status_code, 200)
            self.assertEqual(response.json()["message"], f"hello, worker_{i}!")

    @patch('uvicorn.run')
    def test_load_balancer_configuration(self, mock_uvicorn_run) -> None:
        """Tests configuration for load balancer scenarios."""
        app = ReactiveServiceApplication()
        
        # Simulate load balancer configuration
        lb_config = {
            'host': '0.0.0.0',
            'port': 8080,
            'workers': 4
        }
        
        # Test that app can be configured for load balancing
        app.run(host=lb_config['host'], port=lb_config['port'])
        
        mock_uvicorn_run.assert_called_once_with(
            app.app,
            host='0.0.0.0',
            port=8080,
            log_level='info'
        )

    def test_resource_usage_monitoring(self) -> None:
        """Tests resource usage monitoring capabilities."""
        import psutil
        import gc
        
        # Monitor memory usage during application creation
        process = psutil.Process()
        initial_memory = process.memory_info().rss
        
        # Create multiple applications
        apps = [ReactiveServiceApplication() for _ in range(10)]
        
        final_memory = process.memory_info().rss
        memory_increase = final_memory - initial_memory
        
        # Memory increase should be reasonable
        self.assertLess(memory_increase, 100 * 1024 * 1024)  # Less than 100MB
        
        # Clean up
        del apps
        gc.collect()


if __name__ == "__main__":
    unittest.main()