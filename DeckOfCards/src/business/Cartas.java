package business;

import org.json.*;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;




public class Cartas {
	
	 public static String criarNovoBaralho() throws IOException {
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
	 
	 public static List<String> distribuirCartas(String deckId, String jogador, int numCartas) throws IOException {
	        URL url = new URL("https://deckofcardsapi.com/api/deck/" + deckId + "/draw/?count=" + numCartas);
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
	        JSONArray cardsArray = jsonObject.getJSONArray("cards");

	        List<String> cartas = new ArrayList<>();
	        for (int i = 0; i < cardsArray.length(); i++) {
	            JSONObject cardObject = cardsArray.getJSONObject(i);
	            cartas.add(cardObject.getString("code"));
	        }

	        return cartas;
	    }
	 
	 
	 public static int obterValorCarta(String carta) {
	        String valor = carta.substring(0, carta.length() - 1);
	        switch (valor) {
	            case "A":
	                return 1;
	            case "K":
	                return 13;
	            case "Q":
	                return 12;
	            case "J":
	                return 11;
	            case "0":
	            	return 10;
	            default:
	                return Integer.parseInt(valor);
	        }
	    }
	 
	 public static void organizarMao(List<String> cartas) {
		    for (int i = 0; i < cartas.size() - 1; i++) {
		        for (int j = 0; j < cartas.size() - i - 1; j++) {
		            String carta1 = cartas.get(j);
		            String carta2 = cartas.get(j + 1);

		            int valor1 = obterValorCarta(carta1);
		            int valor2 = obterValorCarta(carta2);

		            if (valor1 > valor2) {
		                Collections.swap(cartas, j, j + 1);
		                
		            } else if (valor1 == valor2) {
		                String naipe1 = carta1.substring(carta1.length() - 1);
		                String naipe2 = carta2.substring(carta2.length() - 1);
		                if (naipe1.compareTo(naipe2) > 0) {
		                    Collections.swap(cartas, j, j + 1);
		                }
		            }
		        }
		    }
		}	 

	    
	 public static boolean temSequencia(List<String> cartas) {
		    organizarMao(cartas);

		    Map<String, Integer> contagemNaipe = new HashMap<>();
		    int sequenciaAtual = 1;

		    for (int i = 0; i < cartas.size() - 1; i++) {
		        String cartaAtual = cartas.get(i);
		        String proximaCarta = cartas.get(i + 1);

		        int valorAtual = obterValorCarta(cartaAtual);
		        int valorProximaCarta = obterValorCarta(proximaCarta);
		        String naipeAtual = cartaAtual.substring(cartaAtual.lastIndexOf("de ") + 3);
		        String naipeProximaCarta = proximaCarta.substring(proximaCarta.lastIndexOf("de ") + 3);

		        if (naipeAtual.equals(naipeProximaCarta) && valorProximaCarta == valorAtual + 1) {
		            sequenciaAtual++;
		        } else {
		            sequenciaAtual = 1;
		        }

		        if (sequenciaAtual >= 3) {
		            return true;
		        }
		    }

		    return false;
		}
	 
	 
	 public static JSONObject criarJSONMao(List<String> cartasAlan, List<String> cartasBruno) {
		    JSONArray maoAlan = new JSONArray();
		    for (String carta : cartasAlan) {
		        maoAlan.put(obterNomeCarta(carta));
		    }

		    JSONArray maoBruno = new JSONArray();
		    for (String carta : cartasBruno) {
		        maoBruno.put(obterNomeCarta(carta));
		    }

		    JSONObject cartas = new JSONObject();
		    cartas.put("cartas", new JSONObject().put("Alan", maoAlan).put("Bruno", maoBruno));
		    return cartas;
		}
	 
	 
	 public static String obterNomeCarta(String carta) {
		    String valor = "";
		    String naipe = "";

		    String valorCarta = carta.substring(0, carta.length() - 1);
		    switch (valorCarta) {
		        case "J": valor = "Valete"; break;
		        case "Q": valor = "Rainha"; break;
		        case "K": valor = "Rei"; break;
		        case "A": valor = "Ás"; break;
		        case "0": valor = "10"; break;
		        default:
		            valor = valorCarta;
		            break;
		    }

		    switch (carta.substring(carta.length() - 1)) {
		        case "H": naipe = "de Copas"; break;
		        case "D": naipe = "de Ouros"; break;
		        case "S": naipe = "de Espadas"; break;
		        case "C": naipe = "de Paus"; break;
		    }

		    return valor + " " + naipe;
		}
	
}
