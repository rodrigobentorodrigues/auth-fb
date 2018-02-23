package br.com.auth.infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

/**
 *
 * @author rodrigobento
 */
public class ClienteServiceFBImpl implements ClienteServiceFB {

    private final String CLIENT_ID;
    private final String REDIRECT_URI;
    private final String APP_SECRET;
    private final String URL;

    {
        // Informe aqui os dados especificados
        this.APP_SECRET = "";
        this.CLIENT_ID = "";
        this.REDIRECT_URI = "";
        // Faz a requisicao para a URL contendo o scopo para os dados do perfil do usuario
        this.URL = "http://www.facebook.com/dialog/oauth?client_id="
                + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&scope=public_profile";
    }

    @Override
    public String getURL() {
        return URL;
    }

    private String getAuthFB(String codigo) {
        return "https://graph.facebook.com/oauth/access_token?client_id="
                + CLIENT_ID + "&redirect_uri="
                + REDIRECT_URI + "&client_secret=" + APP_SECRET + "&code=" + codigo;
    }

    /**
     *  
     * @author rodrigobento
     * @param codigo referente a requisicão
     * @return o token de acesso apartir do codigo da requisicao e dos dados do facebook developers
     */
    @Override
    public String getAcessToken(String codigo) {
        String valor = "";
        try {
            URL url = new URL(getAuthFB(codigo));
            URLConnection conexao = url.openConnection();
            StringBuilder palavra = new StringBuilder();
            try (BufferedReader bf = new BufferedReader(
                    new InputStreamReader(conexao.getInputStream()))) {
                String s;
                while ((s = bf.readLine()) != null) {
                    palavra.append(s);
                }
            }
            JSONObject objeto = new JSONObject(palavra.toString());
            valor = objeto.getString("access_token");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return valor;
    }

    @Override
    public JSONObject getData(String acessToken) {
        JSONObject retorno = null;
        try {
            // String contendo o caminho de requisição contendo os campos desejados, como (nome, foto e idade)
            String path = "https://graph.facebook.com/me?fields=name,picture,age_range&access_token=" + acessToken;
            URL url = new URL(path);
            URLConnection conexao = url.openConnection();
            StringBuilder palavra = new StringBuilder();
            try (BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(conexao.getInputStream()))) {
                String s;
                while ((s = buffer.readLine()) != null) {
                    palavra.append(s);
                }
            }
            retorno = new JSONObject(palavra.toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

}
