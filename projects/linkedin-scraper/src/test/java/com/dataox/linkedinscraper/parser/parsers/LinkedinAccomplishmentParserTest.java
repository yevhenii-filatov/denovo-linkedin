package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinAccomplishment;
import com.dataox.linkedinscraper.parser.dto.types.LinkedinAccomplishmentType;
import com.dataox.linkedinscraper.parser.service.mappers.LinkedinAccomplishmentTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinAccomplishmentParserTest {
    private static final String ACCOMPLISHMENT_LANGUAGE_SOURCE = "/sources/linkedin-accomplishment-parser/language-accomplishment-section.html";
    private static final String ACCOMPLISHMENT_PUBLICATIONS_SOURCE = "/sources/linkedin-accomplishment-parser/publications-accomplishment-section.html";
    private static final String ACCOMPLISHMENT_HONORS_SOURCE = "/sources/linkedin-accomplishment-parser/honors-and-awards-accomplishment-section.html";
    private static final String ACCOMPLISHMENT_ORGANIZATIONS_SOURCE = "/sources/linkedin-accomplishment-parser/organizations-accomplishment-section.html";
    private LinkedinParser<List<LinkedinAccomplishment>, List<String>> parser;

    @BeforeEach
    void setUp() {
        parser = new LinkedinAccomplishmentParser(new LinkedinAccomplishmentTypeMapper());
    }

    @Test
    void shouldParseLanguages() throws IOException {
        String source = loadResource(ACCOMPLISHMENT_LANGUAGE_SOURCE);
        List<LinkedinAccomplishment> accomplishments = parser.parse(List.of(source));

        assertThat(accomplishments.size()).isEqualTo(3);

        LinkedinAccomplishment actual = accomplishments.get(0);
        LinkedinAccomplishment expected = new LinkedinAccomplishment();
        expected.setTitle("English");
        expected.setDescription("Professional working proficiency");
        expected.setLinkedinAccomplishmentType(LinkedinAccomplishmentType.LANGUAGES);
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setItemSource("md5:039df0e010fce96f95eb084e5e534e48");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual languages section source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }

    @Test
    void shouldPublications() throws IOException {
        String source = loadResource(ACCOMPLISHMENT_PUBLICATIONS_SOURCE);
        List<LinkedinAccomplishment> accomplishments = parser.parse(List.of(source));

        assertThat(accomplishments.size()).isEqualTo(3);

        LinkedinAccomplishment actual = accomplishments.get(0);
        LinkedinAccomplishment expected = new LinkedinAccomplishment();
        expected.setTitle("Blitzscaling");
        expected.setDescription(
                "publication description Currency publication description What entrepreneur or " +
                        "founder doesn’t aspire to build the next Amazon, Facebook, or Airbnb? Yet those who actually manage " +
                        "to do so are exceedingly rare. So what separates the startups that get disrupted and disappear from " +
                        "the ones who grow to become global giants? The secret is blitzscaling: a set of techniques for scaling " +
                        "up at a dizzying pace that blows competitors out of the water. The objective of blitzscaling is not to go " +
                        "from zero to one, but from one to one billion — as quickly as possible. When growing at a breakneck pace, " +
                        "getting to the next level requires very different strategies from those that got you to where you are today." +
                        " Whether your business has ten employees or ten thousand, Blitzscaling is the essential playbook for winning " +
                        "in a world where speed is the only competitive advantage that matters. publication date Oct 9, 2018"
        );
        expected.setLinkedinAccomplishmentType(LinkedinAccomplishmentType.PUBLICATIONS);
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setItemSource("md5:f8b6b2dc45e15fbc063e37c4c39d9b0b");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual publications section source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }

    @Test
    void shouldHonorsAndAwards() throws IOException {
        String source = loadResource(ACCOMPLISHMENT_HONORS_SOURCE);
        List<LinkedinAccomplishment> accomplishments = parser.parse(List.of(source));

        assertThat(accomplishments.size()).isEqualTo(8);

        LinkedinAccomplishment actual = accomplishments.get(0);
        LinkedinAccomplishment expected = new LinkedinAccomplishment();
        expected.setTitle("Member, Academy of Arts and Sciences");
        expected.setDescription(
                "honor description Founded during the American Revolution by John Adams, James Bowdoin, John Hancock, " +
                        "and other prominent contributors to the establishment of the new nation, the Academy has a " +
                        "rich intellectual history spanning more than 230 years. Since its founding in 1780, " +
                        "the Academy has provided a forum for scholars, members of the learned professions, " +
                        "and government and business leaders to work together on behalf of the democratic " +
                        "interests of the republic. As stated in the Academy’s Charter, the “end and design " +
                        "of the institution is...to cultivate every art and science which may tend to advance " +
                        "the interest, honor, dignity, and happiness of a free, independent, and virtuous people.” " +
                        "honor date Apr 2018 honor issuer Academy of Arts and Sciences"
        );
        expected.setLinkedinAccomplishmentType(LinkedinAccomplishmentType.HONORS_AND_AWARDS);
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setItemSource("md5:499be0652fd45e41113b5c4b69d1727b");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual honor and awards section source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }

    @Test
    void shouldParseOrganizations() throws IOException {
        String source = loadResource(ACCOMPLISHMENT_ORGANIZATIONS_SOURCE);
        List<LinkedinAccomplishment> accomplishments = parser.parse(List.of(source));

        assertThat(accomplishments.size()).isEqualTo(8);

        LinkedinAccomplishment actual = accomplishments.get(0);
        LinkedinAccomplishment expected = new LinkedinAccomplishment();
        expected.setTitle("GM’s Inclusion Advisory Board");
        expected.setDescription("organization date Jun 2020 – Present");
        expected.setLinkedinAccomplishmentType(LinkedinAccomplishmentType.ORGANIZATIONS);
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setItemSource("md5:3a3a6441d00727db906f0761bd73abf6");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual organizations section source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }

}
