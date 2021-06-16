package com.brsrker.emerald.jwt.auth.model;

import com.brsrker.emerald.jwt.auth.util.Constant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserRegisterDTO {
    @NotNull
    @Pattern(regexp = Constant.EMAIL_VAL_REGEX, message = "Invalid email")
    private String email;
    @NotNull
    @Pattern(regexp = Constant.PASSWORD_VAL_REGEX, message = "Invalid password constrain")
    private String password;
    private Boolean enabled;
    private Boolean expire;
    private Boolean locked;
    private LocalDateTime expireAt;
}
