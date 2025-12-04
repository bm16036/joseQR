package com.menudigital.menuapi.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.io.Decoders; import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value; import org.springframework.security.core.userdetails.UserDetails; import org.springframework.stereotype.Service;
import javax.crypto.SecretKey; import java.util.*; import java.util.function.Function;
@Service
public class JwtService {
  @Value("${app.jwt.secret}") private String secret;
  @Value("${app.jwt.expiration-ms}") private long expirationMs;
  private SecretKey key(){ byte[] bytes=Decoders.BASE64.decode(Base64.getEncoder().encodeToString(secret.getBytes())); return Keys.hmacShaKeyFor(bytes); }
  public String extractUsername(String token){ return extractClaim(token, Claims::getSubject); }
  public <T> T extractClaim(String token, Function<Claims,T> r){ var c=Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload(); return r.apply(c); }
  public String generateToken(UserDetails u){
    var now=new Date(); var exp=new Date(System.currentTimeMillis()+expirationMs);
    return Jwts.builder().claims(Map.of("role",u.getAuthorities().iterator().next().getAuthority())).subject(u.getUsername()).issuedAt(now).expiration(exp).signWith(key()).compact();
  }
  public boolean isValid(String token, UserDetails u){ String un=extractUsername(token); var exp=extractClaim(token, Claims::getExpiration); return un.equals(u.getUsername()) && exp.after(new Date()); }
}
