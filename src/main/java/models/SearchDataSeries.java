package models;

import java.util.List;

public class SearchDataSeries {
    private String input;
    private List<String> outputs;
    private List<Serie> data;

    public String getInput() {
        return input;
    }

    public List<String> getOutputs() {
        return outputs;
    }

    public List<Serie> getData() {
        return data;
    }

}
