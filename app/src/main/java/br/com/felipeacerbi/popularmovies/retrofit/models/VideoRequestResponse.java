package br.com.felipeacerbi.popularmovies.retrofit.models;

import java.util.List;

@SuppressWarnings({"UnusedDeclaration"})
public class VideoRequestResponse{

	private int id;
	private List<VideoResult> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<VideoResult> results){
		this.results = results;
	}

	public List<VideoResult> getResults(){
		return results;
	}
}