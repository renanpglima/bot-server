package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
    	return ok("Hello!");
    }
    
    /*public Result messageInterpreter(String message) {
    	
    	Result res;
    	
    	JsonNode json = Json.newObject();
    	
    	message = message.toLowerCase();
    	
    	MessageType type = MessageType.getTypeByText(message);
    	
    	if (type == MessageType.STORE) {
    		
    		JsonNode stores = this.stores.getStores();
    		res = ok(stores);
    	}
    	else if (type == MessageType.MOVIE) {
    		json = Json.parse("{\"message\":\"Você quer saber os filmes que estão em cartaz?\"}");
    		res = ok(json);
    	}else {
    		json = Json.parse("{\"message\":\"Não entendi sua pergunta, você quer saber dos filmes ou das lojas?\"}");
    		res = ok(json);
    	}
    	
    	return res;
    }
    
    public Result getDetails(int type) {
    	Result res;
    	
    	if (type == 0) {
    		res = ok("detalhes dos filmes...");
    	}else {
    		res = ok("detalhes das lojas...");
    	}
    	
    	return res;
    }*/

}
