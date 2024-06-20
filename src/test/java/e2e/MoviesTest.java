package e2e;

import models.Movie;
import models.MovieData;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MoviesPage;
import utils.DatabaseUtils;
import utils.JsonUtils;

public class MoviesTest extends BaseTest {

    MoviesPage moviesPage;
    MovieData movieData;

    @BeforeClass
    public void setupEnvironment() {
        DatabaseUtils.executeSQL("DELETE FROM movies;");
    }

    @BeforeMethod
    public void setupTest() {
        setupBrowser();
        // Initialize movieData
        movieData = JsonUtils.readMovieData();
        if (movieData == null) {
            throw new RuntimeException("Failed to read movie data from JSON.");
        }

        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.doLogin();

        // Ensure login was successful
        moviesPage = new MoviesPage(driver);
        moviesPage.isLoggedIn("Admin");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void shouldCreateMovie() throws Exception {
        Movie movie = movieData.getCreate();
        moviesPage.create(movie);
        moviesPage
                .getPopup()
                .haveText("O filme '" + movie.getTitle() + "' foi adicionado ao catálogo.");
    }



}
