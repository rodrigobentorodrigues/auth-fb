package br.com.auth.infra;

import org.json.JSONObject;

/**
 *
 * @author rodrigobento
 */
public interface ClienteServiceFB {
    
    String getAcessToken(String code);
    JSONObject getData(String token);
    String getURL();
    
}
