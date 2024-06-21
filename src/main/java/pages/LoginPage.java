package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.components.Popup;

import java.time.Duration;
import java.util.List;

public class LoginPage {

    WebDriver driver;
    Wait<WebDriver> wait;
    Popup popup;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        popup = new Popup(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }

    // Elements
    @FindBy(css = ".login-form")
    private WebElement modal_Login;

    @FindBy(xpath = "//input[@placeholder='E-mail']")
    private WebElement input_Email;

    @FindBy(xpath = "//input[@placeholder='Senha']")
    private WebElement input_Password;

    @FindBy(xpath = "//button[text()='Entrar']")
    private WebElement button_LogIn;

    @FindBy(css = "span[class*='alert']")
    private List<WebElement> text_AlertFields;

    // Operations
    public LoginPage visit() {
        driver.get("http://localhost:3000/admin/login");
        wait.until(d -> modal_Login.isDisplayed());
        return this;
    }

    public Popup getPopup() {
        return popup;
    }

    public void doLogin() {
        this.visit();
        this.submit("admin@zombieplus.com", "pwd123");
    }

    public void submit(String email, String password) {
        input_Email.sendKeys(email);
        input_Password.sendKeys(password);
        button_LogIn.click();
    }

    public void alertHaveText(Object target) {
        wait.until(ExpectedConditions.visibilityOfAllElements(text_AlertFields));

        if (target instanceof String) {
            Assert.assertEquals(text_AlertFields.size(), 1);
            String actualMessage = text_AlertFields.getFirst().getText();
            Assert.assertEquals(actualMessage, target);
        } else if (target instanceof List) {
            List<String> expectedMessages = (List<String>) target;
            Assert.assertEquals(text_AlertFields.size(), expectedMessages.size());

            for (int i = 0; i < expectedMessages.size(); i++) {
                String actualMessage = text_AlertFields.get(i).getText();
                Assert.assertEquals(actualMessage, expectedMessages.get(i));
            }
        } else {
            throw new IllegalArgumentException("Unsupported type for alert text: " + target.getClass().getName());
        }
    }

}
