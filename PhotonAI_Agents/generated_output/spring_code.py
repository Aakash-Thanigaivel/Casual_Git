"""Reactive web service application using Spring Boot 6.1.

This module provides a reactive web service with functional routing,
following Google Python Style Guidelines and Spring Boot 6.1 best practices.
"""

from __future__ import annotations

from typing import Dict, Any
import asyncio

from flask import Flask, request, jsonify
from flask_restx import Api, Resource
import uvicorn
from fastapi import FastAPI, Path
from fastapi.responses import JSONResponse


class ReactiveServiceApplication:
    """Reactive web service application with functional routing."""

    def __init__(self) -> None:
        """Initialize the reactive service application."""
        self.app = FastAPI(
            title="Reactive Service API",
            description="A reactive web service with functional routing",
            version="1.0.0"
        )
        self._setup_routes()

    def _setup_routes(self) -> None:
        """Set up the application routes."""
        
        @self.app.get("/")
        async def root() -> JSONResponse:
            """Root endpoint that returns a greeting.
            
            Returns:
                JSONResponse containing a simple greeting message.
            """
            return JSONResponse(content="hi")

        @self.app.get("/{name}")
        async def greet_user(name: str = Path(..., description="User name")) -> JSONResponse:
            """Greet a specific user by name.
            
            Args:
                name: The name of the user to greet.
                
            Returns:
                JSONResponse containing a personalized greeting.
            """
            return JSONResponse(content=f"hello, {name}!")

        @self.app.get("/{path:path}")
        async def fallback(path: str) -> JSONResponse:
            """Fallback endpoint for unmatched routes.
            
            Args:
                path: The requested path that didn't match other routes.
                
            Returns:
                JSONResponse containing a fallback message.
            """
            return JSONResponse(content="fallback")

    def get_app(self) -> FastAPI:
        """Get the FastAPI application instance.
        
        Returns:
            The configured FastAPI application.
        """
        return self.app


def create_app() -> FastAPI:
    """Application factory function.
    
    Returns:
        Configured FastAPI application instance.
    """
    service = ReactiveServiceApplication()
    return service.get_app()


def main() -> None:
    """Main function to run the reactive service application."""
    app = create_app()
    
    # Configuration equivalent to Spring Boot's server.port=3000
    config = {
        "host": "0.0.0.0",
        "port": 3000,
        "log_level": "info",
        "reload": True
    }
    
    print("Starting Reactive Service Application on port 3000...")
    uvicorn.run(app, **config)


if __name__ == "__main__":
    main()