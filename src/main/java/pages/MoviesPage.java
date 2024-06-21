package pages;

import models.Movie;
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

public class MoviesPage {

    WebDriver driver;
    Actions actions;
    Popup popup;
    Wait<WebDriver> wait;

    public MoviesPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(7));
        actions = new Actions(driver);
        popup = new Popup(driver);
        PageFactory.initElements(driver, this);
    }

    // Elements
    @FindBy(css = "a[href$='register']")
    private WebElement button_RegisterMovie;

    @FindBy(xpath = "//button[@type='button'][text()='Cadastrar']")
    private WebElement button_Submit;

    @FindBy(name = "title")
    private WebElement input_MovieTitle;

    @FindBy(name = "overview")
    private WebElement input_MovieOverview;

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
    private WebElement table_Movies;

    // Operations
    public MoviesPage goToForm() {
        wait.until(d -> button_RegisterMovie.isDisplayed());
        button_RegisterMovie.click();
        return this;
    }

    public MoviesPage create(Movie movie) {
        this.goToForm();
        wait.until(d -> input_MovieTitle.isDisplayed());
        input_MovieTitle.sendKeys(movie.getTitle());
        input_MovieOverview.sendKeys(movie.getOverview());

        WebElement select_MovieProvider = driver.findElement(By.cssSelector("#select_company_id .react-select__indicator"));
        WebElement select_MovieYear = driver.findElement(By.cssSelector("#select_year .react-select__indicator"));

        waitUntilElementClickable(select_MovieProvider);
        select_MovieProvider.click();
        selectOptionByText(movie.getCompany());

        waitUntilElementClickable(select_MovieYear);
        select_MovieYear.click();
        selectOptionByText(String.valueOf(movie.getReleaseYear()));

        String currentDir = System.getProperty("user.dir");
        String imagePath = currentDir + "/src/test/resources/fixtures" + movie.getCover();
        input_Cover.sendKeys(imagePath);

        if (movie.isFeatured()) {
            switch_Featured.click();
        }

        this.submit();
        return this;
    }

    public MoviesPage remove(String movieTitle) {
        WebElement button_RemoveMovie = driver.findElement(By.xpath("//td[text()='" + movieTitle + "']/..//td[@class='remove-item']//button"));
        wait.until(ExpectedConditions.elementToBeClickable(button_RemoveMovie));

        button_RemoveMovie.click();
        wait.until(ExpectedConditions.elementToBeClickable(button_ConfirmRemove));
        button_ConfirmRemove.click();
        return this;
    }

    public Popup getPopup() {
        return popup;
    }

    public void isLoggedIn(String username) {
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='logged-user']//small")));
        String actualUser = usernameElement.getText();
        Assert.assertEquals(actualUser, "Olá, " + username);
    }

    public MoviesPage submit() {
        wait.until(ExpectedConditions.elementToBeClickable(button_Submit));
        actions.moveToElement(button_Submit).perform();
        button_Submit.click();
        return this;
    }

    public MoviesPage search (String text) {
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
            wait.until(ExpectedConditions.visibilityOfAllElements(table_Movies));
            WebElement rowTitle = driver.findElement(By.xpath("//td[text()='" + output + "']"));
            wait.until(ExpectedConditions.visibilityOfAllElements(rowTitle));
        }
    }
}
