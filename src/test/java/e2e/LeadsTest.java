package e2e;

import org.testng.annotations.*;
import utils.DatabaseUtils;
import pages.LeadsPage;
import com.github.javafaker.Faker;
import org.testng.Assert;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Locale;

public class LeadsTest extends BaseTest {

    Faker faker;
    LeadsPage leadsPage;

    @BeforeClass
    public void setupEnvironment() {
        DatabaseUtils.executeSQL("DELETE FROM leads;");
    }

    @BeforeMethod
    public void setupTest() {
        setupBrowser();
        // Initialize instances
        faker = new Faker(new Locale("pt-BR"));
        leadsPage = new LeadsPage(driver);
        // Access landing page
        driver.get("http://localhost:3000/");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void shouldRegisterNewLead() {
        // Set test data
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        // Steps
        leadsPage
                .openLeadForm()
                .submit(name, email)
                .getPopup()
                .haveText("Agradecemos por compartilhar seus dados conosco. Em breve, nossa equipe entrará em contato.");
    }

    @Test
    public void shouldNotRegisterNewLeadWhenEmailAlreadyExists() throws Exception {
        // Set test data
        String leadName = faker.name().fullName();
        String leadEmail = faker.internet().emailAddress();

        // Create new lead through API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:3333/leads"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"" + leadName + "\", \"email\":\"" + leadEmail + "\"}"))
                .build();

        // Assert Lead created successfully
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(response.statusCode(), 201);

        // Steps
        leadsPage
                .openLeadForm()
                .submit(leadName, leadEmail)
                .getPopup()
                .haveText("Verificamos que o endereço de e-mail fornecido já consta em nossa lista de espera. Isso significa que você está um passo mais perto de aproveitar nossos serviços.");
    }

    @Test
    public void shouldNotRegisterNewLeadWhenEmailIsInvalid() {
        // Set test data
        String leadName = faker.name().fullName();

        // Steps
        leadsPage
                .openLeadForm()
                .submit(leadName, "matheus.mota.com")
                .alertHaveText("Email incorreto");
    }

    @Test
    public void shouldNotRegisterNewLeadWhenNameIsNotInformed() {
        // Set test data
        String leadEmail = faker.internet().emailAddress();

        // Steps
        leadsPage
                .openLeadForm()
                .submit("", leadEmail)
                .alertHaveText("Campo obrigatório");
    }

    @Test
    public void shouldNotRegisterNewLeadWhenEmailIsNotInformed() {
        // Set test data
        String leadName = faker.name().fullName();

        // Steps
        leadsPage
                .openLeadForm()
                .submit(leadName, "")
                .alertHaveText("Campo obrigatório");
    }

    @Test
    public void shouldNotRegisterNewLeadWhenEmailAndNameAreNotInformed() {
        // Steps
        leadsPage
                .openLeadForm()
                .submit("", "")
                .alertHaveText(Arrays.asList("Campo obrigatório", "Campo obrigatório"));
    }
}
