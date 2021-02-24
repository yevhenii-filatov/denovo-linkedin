package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinSkill;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class LinkedinSkillsWithoutEndorsementParser implements LinkedinParser<List<LinkedinSkill>, String> {

    private static final String WITHOUT_ENDORSEMENTS_SELECTOR = "li:not(:has(a[data-control-name=skills_endorsement_full_list]))";

    @Override
    public List<LinkedinSkill> parse(String source) {
        if (source.isEmpty()) {
            log.info("{} received empty source", this.getClass().getSimpleName());
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        Element skillsSectionElement = toElement(source);
        List<LinkedinSkill> topThreeSkills = getTopThree(time, skillsSectionElement);

        addOtherSkills(time, skillsSectionElement, topThreeSkills);

        return topThreeSkills;
    }

    private List<LinkedinSkill> getTopThree(Instant time, Element skillsSectionElement) {
        return splitTopThreeSkills(skillsSectionElement).stream()
                .map(element -> getLinkedinSkill(element, time, "Top Three"))
                .collect(Collectors.toList());
    }

    private void addOtherSkills(Instant time, Element skillsSectionElement, List<LinkedinSkill> topThreeSkills) {
        Element categoriesExpandedElement = skillsSectionElement.selectFirst("#skill-categories-expanded");

        if (nonNull(categoriesExpandedElement)) {
            List<LinkedinSkill> otherSkills = getOtherSkills(time, categoriesExpandedElement);
            topThreeSkills.addAll(otherSkills);
        }
    }

    private List<LinkedinSkill> getOtherSkills(Instant time, Element categoriesExpandedElement) {
        return splitOnCategories(categoriesExpandedElement).stream()
                .flatMap(categoryElement -> {
                    String category = parseCategory(categoryElement);
                    return splitOtherSkills(categoryElement).stream()
                            .map(skillElement -> getLinkedinSkill(skillElement, time, category));
                })
                .collect(Collectors.toList());
    }

    private LinkedinSkill getLinkedinSkill(Element skillElement, Instant time, String category) {
        LinkedinSkill skill = new LinkedinSkill();

        skill.setUpdatedAt(time);
        skill.setItemSource(skillElement.html());
        skill.setCategory(category);
        skill.setName(parseName(skillElement));

        return skill;
    }

    private Elements splitTopThreeSkills(Element skillsSectionElement) {
        return skillsSectionElement.select("div:contains(Skills & endorsements) + ol > "
                + WITHOUT_ENDORSEMENTS_SELECTOR);
    }

    private Elements splitOnCategories(Element categoriesExpandedElement) {
        return categoriesExpandedElement.select(".pv-skill-category-list");
    }

    private Elements splitOtherSkills(Element otherSkillsSectionElement) {
        return otherSkillsSectionElement.select("ol > " + WITHOUT_ENDORSEMENTS_SELECTOR);
    }

    private String parseCategory(Element categoryElement) {
        return text(categoryElement.selectFirst("h3"));
    }

    private String parseName(Element skillElement) {
        return text(skillElement.selectFirst("span.pv-skill-category-entity__name-text"));
    }
}
