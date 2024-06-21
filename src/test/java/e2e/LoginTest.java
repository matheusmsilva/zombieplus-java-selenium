package e2e;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MoviesPage;

import java.util.Arrays;

public class LoginTest extends BaseTest {

    LoginPage loginPage;
    MoviesPage moviesPage;

    @BeforeMethod
    public void setupTest() {
        setupBrowser();
        // Initialize instances
        loginPage = new LoginPage(driver);
        moviesPage = new MoviesPage(driver);

        // Access login page
        loginPage.visit();
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
    public void shouldNotLogInWhenPasswordIsIncorrect() {
        loginPage.submit("admin@zombieplus.com", "abc123");
        loginPage
                .getPopup()
                .haveText("Ocorreu um erro ao tentar efetuar o login. Por favor, verifique suas credenciais e tente novamente.");
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
