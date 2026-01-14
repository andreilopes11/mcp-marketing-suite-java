FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR file
COPY target/mcp-marketing-suite-*.jar app.jar

# Create outputs directory
RUN mkdir -p /app/outputs

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

