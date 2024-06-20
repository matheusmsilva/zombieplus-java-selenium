package pages;

import models.Movie;
import org.openqa.selenium.By;
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

public class MoviesPage {

    WebDriver driver;
    Actions actions;
    Popup popup;

    public MoviesPage(WebDriver driver) {
        this.driver = driver;
        actions = new Actions(driver);
        popup = new Popup(driver);
        PageFactory.initElements(driver, this);
    }

    // Elements
    @FindBy(css = "a[href$='register']")
    private WebElement btn_RegisterMovie;

    @FindBy(xpath = "//button[@type='button'][text()='Cadastrar']")
    private WebElement btn_Submit;

    @FindBy(name = "title")
    private WebElement input_MovieTitle;

    @FindBy(name = "overview")
    private WebElement input_MovieOverview;

    @FindBy(css = "#select_company_id .react-select__indicator")
    private WebElement select_MovieProvider;

    @FindBy(css = "#select_year .react-select__indicator")
    private WebElement select_MovieYear;

    @FindBy(name = "cover")
    private WebElement input_Cover;

    @FindBy(css = ".featured .react-switch")
    private WebElement switch_Featured;

    // Operations
    public void goToForm() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(d -> btn_RegisterMovie.isDisplayed());
        btn_RegisterMovie.click();
    }

    public void create(Movie movie) {
        this.goToForm();
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(d -> input_MovieTitle.isDisplayed());
        input_MovieTitle.sendKeys(movie.getTitle());
        input_MovieOverview.sendKeys(movie.getOverview());

        wait.until(ExpectedConditions.elementToBeClickable(select_MovieProvider));
        select_MovieProvider.click();
        WebElement providerOption = driver.findElement(By.xpath("//div[contains(@class, 'react-select__option') and text()='" + movie.getCompany() + "']"));
        providerOption.click();

        wait.until(ExpectedConditions.elementToBeClickable(select_MovieYear));
        select_MovieYear.click();
        WebElement yearOption = driver.findElement(By.xpath("//div[contains(@class, 'react-select__option') and text()='" + movie.getReleaseYear() + "']"));
        yearOption.click();

        String currentDir = System.getProperty("user.dir");
        String imagePath = currentDir + "/src/test/resources/fixtures" + movie.getCover();
        input_Cover.sendKeys(imagePath);

        if (movie.isFeatured()) {
            switch_Featured.click();
        }

        wait.until(ExpectedConditions.elementToBeClickable(btn_Submit));
        actions.moveToElement(btn_Submit).perform();
        btn_Submit.click();
    }

    public Popup getPopup() {
        return popup;
    }

    public void isLoggedIn(String username) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='logged-user']//small")));
        String actualUser = usernameElement.getText();
        Assert.assertEquals(actualUser, "Olá, " + username);
    }
}
