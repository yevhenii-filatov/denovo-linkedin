package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinActivity;
import com.dataox.linkedinscraper.parser.dto.LinkedinPost;
import com.dataox.linkedinscraper.parser.dto.types.LinkedinActivityType;
import com.dataox.linkedinscraper.parser.service.TimeConverter;
import com.dataox.linkedinscraper.parser.service.mappers.LinkedinActivityTypeMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinActivityParserTest {
    private static final String ACTIVITY_SOURCE = "/sources/linkedin-activity-parser/activity-feed.html";
    private static List<LinkedinActivity> activitiesList;

    @BeforeAll
    static void beforeAll() throws IOException {
        String source = loadResource(ACTIVITY_SOURCE);
        List<String> activitySource = Jsoup.parse(source).select(".occludable-update.ember-view").stream()
                .map(Element::html)
                .collect(Collectors.toList());

        LinkedinPostParser postParser = new LinkedinPostParser(new TimeConverter(), new LinkedinCommentParser(new TimeConverter()));
        LinkedinParser<List<LinkedinActivity>, List<String>> activitiesParser =
                new LinkedinActivityParser(postParser, new LinkedinActivityTypeMapper());

        activitiesList = activitiesParser.parse(activitySource);
    }

    @Test
    void shouldParseSharedThis() {
        LinkedinActivity actual = activitiesList.get(3);
        actual.getLinkedinActivityType();

        LinkedinActivity expected = new LinkedinActivity();
        expected.setLinkedinActivityType(LinkedinActivityType.SHARED_THIS);
        expected.setUpdatedAt(actual.getUpdatedAt());

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "linkedinPost");


        LinkedinPost actualActivityPost = actual.getLinkedinPost();

        LinkedinPost expectedActivityPost = new LinkedinPost();
        expectedActivityPost.setLinkedinComments(actualActivityPost.getLinkedinComments());
        expectedActivityPost.setCollectedDate(actualActivityPost.getCollectedDate());
        expectedActivityPost.setAuthorHeadline("Founder @ Aidos | Data Scientist | Ex-J.P. Morgan");
        expectedActivityPost.setAuthorConnectionDegree("2nd");
        expectedActivityPost.setAuthorProfileName("Andrew Vo, CFA");
        expectedActivityPost.setAuthorProfileUrl("https://www.linkedin.com/in/aidos-andrew?miniProfileUrn=urn%3Ali%3Afs_miniProfile%3AACoAAAbgPvkBAhFlAcbbgmykf0oScmf4rqwDt5Y");
        expectedActivityPost.setNumberOfComments(0);
        expectedActivityPost.setNumberOfReactions(1);
        expectedActivityPost.setUrl("https://www.linkedin.com/feed/update/urn:li:activity:6645013830671835136");
        expectedActivityPost.setRelativePublicationDate("1yr");
        expectedActivityPost.setAbsolutePublicationDate(actualActivityPost.getAbsolutePublicationDate());
        expectedActivityPost.setContent(
                        "In 1963, President John F. Kennedy ordered the U.S. Army 2nd Infantry Division to enforce the racial" +
                        " integration of The University of Alabama. In a vain attempt to halt the enrollment of " +
                        "black students, Governor George Wallace stood in defiance, refusing to allow admission " +
                        "to these exceptional students. Almost 60 years later, America is a country more divided than " +
                        "ever, and segregation still exists albeit in a less overt form. \uD83D\uDC68\uD83C\uDFFF\uD83D\uDE80" +
                        " African Americans represent 12% of the U.S. population, yet only receive 1% of funding " +
                        "from Venture Capitalists. \uD83D\uDC69\uD83C\uDFFD\uD83D\uDE80 Latino Americans represent" +
                        " 17% of the U.S. population, yet only receive 2% of funding from Venture Capitalists." +
                        " \uD83D\uDC69\uD83D\uDE80 Females represent 58% of the U.S. population, " +
                        "yet only receive 9% of funding from Venture Capitalists. As in the case of George Wallace," +
                        " \uD83D\uDE45♂️ Venture Capitalists will continue this pattern unless they are forced to stop. " +
                        "What is a possible fix? I fully support the Peer-Selection Model by Village Capital - " +
                        "it is the equivalent of JFK sending in the infantry to force Social Equality in venture " +
                        "capital. \uD83D\uDE4B♀️ Please share this post if you support Social Equality as Large social" +
                        " change starts with small actions. \uD83D\uDE80 #diversity #inclusion #genderequality " +
                        "#venturecapital #vc #founders #ceos #nextwaveoftech #usa"
        );
        expectedActivityPost.setItemSource("md5:65bcd9a40baccabde87cc298e3832cfe");

        assertThat(actualActivityPost)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expectedActivityPost, "itemSource");

        assertThat(actualActivityPost)
                .as("actual activity source hash md5:" + DigestUtils.md5DigestAsHex(actualActivityPost.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expectedActivityPost, "itemSource");

    }

    @Test
    void shouldParseLikesThis() {
        LinkedinActivity actual = activitiesList.get(0);
        actual.getLinkedinActivityType();

        LinkedinActivity expected = new LinkedinActivity();
        expected.setLinkedinActivityType(LinkedinActivityType.LIKED_THIS);
        expected.setUpdatedAt(actual.getUpdatedAt());

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "linkedinPost");


        LinkedinPost actualActivityPost = actual.getLinkedinPost();

        LinkedinPost expectedActivityPost = new LinkedinPost();
        expectedActivityPost.setLinkedinComments(actualActivityPost.getLinkedinComments());
        expectedActivityPost.setCollectedDate(actualActivityPost.getCollectedDate());
        expectedActivityPost.setAuthorHeadline("Security Professional - Security Guarding, Video Monitoring, & Mobile Patrol");
        expectedActivityPost.setAuthorConnectionDegree("3rd+");
        expectedActivityPost.setAuthorProfileName("Ryan Vincent");
        expectedActivityPost.setAuthorProfileUrl("https://www.linkedin.com/in/ryan-vincent-security?miniProfileUrn=urn%3Ali%3Afs_miniProfile%3AACoAAA_V_kEBZwXdVwhaBuHM62I32x2ZU8sa-SM");
        expectedActivityPost.setNumberOfComments(14377);
        expectedActivityPost.setNumberOfReactions(246372);
        expectedActivityPost.setUrl("https://www.linkedin.com/feed/update/urn:li:activity:6740272758279716864");
        expectedActivityPost.setRelativePublicationDate("4mo");
        expectedActivityPost.setAbsolutePublicationDate(actualActivityPost.getAbsolutePublicationDate());
        expectedActivityPost.setContent("This is Major Bill White, the oldest living Marine at 105 years old. ❤️");
        expectedActivityPost.setItemSource("md5:53a080d1ee0b406267d3f2e507bae007");

        assertThat(actualActivityPost)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expectedActivityPost, "itemSource");

        assertThat(actualActivityPost)
                .as("actual activity source hash md5:" + DigestUtils.md5DigestAsHex(actualActivityPost.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expectedActivityPost, "itemSource");

    }

    @Test
    void shouldParseCommentedOnThis() {
        LinkedinActivity actual = activitiesList.get(4);
        actual.getLinkedinActivityType();

        LinkedinActivity expected = new LinkedinActivity();
        expected.setLinkedinActivityType(LinkedinActivityType.COMMENTED_ON);
        expected.setUpdatedAt(actual.getUpdatedAt());

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "linkedinPost");


        LinkedinPost actualActivityPost = actual.getLinkedinPost();
        System.out.println(actualActivityPost.getAuthorHeadline());

        LinkedinPost expectedActivityPost = new LinkedinPost();
        expectedActivityPost.setLinkedinComments(actualActivityPost.getLinkedinComments());
        expectedActivityPost.setCollectedDate(actualActivityPost.getCollectedDate());
        expectedActivityPost.setAuthorHeadline("I help health and wellness providers create marketing campaigns that attract new patients and convert them using our SAM Assist Automated follow-up Platform.");
        expectedActivityPost.setAuthorConnectionDegree("2nd");
        expectedActivityPost.setAuthorProfileName("Randall Chesnutt");
        expectedActivityPost.setAuthorProfileUrl("https://www.linkedin.com/in/socialmediarandall?miniProfileUrn=urn%3Ali%3Afs_miniProfile%3AACoAAATCd2YBhu4oMEEZahJsM_3sA8IebBQUNjs");
        expectedActivityPost.setNumberOfComments(43);
        expectedActivityPost.setNumberOfReactions(9);
        expectedActivityPost.setUrl("https://www.linkedin.com/feed/update/urn:li:activity:6635988898189197313");
        expectedActivityPost.setRelativePublicationDate("1yr");
        expectedActivityPost.setAbsolutePublicationDate(actualActivityPost.getAbsolutePublicationDate());
        expectedActivityPost.setContent("Which design color scheme do you prefer ? This is for a marketing campaign, I appreciate if you only pick one. 1 | 2 | 3 | 4 Thanks in advance");
        expectedActivityPost.setItemSource("md5:b55f1d0efd76800194b37fbc543e0f80");

        assertThat(actualActivityPost)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expectedActivityPost, "itemSource");

        assertThat(actualActivityPost)
                .as("actual activity source hash md5:" + DigestUtils.md5DigestAsHex(actualActivityPost.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expectedActivityPost, "itemSource");

    }

}
