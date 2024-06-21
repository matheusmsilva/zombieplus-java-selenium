package pages;

import models.Movie;
import models.Serie;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.components.Popup;

import java.time.Duration;
import java.util.List;

public class SeriesPage {

    WebDriver driver;
    Actions actions;
    Popup popup;
    Wait<WebDriver> wait;

    public SeriesPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(7));
        actions = new Actions(driver);
        popup = new Popup(driver);
        PageFactory.initElements(driver, this);
    }

    // Elements
    @FindBy(css = "a[href$='register']")
    private WebElement button_RegisterSerie;

    @FindBy(css = "a[href$='tvshows']")
    private WebElement button_Series;

    @FindBy(xpath = "//button[@type='button'][text()='Cadastrar']")
    private WebElement button_Submit;

    @FindBy(name = "title")
    private WebElement input_SerieTitle;

    @FindBy(name = "overview")
    private WebElement input_SerieOverview;

    @FindBy(name = "cover")
    private WebElement input_Cover;

    @FindBy(css = ".featured .react-switch")
    private WebElement switch_Featured;

    @FindBy(css = ".confirm-removal")
    private WebElement button_ConfirmRemove;

    @FindBy(className = "alert")
    private List<WebElement> text_AlertFields;

    @FindBy(xpath = "//input[@placeholder='Busque pelo nome']")
    private WebElement input_Search;

    @FindBy(css = ".actions button")
    private WebElement button_Search;

    @FindBy(xpath = "//table")
    private WebElement table_Series;

    @FindBy(name = "seasons")
    private WebElement input_Seasons;

    // Operations
    public SeriesPage go() {
        waitUntilElementClickable(button_Series);
        button_Series.click();
        return this;
    }

    public SeriesPage goToForm() {
        wait.until(d -> button_RegisterSerie.isDisplayed());
        button_RegisterSerie.click();
        return this;
    }

    public SeriesPage create(Serie serie) {
        this.goToForm();
        wait.until(d -> input_SerieTitle.isDisplayed());
        input_SerieTitle.sendKeys(serie.getTitle());
        input_SerieOverview.sendKeys(serie.getOverview());

        this.clickElementWithRetry(By.cssSelector("#select_company_id .react-select__indicator"));
        selectOptionByText(serie.getCompany());

        this.clickElementWithRetry(By.cssSelector("#select_year .react-select__indicator"));
        selectOptionByText(String.valueOf(serie.getReleaseYear()));

        input_Seasons.sendKeys(String.valueOf(serie.getSeasons()));

        String currentDir = System.getProperty("user.dir");
        String imagePath = currentDir + "/src/test/resources/fixtures" + serie.getCover();
        input_Cover.sendKeys(imagePath);

        if (serie.isFeatured()) {
            switch_Featured.click();
        }

        this.submit();
        return this;
    }

    public SeriesPage remove(String serieTitle) {
        wait.until(ExpectedConditions.visibilityOfAllElements(table_Series));
        WebElement button_RemoveMovie = driver.findElement(By.xpath("//td[text()='" + serieTitle + "']/..//td[@class='remove-item']//button"));

        waitUntilElementClickable(button_RemoveMovie);
        button_RemoveMovie.click();

        waitUntilElementClickable(button_ConfirmRemove);
        button_ConfirmRemove.click();
        return this;
    }

    public Popup getPopup() {
        return popup;
    }

    public SeriesPage submit() {
        waitUntilElementClickable(button_Submit);
        actions.moveToElement(button_Submit).perform();
        button_Submit.click();
        return this;
    }

    public SeriesPage search (String text) {
        wait.until(ExpectedConditions.visibilityOfAllElements(input_Search));
        input_Search.sendKeys(text);
        button_Search.click();
        return this;
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

    public void selectOptionByText(String text) {
        try {
            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'react-select__option') and text()='" + text + "']")));
            option.click();
        } catch (StaleElementReferenceException e) {
            // Retry locating the option and clicking
            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'react-select__option') and text()='" + text + "']")));
            option.click();
        }
    }

    private void waitUntilElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void tableHaveContent(List<String> outputs) {
        for (String output : outputs) {
            wait.until(ExpectedConditions.visibilityOfAllElements(table_Series));
            WebElement rowTitle = driver.findElement(By.xpath("//td[text()='" + output + "']"));
            wait.until(ExpectedConditions.visibilityOfAllElements(rowTitle));
        }
    }

    private void clickElementWithRetry(By locator) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == 3) {
                    throw e;
                }
            }
        }
    }
}
