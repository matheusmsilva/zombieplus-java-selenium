package pages;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.components.Popup;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.List;

public class LeadsPage {

    WebDriver driver;
    Popup popup;

    public LeadsPage (WebDriver driver) {
        this.driver = driver;
        popup = new Popup(driver);
        PageFactory.initElements(driver, this);
    }

    // Elements
    @FindBy(xpath="//button[text()='Aperte o play... se tiver coragem']")
    private WebElement button_PressPlay;

    @FindBy(name="name")
    private WebElement textArea_Name;

    @FindBy(name="email")
    private WebElement textArea_Email;

    @FindBy(xpath="//button[text()='Quero entrar na fila!']")
    private WebElement button_Submit;

    @FindBy(xpath = "//h2[text()='Fila de espera']")
    private WebElement text_HeaderModal;

    @FindBy(id = "swal2-html-container")
    private WebElement text_FeedbackMessage;

    @FindBy(className = "alert")
    private List<WebElement> text_AlertFields;

    // Operations
    public Popup getPopup() {
        return popup;
    }

    public LeadsPage openLeadForm() {
        button_PressPlay.click();
        Wait<WebDriver> wait = new WebDriverWait(this.driver, Duration.ofSeconds(3));
        wait.until(d -> text_HeaderModal.isDisplayed());
        return this;
    }

    public LeadsPage submit(String name, String  email) {
        textArea_Name.sendKeys(name);
        textArea_Email.sendKeys(email);
        button_Submit.click();
        return this;
    }

    public void alertHaveText(Object target) {
        new WebDriverWait(this.driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfAllElements(text_AlertFields));

        if (target instanceof String) {
            Assert.assertEquals(text_AlertFields.size(), 1);
            String actualMessage = text_AlertFields.get(0).getText();
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
