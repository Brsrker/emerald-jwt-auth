package com.brsrker.emerald.jwt.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String BASIC_PREFIX = "Basic ";
    private static final String AUTHORITIES_KEY = "auth";
    private static final long JWT_TOKEN_VALIDITY = 60000 * 60;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();

        if(method.equals("GET") && path.equals("/authenticate")){
            if (headerHasAuthorizationBasicToken(request)) {
                try {
                    String authorization = getHeaderAuthToken(request);
                    byte[] credDecoded = Base64.getDecoder().decode(authorization);
                    String credentials = new String(credDecoded, StandardCharsets.UTF_8);

                    final String[] values = credentials.split(":", 2);
                    Authentication authentication = customAuthenticationProvider
                            .authenticate(new UsernamePasswordAuthenticationToken(values[0], values[1]));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwtToken = generateJWTToken(authentication);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + jwtToken);

                } catch (BadCredentialsException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean headerHasAuthorizationBasicToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(AUTHORIZATION_HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(BASIC_PREFIX);
    }

    private String getHeaderAuthToken(HttpServletRequest request) {
        return  request.getHeader(AUTHORIZATION_HEADER).replace(BASIC_PREFIX, "");
    }

    public String generateJWTToken(Authentication authentication) {

        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        User user = (User) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authorities);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

}