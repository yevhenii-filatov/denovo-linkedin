package com.dataox.linkedinscraper.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@ToString
public class AccountCredentials {
    String login;
    String password;
}
