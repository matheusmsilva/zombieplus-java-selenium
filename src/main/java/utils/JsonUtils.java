package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.MovieData;

import java.io.File;
import java.io.IOException;

public class JsonUtils {
    private static final String JSON_FILE_PATH = "E:/automation/automacao-java/zombieplus/src/test/resources/fixtures/movies.json";

    public static MovieData readMovieData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(JSON_FILE_PATH), MovieData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}