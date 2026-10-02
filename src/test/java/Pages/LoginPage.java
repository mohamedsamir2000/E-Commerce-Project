package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void setUsername(String username) {
        type(usernameField, username);
    }

    public void setPassword(String password) {
        type(passwordField, password);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public void login(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickLogin();
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
