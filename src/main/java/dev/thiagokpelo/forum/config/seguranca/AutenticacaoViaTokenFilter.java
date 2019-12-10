package dev.thiagokpelo.forum.config.seguranca;

import dev.thiagokpelo.forum.model.Usuario;
import dev.thiagokpelo.forum.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private static final String TIPO_DA_AUTORIZACAO = "Bearer ";
    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;

    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperarToken(request);
        boolean valido = tokenService.isTokenValido(token);

        if (valido) {
            autenticarCliente(token);
        }
        filterChain.doFilter(request, response);
    }

    private void autenticarCliente(String token) {
        Long idUsuario = tokenService.getIdUsuario(token);
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        UsernamePasswordAuthenticationToken autenticado = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(autenticado);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith(TIPO_DA_AUTORIZACAO)) {
            return null;
        }

        return token.replace(TIPO_DA_AUTORIZACAO, "");
    }
}
