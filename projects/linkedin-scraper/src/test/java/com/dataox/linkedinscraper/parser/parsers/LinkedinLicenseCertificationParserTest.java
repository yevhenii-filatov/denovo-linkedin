package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinLicenseCertification;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinLicenseCertificationParserTest {

    private static final String EXPERIENCE_SOURCE = "/sources/linkedin-license-certification-parser/certifications-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(EXPERIENCE_SOURCE);
        LinkedinParser<List<LinkedinLicenseCertification>, String> parser = new LinkedinLicenseCertificationParser();

        List<LinkedinLicenseCertification> experiences = parser.parse(source);

        assertThat(experiences.size()).isEqualTo(3);

        LinkedinLicenseCertification actual = experiences.get(0);

        LinkedinLicenseCertification expected = new LinkedinLicenseCertification();
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setName("Financial accounting");
        expected.setIssuer("Coursera");
        expected.setIssuedDate("Dec 2018");
        expected.setIssuerProfileUrl("https://www.linkedin.com/company/coursera/");
        expected.setExpirationDate("No Expiration Date");
        expected.setCredentialId("D43VVL7HRKML");
        expected.setItemSource("md5:d4f3f5434c0fe68e3a3c19cbb34e293a");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual certification source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }
}
