package business;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class Cartas {
	
	 private static String criarNovoBaralho() throws IOException {
	        URL url = new URL("https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1");
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuilder response = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            response.append(line);
	        }
	        reader.close();

	        JSONObject jsonObject = new JSONObject(response.toString());
	        return jsonObject.getString("deck_id");
	    }
	 
	 

}
