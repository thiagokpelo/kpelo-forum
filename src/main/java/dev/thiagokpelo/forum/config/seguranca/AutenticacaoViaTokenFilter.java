package dev.thiagokpelo.forum.config.seguranca;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    public static final String TIPO_DA_AUTORIZACAO = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperarToken(request);
        System.out.println(token);
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith(TIPO_DA_AUTORIZACAO)) {
            return null;
        }

        return token.replace(TIPO_DA_AUTORIZACAO, "");
    }
}
