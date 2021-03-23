package com.dataox.linkedinscraper.parser.dto.types;

import lombok.Getter;

@Getter
public enum LinkedinInterestType {
    INFLUENCER("Influencers"),
    COMPANY("Companies"),
    GROUP("Groups"),
    SCHOOL("Schools");

    private final String type;

    LinkedinInterestType(String type) {
        this.type = type;
    }
}
