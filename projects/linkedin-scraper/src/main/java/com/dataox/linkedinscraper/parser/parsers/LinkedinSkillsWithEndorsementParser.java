package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinEndorsement;
import com.dataox.linkedinscraper.parser.dto.LinkedinSkill;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.substringBetween;

@Service
@RequiredArgsConstructor
public class LinkedinSkillsWithEndorsementParser implements LinkedinParser<List<LinkedinSkill>, Map<String, List<String>>> {

    private final LinkedinEndorsementParser endorsementParser;

    @Override
    public List<LinkedinSkill> parse(Map<String, List<String>> source) {
        if (source.isEmpty()) {
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.entrySet().stream()
                .flatMap(entry -> {
                    String category = entry.getKey();
                    return entry.getValue().stream()
                            .map(skillEndorsement -> {
                                Element skillEndorsementSource = toElement(skillEndorsement);
                                LinkedinSkill linkedinSkill = getLinkedinSkill(skillEndorsementSource, time, category);

                                setEndorsements(skillEndorsementSource, linkedinSkill);

                                return linkedinSkill;
                            });
                })
                .collect(Collectors.toList());
    }

    private LinkedinSkill getLinkedinSkill(Element skillElement, Instant time, String category) {
        LinkedinSkill skill = new LinkedinSkill();

        skill.setUpdatedAt(time);
        skill.setItemSource(skillElement.html());
        skill.setCategory(category);
        skill.setName(parseName(skillElement));
        skill.setNumberOfEndorsements(getNumberOfEndorsements(skillElement));

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
