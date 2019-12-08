package dev.thiagokpelo.forum.config.seguranca;

import dev.thiagokpelo.forum.model.Usuario;
import dev.thiagokpelo.forum.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {
        Usuario logado = (Usuario) authentication.getPrincipal();

        return TokenUtils.createJWT(secret, "security_issuer", logado.getId().toString(), Long.parseLong(expiration));
    }
}
