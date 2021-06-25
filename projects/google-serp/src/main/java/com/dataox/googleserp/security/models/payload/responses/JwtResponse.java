package com.dataox.googleserp.security.models.payload.responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
public class JwtResponse {
    String token;
    String type = "Bearer";
    Long id;
    String userName;
    List<String> roles;

}
