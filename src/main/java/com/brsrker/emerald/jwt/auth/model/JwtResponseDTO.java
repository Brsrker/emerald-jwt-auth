package com.brsrker.emerald.jwt.auth.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class JwtResponseDTO implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String token;

    public JwtResponseDTO(String token) {
        this.token = token;
    }
}