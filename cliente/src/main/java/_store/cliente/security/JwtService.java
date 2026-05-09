package _store.cliente.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // Esta clave se lee desde application.properties.
    @Value("${app.jwt.secret}")
    private String secret;

    // Tiempo de vida del token en milisegundos.
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    // Genera un token nuevo para el usuario autenticado.
    public String generarToken(String username) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationMs);

        return Jwts.builder()
                // subject = nombre del usuario dentro del token.
                .setSubject(username)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrae el username guardado dentro del token.
    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    // Valida que el token sea correcto y que no haya expirado.
    public boolean tokenValido(String token) {
        try {
            Claims claims = extraerClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Lee el contenido interno del token.
    private Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Convierte la clave de texto en una clave que JJWT pueda usar.
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}