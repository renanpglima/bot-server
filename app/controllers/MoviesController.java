package controllers;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import model.Movie;
import model.Store;
import play.mvc.Controller;
import play.mvc.Result;

public class MoviesController extends Controller {

	private Movie[] movies;
	
	public MoviesController() {
		
		System.out.println("MoviesController");
		
		/*try {
			ObjectMapper mapper = new ObjectMapper();
			movies = mapper.readValue(new URL("http://sonaesodetapi.herokuapp.com/api/v3/movie?shoppingId=1"), 
					Movie[].class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	public Result initMovies() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			movies = mapper.readValue(new URL("http://sonaesodetapi.herokuapp.com/api/v3/movie?shoppingId=1"), 
					Movie[].class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok("Movies Loaded");
	}
	
	
	public Result getMovies() {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.valueToTree(this.movies);
		
		String json = "[]";
		try {
			json = mapper.writeValueAsString(array);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok(json);
	}
	
	public Result getMovie(String name) {	
		String json = "{}";
		Movie m = null;
		name = name.toUpperCase().trim();
		
		for (int i = 0; i < this.movies.length; i++) {
			if (this.movies[i].title.toUpperCase().equals(name)){
				m = this.movies[i];
				break;
			}
		}
		
		if (m != null) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.valueToTree(m);
			try {
				json = mapper.writeValueAsString(node);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ok(json);
		
	}

}
