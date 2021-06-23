package com.dataox.imagedownloader.security.models.payload.requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    String userName;

    Set<String> roles;

    @NotBlank
    @Size(min = 6, max = 40)
    String password;
}
