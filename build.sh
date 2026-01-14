#!/bin/bash

# Build script for MCP Marketing Suite

echo "Building MCP Marketing Suite..."

# Clean previous builds
echo "Cleaning previous builds..."
mvn clean

# Run tests
echo "Running tests..."
mvn test

if [ $? -ne 0 ]; then
    echo "Tests failed! Aborting build."
    exit 1
fi

# Build the project
echo "Building JAR..."
mvn package -DskipTests

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

echo "Build completed successfully!"
echo "JAR file: target/mcp-marketing-suite-*.jar"

