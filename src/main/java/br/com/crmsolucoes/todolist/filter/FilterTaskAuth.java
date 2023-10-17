package br.com.crmsolucoes.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.crmsolucoes.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component //Classe gerenciada pelo SpringBoot
public class FilterTaskAuth extends OncePerRequestFilter{
    //Antes de ir para cada rota passa pelo filtro

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            var servelethPath = request.getServletPath();

            if (servelethPath.startsWith("/tasks")) {
                //Pegar Credenciais
                var authorization = request.getHeader("Authorization");

                if (authorization == null) {
                    response.sendError(400);
                    return;
                }

                var authEncoded = authorization.substring("Basic".length()).trim();
            
                byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

                var authString  = new String(authDecoded);
            
                String[] credentials = authString.split(":");
                
                if (credentials.length < 2) {
                    response.sendError(400);
                    return;
                }
                
                var username = credentials[0];            
                var password = credentials[1];


                //Validar Usuário
                var user = this.userRepository.findByUsername(username);

                if (user == null) {
                    response.sendError(401);
                    return;
                } 

                //Validar Senha
                var password_verify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (!password_verify.verified) {
                    response.sendError(401);
                    return;
                }

                //Segue a Aplicação
                request.setAttribute("idUser", user.getId());
            } 

            filterChain.doFilter(request, response);
    }
    
}
