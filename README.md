# UI BDD Automation Framework

A comprehensive UI automation testing framework built with Selenium WebDriver, Cucumber BDD, and TestNG. This framework supports both API and UI testing with detailed reporting, screenshot capture on failures, and Jira integration.

## Features

- **BDD Testing**: Uses Cucumber for behavior-driven development with Gherkin syntax
- **Cross-Browser Support**: Supports Chrome, Firefox, Edge, and Safari
- **Parallel Execution**: TestNG parallel execution for faster test runs
- **Detailed Reporting**: ExtentReports for comprehensive test reports
- **Screenshot Capture**: Automatic screenshots on test failures
- **Jira Integration**: Automatic ticket creation for failed tests
- **Custom WebDriver Listener**: Enhanced logging and error handling
- **API Testing**: REST API testing capabilities
- **Database Comparison**: UI and database data comparison tests

## Prerequisites

- Java 8 or higher
- Maven 3.6+
- Git
- Docker (optional, for containerized execution)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/anilds47/ui-bdd-automation-framework.git
cd ui-bdd-automation-framework
```

2. Install dependencies:
```bash
mvn clean install
```

3. Configure properties in `src/main/resources/ConfigFiles/config.properties`:
   - Browser settings
   - URLs
   - Database connections
   - Jira credentials

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── utilities/
│   │           ├── CustomWebDriverListener.java
│   │           ├── DriverFactory.java
│   │           ├── ExtentUtility.java
│   │           ├── JiraUtility.java
│   │           ├── ReusableMethods.java
│   │           ├── ScreenshotUtil.java
│   │           └── TestBaseClass.java
│   └── resources/
│       └── ConfigFiles/
│           └── config.properties
└── test/
    ├── java/
    │   └── com/
    │       ├── runners/
    │       │   └── CucumberTestRunner.java
    │       └── stepdefinitions/
    │           ├── ApiSteps.java
    │           ├── CucumberHooks.java
    │           └── UISteps.java
    └── resources/
        └── features/
            ├── ApiTests.feature
            ├── UIDBComparisonTests.feature
            └── UISearchTests.feature
```

## Usage

### Running BDD Tests

Execute all BDD tests using TestNG:
```bash
mvn test -DsuiteXmlFile=testng-bdd.xml
```

Or run specific feature files:
```bash
mvn test -Dcucumber.options="--tags @smoke"
```

### Running with Different Browsers

Set browser in config.properties or via system property:
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
```

### Parallel Execution

Tests run in parallel by default. Configure thread count in testng-bdd.xml.

## Configuration

### config.properties

```properties
# Browser Configuration
browser=chrome
headless=false

# URLs
baseUrl=https://example.com
apiBaseUrl=https://api.example.com

# Database Configuration
dbUrl=jdbc:mysql://localhost:3306/testdb
dbUsername=user
dbPassword=password

# Jira Configuration
jiraUrl=https://yourcompany.atlassian.net
jiraUsername=username
jiraPassword=password
jiraProjectKey=PROJ
```

## Test Execution

### TestNG BDD Suite

The `testng-bdd.xml` file configures the Cucumber test runner:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<suite name="BDD Test Suite" parallel="methods" thread-count="3">
    <test name="BDD Tests">
        <classes>
            <class name="com.runners.CucumberTestRunner"/>
        </classes>
    </test>
</suite>
```

### Cucumber Features

Feature files are located in `src/test/resources/features/`:

- `ApiTests.feature`: API testing scenarios
- `UISearchTests.feature`: UI search functionality tests
- `UIDBComparisonTests.feature`: UI and database data comparison tests

### Custom WebDriver Listener

The `CustomWebDriverListener` provides:
- Detailed logging of WebDriver actions
- Automatic screenshot capture on failures
- Jira ticket creation for failed tests
- Test execution summary generation

## Reporting

- **ExtentReports**: HTML reports in `HtmlReports/Reports/`
- **Test Summary**: `test-summary.html` and `test-summary.txt`
- **Screenshots**: Failed test screenshots in `HtmlReports/FailedScreenshots/`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Best Practices

- Use Page Object Model for UI tests
- Implement proper waits using WebDriverWait
- Add meaningful scenario names and descriptions
- Use tags for test categorization (@smoke, @regression, etc.)
- Keep step definitions reusable and maintainable

## Troubleshooting

### Common Issues

1. **WebDriver not found**: Ensure browser drivers are in PATH or use WebDriverManager
2. **Tests failing intermittently**: Check for proper wait implementations
3. **Jira integration not working**: Verify Jira credentials and project permissions
4. **Parallel execution issues**: Ensure thread-safe implementations

### Debug Mode

Run tests in debug mode:
```bash
mvn test -DforkCount=0 -DreuseForks=false
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the GitHub repository
- Check the troubleshooting section
- Review the code comments for implementation details</content>
<parameter name="filePath">C:\Users\anild\OneDrive\Documents\ui_automation\README.md
