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
- **Mobile Testing**: Appium support for Android and iOS testing

## Prerequisites

- Java 8 or higher
- Maven 3.6+
- Git
- Docker (required for MCP server functionality)

## MCP Server Setup

This framework integrates with Model Context Protocol (MCP) servers for enhanced automation capabilities, including GitHub operations and Selenium WebDriver management.

### Docker Installation

1. **Install Docker Desktop**:
   - Download from [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
   - Follow the installation wizard for your operating system
   - Start Docker Desktop after installation

2. **Verify Docker Installation**:
   ```bash
   docker --version
   docker run hello-world
   ```

### MCP Configuration

The MCP servers are configured in `mcp.json` located at:
- Windows: `%APPDATA%\github-copilot\intellij\mcp.json`
- Linux/Mac: `~/.config/github-copilot/intellij/mcp.json`

#### Current MCP Configuration:

```json
{
    "servers": {
        "github": {
            "command": "docker",
            "args": [
                "run",
                "-i",
                "--rm",
                "-e",
                "GITHUB_PERSONAL_ACCESS_TOKEN",
                "-e",
                "GITHUB_API_HOST",
                "ghcr.io/github/github-mcp-server"
            ],
            "env": {
                "GITHUB_PERSONAL_ACCESS_TOKEN": "your_github_token_here",
                "GITHUB_API_HOST": "api.github.com"
            }
        },
        "selenium": {
            "command": "npx",
            "args": ["-y", "@angiejones/mcp-selenium@latest"]
        }
    }
}
```

#### Setup Steps:

1. **Generate GitHub Personal Access Token**:
   - Go to GitHub Settings → Developer settings → Personal access tokens
   - Create a new token with `repo`, `workflow`, and `read:org` permissions
   - Copy the token and replace `your_github_token_here` in the MCP config

2. **Verify MCP Server Connection**:
   - Restart your IDE (IntelliJ IDEA with GitHub Copilot)
   - The MCP servers should automatically start when needed

#### MCP Server Features:

- **GitHub Server**: Enables repository operations, pull request management, issue tracking
- **Selenium Server**: Provides advanced browser automation capabilities and diagnostics

### Troubleshooting MCP Setup

- **Docker not running**: Ensure Docker Desktop is started
- **Token expired**: Regenerate GitHub token and update `mcp.json`
- **Permission denied**: Verify token has required scopes
- **Server connection failed**: Check Docker network connectivity

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

## Mobile Testing with Appium

This framework now supports mobile application testing using Appium for both Android and iOS platforms.

### Mobile Testing Prerequisites

- **Node.js** (for Appium server)
- **Appium Server**: Install globally via npm
  ```bash
  npm install -g appium
  npm install -g appium-doctor
  ```
- **Android SDK** (for Android testing)
- **Xcode** (for iOS testing on macOS)
- **Mobile Device/Emulator**: Connected Android device or iOS simulator

### Setting up Appium

1. **Install Appium Server**:
   ```bash
   npm install -g appium
   appium --version
   ```

2. **Install UiAutomator2 Driver** (for Android):
   ```bash
   appium driver install uiautomator2
   ```

3. **Install XCUITest Driver** (for iOS):
   ```bash
   appium driver install xcuitest
   ```

4. **Start Appium Server**:
   ```bash
   appium --address 127.0.0.1 --port 4723
   ```

### Mobile Device Setup

#### Android Setup:
1. **Enable Developer Options** on your Android device
2. **Enable USB Debugging**
3. **Connect device** via USB or start emulator
4. **Verify device connection**:
   ```bash
   adb devices
   ```

#### iOS Setup (macOS only):
1. **Install Xcode** from App Store
2. **Start iOS Simulator** or connect physical device
3. **Verify device**:
   ```bash
   xcrun simctl list devices
   ```

### Mobile Test Configuration

Update `config.properties` with mobile settings:

```properties
# Mobile Configuration
MobilePlatform=Android
MobileDeviceUDID=emulator-5554
AppiumServerUrl=http://127.0.0.1:4723/wd/hub
MobileDeviceName=AndroidDevice
MobileAppPackage=com.example.app
MobileAppActivity=.MainActivity
MobileAppPath=/path/to/your/app.apk
MobileBrowser=Chrome
MobileAutomationName=UiAutomator2
MobileNoReset=true
MobileNewCommandTimeout=300
```

### Running Mobile Tests

Execute mobile tests using tags:
```bash
mvn test -Dcucumber.options="--tags @mobile"
```

Or run specific mobile scenarios:
```bash
mvn test -Dcucumber.options="--tags @mobile and @smoke"
```

### Mobile Step Definitions

Available mobile-specific steps in `MobileSteps.java`:

- Launch mobile app/browser
- Tap on elements
- Enter text in fields
- Scroll and swipe gestures (up, down, left, right)
- Long press actions with custom duration
- Element verification (visibility, text content)
- Navigation (back, refresh, URL navigation)
- Keyboard operations (hide, show, verify state)
- Custom wait operations

### MobileUtils Class

The `MobileUtils` class in `com.utilities` package provides reusable methods for mobile operations:

```java
// Launch mobile app using configuration from config.properties
MobileUtils mobileUtils = MobileUtils.launchMobileAppFromConfig("Android", "emulator-5554");

// Or launch with custom parameters
MobileUtils mobileUtils = MobileUtils.launchMobileApp("Android", "MyDevice", "emulator-5554", "http://127.0.0.1:4723/wd/hub");

// Launch mobile browser
MobileUtils mobileUtils = MobileUtils.launchMobileBrowserFromConfig("Android", "emulator-5554");

// Basic interactions
mobileUtils.tapElement("id", "button_id");
mobileUtils.enterText("Hello World", "xpath", "//input[@name='search']");

// Gestures
mobileUtils.scrollDown();
mobileUtils.scrollUp();
mobileUtils.swipeLeft();
mobileUtils.swipeRight();

// Long press with custom duration
mobileUtils.longPressElement("accessibilityId", "menu_item", 3000);

// Element verification
boolean isVisible = mobileUtils.isElementVisible("id", "element_id");
String text = mobileUtils.getElementText("xpath", "//div[@class='title']");

// Keyboard operations
mobileUtils.hideKeyboard();
boolean keyboardShown = mobileUtils.isKeyboardShown();

// Custom waits
WebElement element = mobileUtils.waitForElement("css", ".loading", 10);
```

### Mobile Test Features

The framework supports:

- **Native App Testing**: Test mobile applications directly
- **Mobile Web Testing**: Test websites on mobile browsers
- **Gesture Support**: Swipe, scroll, long press, tap
- **Cross-Platform**: Android and iOS support
- **Device Farm Ready**: Compatible with cloud device farms
- **Reusable Utilities**: MobileUtils class for common operations

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
- Review the code comments for implementation details
