package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Movie;
import org.testng.Assert;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ApiZombieplus {

    private final String baseUrl = "http://localhost:3333";
    private String token;

    public ApiZombieplus(){
        this.token = null;
    }

    public void setToken() throws Exception {
        // Create body request
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("email", "admin@zombieplus.com");
        loginData.put("password", "pwd123");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(loginData);

        // Create client and request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + "/sessions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send request and assert it works
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(response.statusCode(), 200);

        // Read the response body and set token
        JsonNode responseJson = objectMapper.readTree(response.body());
        token = "Bearer " + responseJson.get("token").asText();
    }

    public String getCompanyIdByName(String companyName) throws Exception {
        // Encode the company name
        String encodedCompanyName = URLEncoder.encode(companyName, StandardCharsets.UTF_8);

        // Create client and request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + "/companies?name=" + encodedCompanyName))
                .header("Authorization", token)
                .build();

        // Send request and parse response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(response.statusCode(), 200);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.body());

        // Assuming the first result is the correct company
        return responseJson.get("data").get(0).get("id").asText();
    }

    public void postMovie(Movie movie) throws Exception {
        String companyId = getCompanyIdByName(movie.getCompany());
        movie.setCompanyId(companyId);

        // Create multipart form data
        String boundary = "Boundary-" + System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"title\"\r\n\r\n").append(movie.getTitle()).append("\r\n");
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"overview\"\r\n\r\n").append(movie.getOverview()).append("\r\n");
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"company_id\"\r\n\r\n").append(movie.getCompanyId()).append("\r\n");
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"release_year\"\r\n\r\n").append(movie.getReleaseYear()).append("\r\n");
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"featured\"\r\n\r\n").append(movie.isFeatured()).append("\r\n");
        sb.append("--").append(boundary).append("--");

        // Create client and request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + "/movies"))
                .header("Authorization", token)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofString(sb.toString()))
                .build();

        // Send request and assert it works
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(response.statusCode(), 201);
    }



}
