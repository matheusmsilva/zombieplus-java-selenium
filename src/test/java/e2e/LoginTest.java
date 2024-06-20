package e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MoviesPage;

import java.util.Arrays;

public class LoginTest {

    WebDriver driver;
    LoginPage loginPage;
    MoviesPage moviesPage;

    @BeforeMethod
    public void setup() {
        // Initialize instances
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        moviesPage = new MoviesPage(driver);

        // Access login page
        loginPage.visit();
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void shouldLogInAsAdmin() {
        loginPage.submit("admin@zombieplus.com", "pwd123");
        moviesPage.isLoggedIn("Admin");
    }

    @Test
    public void shouldNotLogInWhenEmailIsInvalid() {
        loginPage.submit("admin.zombieplus.com", "pwd123");
        loginPage.alertHaveText("Email incorreto");
    }

    @Test
    public void shouldNotLogInWhenEmailIsNotInformed() {
        loginPage.submit("", "pwd123");
        loginPage.alertHaveText("Campo obrigatório");
    }

    @Test
    public void shouldNotLogInWhenPasswordIsNotInformed() {
        loginPage.submit("admin@zombieplus.com", "");
        loginPage.alertHaveText("Campo obrigatório");
    }

    @Test
    public void shouldNotLogInWhenEmailAndPasswordAreNotInformed() {
        loginPage.submit("", "");
        loginPage.alertHaveText(Arrays.asList("Campo obrigatório", "Campo obrigatório"));
    }

}
