package com.dataox.linkedinscraper.validator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationField {

    private ValidationType validationType;
    private String value;
}
