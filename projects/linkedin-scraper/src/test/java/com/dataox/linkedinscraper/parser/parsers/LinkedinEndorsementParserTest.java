package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinEndorsement;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinEndorsementParserTest {

    private static final String ENDORSEMENT_SOURCE = "/sources/linkedin-endorsement-parser/endorsement-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(ENDORSEMENT_SOURCE);
        LinkedinParser<List<LinkedinEndorsement>, String> parser = new LinkedinEndorsementParser();

        List<LinkedinEndorsement> endorsements = parser.parse(source);

        assertThat(endorsements.size()).isEqualTo(9);

        LinkedinEndorsement actual = endorsements.get(0);

        LinkedinEndorsement expected = new LinkedinEndorsement();
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setEndorserFullName("Victor Gozhyi");
        expected.setEndorserHeadline("COO - TemplateMonster.com");
        expected.setEndorserConnectionDegree("3rd");
        expected.setEndorserProfileUrl("https://www.linkedin.com/in/victor-gozhyi-75b5295a/");
        expected.setItemSource("md5:6c8899bd97b410300bbb0973bad3ac0a");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual endorsements source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");

    }
}
