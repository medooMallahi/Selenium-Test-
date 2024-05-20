import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.net.MalformedURLException;

public class SeleniumOne {
    // init
    private WebDriver driver;
    private WebDriverWait wait;

    // form data
    private final By usernameLocator = By.id("username");
    private final By passwordLocator = By.id("password");

    // buttons login/logout
    private final By submitButtonLocator = By.id("submit");
    private final By logoutButtonLocator = By.xpath("//a[text()='Log out']");

    // page elements
    private final By welcomeMessageLocator = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private final By errorMessageLocator = By.id("error");

    // complex xpath
    private final By complexXpathLocator = By.xpath("//div//button[@id='submit']");

    // Locator for static page title
    private final By staticPageTitleLocator = By.xpath("//h1[contains(text(),'404: Page Not Found')]");

    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, 10);
    }

    private WebElement waitVisibilityAndFindElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void enterCredentials(String username, String password) {
        WebElement usernameElement = waitVisibilityAndFindElement(usernameLocator);
        usernameElement.sendKeys(username);
        WebElement passwordElement = waitVisibilityAndFindElement(passwordLocator);
        passwordElement.sendKeys(password);
    }

    private void clickLoginButton() {
        WebElement submitButtonElement = waitVisibilityAndFindElement(submitButtonLocator);
        submitButtonElement.click();
    }

    private void openPage(String link) {
        driver.get(link);
    }

    private void checkNotificationStatus(By notificationLocator, String text) {
        WebElement notificationElement = waitVisibilityAndFindElement(notificationLocator);
        assertTrue(notificationElement.getText().contains(text));
    }

    @Test
    public void loginWithRightCredentialsTest() {

        openPage("https://practicetestautomation.com/practice-test-login/");


        enterCredentials("student", "Password123"); // Replace with test credentials


        clickLoginButton();


        WebElement welcomeMessageElement = waitVisibilityAndFindElement(welcomeMessageLocator);
        assertTrue(welcomeMessageElement.isDisplayed());


        WebElement logoutButtonElement = waitVisibilityAndFindElement(logoutButtonLocator);
        logoutButtonElement.click();


        WebElement loginElement = waitVisibilityAndFindElement(usernameLocator);
        assertTrue(loginElement.isDisplayed());
    }

    @Test
    public void loginWithInvalidPasswordTest() {

        openPage("https://practicetestautomation.com/practice-test-login/");


        enterCredentials("student", "wrong_password"); // Replace with test credentials


        clickLoginButton();


        checkNotificationStatus(errorMessageLocator, "Your password is invalid!");
    }

    @Test
    public void loginWithInvalidUsernameTest() {

        openPage("https://practicetestautomation.com/practice-test-login/");


        enterCredentials("invalid_user", "Password123"); // Replace with test credentials


        clickLoginButton();


        checkNotificationStatus(errorMessageLocator, "Your username is invalid!");
    }

    @Test
    public void verifyComplexXpath() {

        openPage("https://practicetestautomation.com/practice-test-login/");


        WebElement complexElement = waitVisibilityAndFindElement(complexXpathLocator);
        assertTrue(complexElement.isDisplayed());
    }

    @Test
    public void sendFormAfterLogin() {

        openPage("https://practicetestautomation.com/practice-test-login/");


        enterCredentials("student", "Password123"); // Replace with test credentials
        clickLoginButton();


        WebElement welcomeMessageElement = waitVisibilityAndFindElement(welcomeMessageLocator);
        assertTrue(welcomeMessageElement.isDisplayed());


        openPage("https://practicetestautomation.com/sample-page/");


        WebElement textBox = waitVisibilityAndFindElement(By.id("search-field"));
        textBox.sendKeys("Tableau");

        WebElement submitButton = waitVisibilityAndFindElement(By.xpath("//input[@class='search-submit']")); // Corrected locator
        submitButton.click();


        WebElement successMessage = waitVisibilityAndFindElement(By.xpath("//h1[contains(text(),'No search results for \"Tableau\"')]"));
        assertTrue(successMessage.isDisplayed());


    }

    @Test
    public void testStaticPage() {

        openPage("https://practicetestautomation.com/sample-page/");


        WebElement pageTitle = waitVisibilityAndFindElement(staticPageTitleLocator);
        assertTrue(pageTitle.isDisplayed());
    }

    @Test
    public void readPageTitle() {

        openPage("https://practicetestautomation.com/practice-test-login/");


        String pageTitle = driver.getTitle();
        assertEquals("Test Login | Practice Test Automation", pageTitle);
    }

    @After
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }
}


