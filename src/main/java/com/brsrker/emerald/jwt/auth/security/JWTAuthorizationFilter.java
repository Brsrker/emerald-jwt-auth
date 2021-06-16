package com.brsrker.emerald.jwt.auth.security;

import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException {

        if (headerHasAuthorizationBearerToken(request)) {
            try {
                final Claims claims = validateToken(request);
                if (claims.get(AUTHORITIES_KEY) != null) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } catch (AccessDeniedException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        chain.doFilter(request, response);
    }

    private boolean headerHasAuthorizationBearerToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(AUTHORIZATION_HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(BEARER_PREFIX);
    }

    private void setUpSpringAuthentication(Claims claims) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null, getAuthorities(claims));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(AUTHORIZATION_HEADER).replace(BEARER_PREFIX, "");
        JwtParser jwtParser = tokenParser();
        return jwtParser.parseClaimsJws(jwtToken).getBody();
    }

    private JwtParser tokenParser() {
        return Jwts.parser().setSigningKey(secret);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Claims claims){
        String auth = (String) claims.get(AUTHORITIES_KEY);
        List<String> auths = Arrays.asList(auth.split(","));
        return auths.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

}