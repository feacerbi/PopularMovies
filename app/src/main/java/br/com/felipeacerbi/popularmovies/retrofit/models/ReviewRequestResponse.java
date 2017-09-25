package br.com.felipeacerbi.popularmovies.retrofit.models;

import java.util.List;

@SuppressWarnings({"UnusedDeclaration"})
public class ReviewRequestResponse{

	private int id;
	private int page;
	private int totalPages;
	private List<ReviewResult> results;
	private int totalResults;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setResults(List<ReviewResult> results){
		this.results = results;
	}

	public List<ReviewResult> getResults(){
		return results;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}
}