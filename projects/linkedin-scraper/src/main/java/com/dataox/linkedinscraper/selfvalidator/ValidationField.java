package com.dataox.linkedinscraper.selfvalidator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationField {

    private SelfValidationType validationType;
    private String value;
}
