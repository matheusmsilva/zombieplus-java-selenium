package models;

public class MovieData {
    private Movie create;
    private Movie duplicate;
    private Movie toRemove;
    private SearchData search;

    public Movie getCreate() {
        return create;
    }

    public Movie getDuplicate() {
        return duplicate;
    }

    public Movie getToRemove() {
        return toRemove;
    }

    public SearchData getSearch() {
        return search;
    }
}
