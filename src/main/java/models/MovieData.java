package models;

public class MovieData {
    private Movie create;
    private Movie duplicate;
    private Movie toRemove;
    private SearchDataMovies search;

    public Movie getCreate() {
        return create;
    }

    public Movie getDuplicate() {
        return duplicate;
    }

    public Movie getToRemove() {
        return toRemove;
    }

    public SearchDataMovies getSearch() {
        return search;
    }
}
