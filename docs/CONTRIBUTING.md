# Contributing to MCP Marketing Suite

Thank you for your interest in contributing! This document provides guidelines for contributing to the project.

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/yourusername/mcp-marketing-suite-java.git`
3. Create a feature branch: `git checkout -b feature/your-feature-name`
4. Make your changes
5. Run tests: `mvn test`
6. Commit your changes: `git commit -m "Add your feature"`
7. Push to your fork: `git push origin feature/your-feature-name`
8. Open a Pull Request

## Code Style

- Follow standard Java conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public APIs
- Keep methods focused and small
- Write unit tests for new functionality

## Project Structure

```
src/main/java/com/mcp/marketing/
├── config/          # Configuration classes
├── controller/      # REST API controllers
├── service/         # Business logic layer
├── agent/           # AI agent implementations
├── mcp/             # MCP server and resources
├── tool/            # Marketing tools (ads, CRM, SEO)
├── model/           # Domain models and DTOs
└── observability/   # Logging and tracing
```

## Adding New Tools

To add a new marketing tool:

1. Create a new class in `com.mcp.marketing.tool`
2. Inject required dependencies (ObservabilityService, MarketingProperties)
3. Implement your tool logic
4. Add integration to `MarketingService`
5. Create corresponding controller endpoint
6. Write unit tests
7. Update API documentation

Example:
```java
@Component
@RequiredArgsConstructor
public class MyNewTool {
    private final ObservabilityService observability;
    private final MarketingProperties properties;
    
    public Map<String, Object> execute(String input) {
        return observability.traceOperation("my_new_tool", () -> {
            // Your logic here
            return result;
        });
    }
}
```

## Adding New MCP Resources

To add a new MCP resource:

1. Create a model class in `com.mcp.marketing.model`
2. Add resource provider method in `McpResourceProvider`
3. Initialize mock data in the constructor
4. Use the resource in your tools

## Testing

- Write unit tests for all new functionality
- Use `@WebMvcTest` for controller tests
- Use `@SpringBootTest` for integration tests
- Mock external dependencies
- Aim for >80% code coverage

Run tests:
```bash
mvn test
```

Run with coverage:
```bash
mvn clean test jacoco:report
```

## Documentation

- Update README.md for major features
- Update API.md for new endpoints
- Add JavaDoc comments for public methods
- Include examples in documentation

## Pull Request Guidelines

- Keep PRs focused on a single feature or fix
- Write clear PR descriptions
- Reference related issues
- Ensure all tests pass
- Update documentation as needed
- Request review from maintainers

## Code Review Process

1. Automated tests must pass
2. At least one maintainer approval required
3. Address all review comments
4. Squash commits before merge

## Reporting Issues

When reporting issues, include:
- Clear description of the problem
- Steps to reproduce
- Expected vs actual behavior
- Environment details (Java version, OS)
- Relevant logs or error messages

## Feature Requests

For feature requests:
- Check existing issues first
- Describe the use case
- Explain why it's valuable
- Propose a solution if possible

## Community

- Be respectful and professional
- Help others in discussions
- Share your use cases and feedback
- Contribute to documentation

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

