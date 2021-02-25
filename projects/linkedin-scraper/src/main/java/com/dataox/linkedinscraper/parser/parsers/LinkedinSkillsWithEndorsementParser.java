package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.dto.sources.SkillsSource;
import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinEndorsement;
import com.dataox.linkedinscraper.parser.dto.LinkedinSkill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static com.dataox.linkedinscraper.parser.utils.sources.SkillsSource.SkillEndorsementsSource;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.substringBetween;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkedinSkillsWithEndorsementParser implements LinkedinParser<List<LinkedinSkill>, List<SkillsSource>> {

    private final LinkedinEndorsementParser endorsementParser;

    @Override
    public List<LinkedinSkill> parse(List<SkillsSource> source) {
        if (source.isEmpty()) {
            log.info("received empty source");
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.stream()
                .flatMap(skillsSource -> getCategorySkills(time, skillsSource))
                .collect(Collectors.toList());
    }

    private Stream<LinkedinSkill> getCategorySkills(Instant time, SkillsSource skillsSource) {
        String category = skillsSource.getCategory();
        return skillsSource.getSkillsWithEndorsementsSources().stream()
                .map(skillEndorsement -> getLinkedinSkill(skillEndorsement, time, category));
    }

    private LinkedinSkill getLinkedinSkill(SkillEndorsementsSource skillEndorsementSource, Instant time, String category) {
        Element skillElement = toElement(skillEndorsementSource.getSkillSource());

        LinkedinSkill skill = new LinkedinSkill();
        skill.setUpdatedAt(time);
        skill.setItemSource(skillElement.html());
        skill.setCategory(category);
        skill.setName(parseName(skillElement));
        skill.setNumberOfEndorsements(getNumberOfEndorsements(skillElement));
        skill.setUrl(skillEndorsementSource.getSkillUrl());
        setEndorsements(skillElement, skill);

        return skill;
    }

    private void setEndorsements(Element skillEndorsementSource, LinkedinSkill linkedinSkill) {
        String endorsementsSource =
                skillEndorsementSource.selectFirst(".pv-profile-detail__content").html();
        List<LinkedinEndorsement> endorsements = endorsementParser.parse(endorsementsSource);
        linkedinSkill.setLinkedinEndorsements(endorsements);
    }

    private String parseNameAndEndorsementsNum(Element skillEndorsementElement) {
        return text(skillEndorsementElement.selectFirst("h2.pv-profile-detail__title"));
    }

    private String parseName(Element skillEndorsementElement) {
        return substringBefore(parseNameAndEndorsementsNum(skillEndorsementElement), " (");
    }

    private int getNumberOfEndorsements(Element skillEndorsementElement) {
        return Integer.parseInt(
                substringBetween(parseNameAndEndorsementsNum(skillEndorsementElement), "(", ")")
        );
    }
}
