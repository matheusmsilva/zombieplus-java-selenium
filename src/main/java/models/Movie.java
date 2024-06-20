package models;

public class Movie {
    private String title;
    private String overview;
    private String company;
    private int releaseYear;
    private boolean featured;
    private String cover;

    // Getters
    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getCompany() {
        return company;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public boolean isFeatured() {
        return featured;
    }

    public String getCover() {
        return cover;
    }
}
