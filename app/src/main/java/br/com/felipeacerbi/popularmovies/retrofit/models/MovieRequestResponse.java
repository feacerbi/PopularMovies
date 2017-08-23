package br.com.felipeacerbi.popularmovies.retrofit.models;

@SuppressWarnings({"UnusedDeclaration"})
public class MovieRequestResponse {

    private int page;
    private MovieResult[] results;
    private int total_results;
    private int total_pages;

    public MovieRequestResponse() {
    }

    public MovieRequestResponse(int page, MovieResult[] results, int total_results, int total_pages) {
        this.page = page;
        this.results = results;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public MovieResult[] getResults() {
        return results;
    }

    public void setResults(MovieResult[] results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
