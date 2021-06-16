package com.brsrker.emerald.jwt.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {

    private String loginId;
    @JsonIgnore
    private String password;
    private Boolean enabled;
    private Boolean locked;
    private Boolean expire;
    private LocalDateTime lastLogin;
    private LocalDateTime expireAt;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

}
