# MCP Marketing Suite - Minimal Implementation
FROM eclipse-temurin:23-jre-alpine

LABEL maintainer="MCP Marketing Suite"
LABEL description="Minimal MCP Server with Java SDK v0.16.0"

WORKDIR /app

# Copy the built JAR file
COPY target/mcp-marketing-suite-*.jar app.jar

# Expose port
EXPOSE 8080

# Health check using MCP endpoint
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/mcp/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

