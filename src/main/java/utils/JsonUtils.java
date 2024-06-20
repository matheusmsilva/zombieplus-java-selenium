package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.MovieData;

import java.io.File;
import java.io.IOException;

public class JsonUtils {
    private static final String JSON_FILE_PATH = "src/test/resources/fixtures";

    public static MovieData readMovieData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(JSON_FILE_PATH + "/movies.json"), MovieData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}