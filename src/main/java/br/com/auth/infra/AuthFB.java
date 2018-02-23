package br.com.auth.infra;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author rodrigobento
 */
@WebServlet(name = "AuthFB", urlPatterns = {"/auth"})
public class AuthFB extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ClienteServiceFB serviceFB = new ClienteServiceFBImpl();

    /**
     * Servlet responsavel pelo retorno obtido após o usuário ter se autenticado pelo FB;
     * Retorna os dados especificados e imprime como resposta o nome e a idade do usuario;
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        // Realizada a primeira requisição, redirecionar para a autenticação do FB
        if (code == null || code.isEmpty()) {
            response.sendRedirect(serviceFB.getURL());
        } else {
            // Autenticação concluida, tokens obtidos
            String acessToken = serviceFB.getAcessToken(code);
            // Para obter os valores do usuario é necessario o token obtido atraves do codigo da requisicao
            JSONObject valores = serviceFB.getData(acessToken);
            JSONObject jsonIdade = valores.getJSONObject("age_range");
            String nome = valores.getString("name");
            int idade = jsonIdade.getInt("max");
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Dados FB</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Nome: " + nome + "</h1>");
                out.println("<h1>Idade: " + idade + "</h1>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

}
