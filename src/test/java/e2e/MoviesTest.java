package e2e;

import api.ApiZombieplus;
import models.Movie;
import models.MovieData;
import models.SearchDataMovies;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MoviesPage;
import utils.DatabaseUtils;
import utils.JsonUtils;

import java.util.Arrays;

public class MoviesTest extends BaseTest {

    MoviesPage moviesPage;
    MovieData movieData;
    LoginPage loginPage;
    ApiZombieplus api;

    @BeforeClass
    public void setupEnvironment() {
        DatabaseUtils.executeSQL("DELETE FROM movies;");
    }

    @BeforeMethod
    public void setupTest() throws Exception {
        setupBrowser();
        // Initialize movieData
        movieData = JsonUtils.readMovieData();
        if (movieData == null) {
            throw new RuntimeException("Failed to read movie data from JSON.");
        }
        moviesPage = new MoviesPage(driver);
        loginPage = new LoginPage(driver);
        api = new ApiZombieplus();
        api.setToken();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void shouldCreateMovie() throws Exception {
        Movie movie = movieData.getCreate();
        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");
        // Steps
        moviesPage
                .create(movie)
                .getPopup()
                .haveText("O filme '" + movie.getTitle() + "' foi adicionado ao catálogo.");
    }

    @Test
    public void shouldRemoveOneMovie() throws Exception {
        // Setup data
        Movie movie = movieData.getToRemove();
        api.postMovie(movie);

        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");

        // Steps
        moviesPage
                .remove(movie.getTitle())
                .getPopup()
                .haveText("Filme removido com sucesso.");
    }

    @Test
    public void shouldNotCreateNewMovieWhenTitleAlreadyExists() throws Exception {
        // Set data
        Movie movie = movieData.getDuplicate();
        api.postMovie(movie);

        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");

        // Steps
        moviesPage
                .create(movie)
                .getPopup()
                .haveText("O título '" + movie.getTitle() + "' já consta em nosso catálogo. Por favor, verifique se há necessidade de atualizações ou correções para este item.");
    }

    @Test
    public void shouldNotCreateNewMovieWhenMandatoryFieldsAreNotFilled() {
        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");

        // Steps
        moviesPage
                .goToForm()
                .submit()
                .alertHaveText(Arrays.asList(
                    "Campo obrigatório",
                    "Campo obrigatório",
                    "Campo obrigatório",
                    "Campo obrigatório"
               ));
    }

    @Test
    public void shouldFilterBySearchingZumbiWord() throws Exception {
        // Set data
        SearchDataMovies movies = movieData.getSearch();

        for(Movie m : movies.getData()) {
            api.postMovie(m);
        }

        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");

        // Steps
        moviesPage
                .search(movies.getInput())
                .tableHaveContent(movies.getOutputs());
    }



}
