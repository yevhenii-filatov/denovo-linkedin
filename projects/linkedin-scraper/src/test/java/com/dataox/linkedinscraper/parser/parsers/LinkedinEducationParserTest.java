package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinEducation;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinEducationParserTest {

    private static final String EDUCATION_SOURCE = "/sources/linkedin-educatin-parser/education-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(EDUCATION_SOURCE);
        LinkedinParser<List<LinkedinEducation>, String> parser = new LinkedinEducationParser();

        List<LinkedinEducation> linkedinEducations = parser.parse(source);

        assertThat(linkedinEducations.size()).isEqualTo(2);

        LinkedinEducation actual1 = linkedinEducations.get(0);
        LinkedinEducation actual2 = linkedinEducations.get(1);

        LinkedinEducation expected1 = new LinkedinEducation();
        expected1.setInstitutionName("Loyola Law School, Loyola Marymount University");
        expected1.setInstitutionProfileUrl("https://www.linkedin.com/school/17874/?legacySchoolId=17874");
        expected1.setDescription(null);
        expected1.setActivitiesAndSocieties("Entertainment and Sports Law Society; Dotted Line Reporter Staff Writer; 2014-2015 Secretary, Women's Law Association; OutLaw");
        expected1.setDegree("J.D.");
        expected1.setStartedYear("2012");
        expected1.setFinishedYear("2015");
        expected1.setUpdatedAt(actual1.getUpdatedAt());
        expected1.setFieldOfStudy(null);
        expected1.setItemSource("md5:5fa104398149e4e3dac8240718f616b3");

        LinkedinEducation expected2 = new LinkedinEducation();
        expected2.setInstitutionName("University of Southern California");
        expected2.setInstitutionProfileUrl("https://www.linkedin.com/school/17971/?legacySchoolId=17971");
        expected2.setDescription(null);
        expected2.setActivitiesAndSocieties(null);
        expected2.setDegree("Bachelor of Arts (B.A.)");
        expected2.setStartedYear("2007");
        expected2.setFinishedYear("2012");
        expected2.setUpdatedAt(actual2.getUpdatedAt());
        expected2.setFieldOfStudy("Communication; Bachelor of Arts (B.A.), Social Sciences (Psychology)");
        expected2.setItemSource("md5:398116b18ecaaf444e3d6a9344bdd239");

        assertThat(actual1)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected1, "itemSource");

        assertThat(actual1)
                .as("actual education source1 hash md5:" + DigestUtils.md5DigestAsHex(actual1.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected1, "itemSource");

        assertThat(actual2)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected2, "itemSource");

        assertThat(actual2)
                .as("actual education source2 hash md5:" + DigestUtils.md5DigestAsHex(actual2.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected2, "itemSource");

    }
}
