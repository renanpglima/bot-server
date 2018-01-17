package controllers;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import model.Store;
import play.mvc.Controller;
import play.mvc.Result;

public class StoresController extends Controller {

	private Store[] stores;
	
	public StoresController() {
		System.out.println("StoresController");
		
		/*try {
			ObjectMapper mapper = new ObjectMapper();
			stores = mapper.readValue(new URL("http://sonaesodetapi.herokuapp.com/api/v3/store?shoppingId=1"), 
					Store[].class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}
	
	public Result initStores() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			stores = mapper.readValue(new URL("http://sonaesodetapi.herokuapp.com/api/v3/store?shoppingId=1"), 
					Store[].class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok("Stores Loaded");
	}

	public Result getStores() {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.valueToTree(this.stores);
		
		String json = "[]";
		try {
			json = mapper.writeValueAsString(array);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok(json);
	}
	
	public Result getStore(String name) {	
		String json = "{}";
		Store s = null;
		name = name.toUpperCase().trim();
		
		for (int i = 0; i < this.stores.length; i++) {
			if (this.stores[i].title.toUpperCase().equals(name)){
				s = this.stores[i];
				break;
			}
		}
		
		if (s != null) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.valueToTree(s);
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
