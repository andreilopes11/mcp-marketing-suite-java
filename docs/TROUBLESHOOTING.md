# Troubleshooting Guide

## Common Issues and Solutions

### 1. Java Version Mismatch

**Error:**
```
UnsupportedClassVersionError: class file version 67.0
```

**Cause:** You're trying to run code compiled with Java 23 using an older Java version.

**Solution:**
```bash
# Check your Java version
java -version

# Should show Java 23.x.x
# If not, install Java 23 or set JAVA_HOME correctly
export JAVA_HOME=/path/to/java23
export PATH=$JAVA_HOME/bin:$PATH
```

### 2. Mockito/ByteBuddy Issues with Java 23

**Error:**
```
Java 23 (67) is not supported by the current version of Byte Buddy
```

**Solution:** Already fixed in `pom.xml`:
- ByteBuddy updated to version 1.15.10
- Spring Boot updated to 3.3.0
- Surefire plugin configured with experimental ByteBuddy flag

### 3. Tests Failing

**To skip tests during build:**
```bash
mvn clean install -DskipTests
```

**To run tests with proper Java 23 support:**
```bash
mvn clean test
```

All tests should pass with the current configuration.

### 4. Application Won't Start - Missing OpenAI Key

**Symptom:** Application starts but AI features return placeholder responses.

**Solution:** This is expected behavior. The application works without an OpenAI API key.

**To enable real AI features:**
```bash
# Linux/Mac
export OPENAI_API_KEY="your-key-here"

# Windows CMD
set OPENAI_API_KEY=your-key-here

# Windows PowerShell
$env:OPENAI_API_KEY="your-key-here"

# Then run the application
mvn spring-boot:run
```

### 5. Port 8080 Already in Use

**Error:**
```
Port 8080 is already in use
```

**Solution 1 - Stop existing process:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

**Solution 2 - Use different port:**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### 6. Maven Build Errors

**Solution - Clean and rebuild:**
```bash
mvn clean install -U
```

The `-U` flag forces update of dependencies.

## Best Practices

### 1. Always Use Maven for Development

✅ **Recommended:**
```bash
mvn spring-boot:run
```

❌ **Not recommended** (requires exact Java version match):
```bash
java -jar target/mcp-marketing-suite-0.1.0-SNAPSHOT.jar
```

### 2. IDE Configuration

**IntelliJ IDEA:**
1. File → Project Structure → Project SDK → Select Java 23
2. File → Settings → Build, Execution, Deployment → Compiler → Java Compiler → Target bytecode version: 23

**Eclipse:**
1. Project → Properties → Java Compiler → Compiler compliance level: 23

**VS Code:**
Add to `.vscode/settings.json`:
```json
{
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-23",
      "path": "/path/to/jdk-23"
    }
  ]
}
```

### 3. Docker Deployment

For production, use Docker to avoid Java version issues:

```bash
mvn clean package -DskipTests
docker build -t mcp-marketing-suite .
docker run -p 8080:8080 -e OPENAI_API_KEY=your-key mcp-marketing-suite
```

## Performance Tips

### 1. Increase Memory for Large Workloads

```bash
export MAVEN_OPTS="-Xmx2g -Xms512m"
mvn spring-boot:run
```

### 2. Enable Parallel Testing

Already configured in `pom.xml`, but you can adjust:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>4</threadCount>
    </configuration>
</plugin>
```

## Getting Help

1. Check logs: `logs/mcp-marketing-suite.log`
2. Enable debug logging: `--debug` or set `logging.level.root=DEBUG` in `application.yml`
3. Review test reports: `target/surefire-reports/`
4. Open an issue on GitHub with:
   - Java version (`java -version`)
   - Maven version (`mvn -version`)
   - Full error stack trace
   - Steps to reproduce

## Verified Working Configuration

✅ **Tested and Working:**
- Java 23.0.1
- Maven 3.9.x
- Spring Boot 3.3.0
- ByteBuddy 1.15.10
- Windows 10/11
- All tests passing (4/4)

