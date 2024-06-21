package e2e;

import api.ApiZombieplus;
import models.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MoviesPage;
import pages.SeriesPage;
import utils.DatabaseUtils;
import utils.JsonUtils;

import java.util.Arrays;

public class SeriesTest extends BaseTest {

    SeriesPage seriesPage;
    MoviesPage moviesPage;
    SerieData serieData;
    LoginPage loginPage;
    ApiZombieplus api;

    @BeforeClass
    public void setupEnvironment() {
        DatabaseUtils.executeSQL("DELETE FROM tvshows;");
    }

    @BeforeMethod
    public void setupTest() throws Exception {
        setupBrowser();
        // Initialize movieData
        serieData = JsonUtils.readSerieData();
        if (serieData == null) {
            throw new RuntimeException("Failed to read movie data from JSON.");
        }
        moviesPage = new MoviesPage(driver);
        loginPage = new LoginPage(driver);
        seriesPage = new SeriesPage(driver);
        api = new ApiZombieplus();
        api.setToken();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void shouldCreateNewSerie() throws Exception {
        Serie serie = serieData.getCreate();
        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");
        // Steps
        seriesPage
                .go()
                .create(serie)
                .getPopup()
                .haveText("A série '" + serie.getTitle() + "' foi adicionada ao catálogo.");
    }

    @Test
    public void shouldRemoveOneSerie() throws Exception {
        // Setup data
        Serie serie = serieData.getToRemove();
        api.postSerie(serie);

        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");

        // Steps
        seriesPage
                .go()
                .remove(serie.getTitle())
                .getPopup()
                .haveText("Série removida com sucesso.");
    }

    @Test
    public void shouldNotCreateNewSerieWhenTitleAlreadyExists() throws Exception {
        // Set data
        Serie serie = serieData.getDuplicate();
        api.postSerie(serie);

        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");

        // Steps
        seriesPage
                .go()
                .create(serie)
                .getPopup()
                .haveText("O título '" + serie.getTitle() + "' já consta em nosso catálogo. Por favor, verifique se há necessidade de atualizações ou correções para este item.");
    }

    @Test
    public void shouldNotCreateNewSerieWhenMandatoryFieldsAreNotFilled() {
        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");

        // Steps
        seriesPage
                .go()
                .goToForm()
                .submit()
                .alertHaveText(Arrays.asList(
                        "Campo obrigatório",
                        "Campo obrigatório",
                        "Campo obrigatório",
                        "Campo obrigatório",
                        "Campo obrigatório (apenas números)"
                ));
    }

    @Test
    public void shouldFilterBySearchingZumbiWord() throws Exception {
        // Set data
        SearchDataSeries series = serieData.getSearch();

        for(Serie s : series.getData()) {
            api.postSerie(s);
        }

        // Login
        loginPage.doLogin();
        moviesPage.isLoggedIn("Admin");

        // Steps
        seriesPage
                .go()
                .search(series.getInput())
                .tableHaveContent(series.getOutputs());
    }

}
