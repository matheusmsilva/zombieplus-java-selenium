package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Serie {
    private String title;
    private String overview;
    private String company;
    @JsonProperty("release_year")
    private int releaseYear;
    private boolean featured;
    private String cover;
    @JsonProperty("company_id")
    private String companyId;
    private int seasons;
    private int season;

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

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public int getSeasons() {
        return seasons;
    }

    public int getSeason() {
        return season;
    }
}
