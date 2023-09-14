package app;

import org.json.*;
import business.Cartas;
import java.io.IOException;
import java.util.List;


public class Aplicacao {

	public static void main(String[] args) {
        try {
            String deckId = Cartas.criarNovoBaralho();
            List<String> cartasAlan = Cartas.distribuirCartas(deckId, "Alan", 11);
            List<String> cartasBruno = Cartas.distribuirCartas(deckId, "Bruno", 11);

            Cartas.organizarMao(cartasAlan);
            Cartas.organizarMao(cartasBruno);

            boolean sequenciaAlan = Cartas.temSequencia(cartasAlan);
            boolean sequenciaBruno = Cartas.temSequencia(cartasBruno);

            JSONObject resultado = Cartas.criarJSONMao(cartasAlan, cartasBruno);
            resultado.put("tem_sequencia_alan", sequenciaAlan);
            resultado.put("tem_sequencia_bruno", sequenciaBruno);

            String vencedor = "empate";
            if (sequenciaAlan && !sequenciaBruno) {
                vencedor = "alan";
            } else if (!sequenciaAlan && sequenciaBruno) {
                vencedor = "bruno";
            }

            resultado.put("vencedor", vencedor);

            System.out.println(resultado.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 

