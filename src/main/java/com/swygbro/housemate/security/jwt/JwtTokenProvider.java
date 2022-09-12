package com.swygbro.housemate.security.jwt;

import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.security.authectication.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private final MyUserDetails myUserDetails;

    final static public String ACCESS_TOKEN_NAME = "JWT-ZIPHAP-TOKEN";
    public final static long TOKEN_VALIDATION_SECOND = 1000L * 3600 * 24;  // 하루를 accessToken 만료 기간으로 잡는다

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getMemberRole(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("MemberRole", String.class);
    }

    public String getLoginRole(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("LoginRole", String.class);
    }

    public String getMemberEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("MemberEmail", String.class);
    }

    public String createToken(Member member){
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put("TokenName", ACCESS_TOKEN_NAME);
        claims.put("MemberEmail", member.getEmail());
        claims.put("MemberRole", member.getMemberRole());
        claims.put("LoginRole", member.getLoginRole());

        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDATION_SECOND); //Expire Time

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return  bearerToken.substring(7);
        } else {
            return null;
        }
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
