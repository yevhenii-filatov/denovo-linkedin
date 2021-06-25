package com.dataox.imagedownloader.security.models.payload.requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank
    String userName;
    @NotBlank
    String password;
}
