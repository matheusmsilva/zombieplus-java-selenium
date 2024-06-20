package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class Popup {

    WebDriver driver;

    public Popup(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Elements
    @FindBy(css = ".swal2-html-container")
    private WebElement popup_Feedback;

    // Operations
    public void haveText(String text) {
        Wait<WebDriver> wait = new WebDriverWait(this.driver, Duration.ofSeconds(3));
        wait.until(d -> popup_Feedback.isDisplayed());

        String actualMessage = popup_Feedback.getText();
        Assert.assertEquals(actualMessage, text);
    }

}
