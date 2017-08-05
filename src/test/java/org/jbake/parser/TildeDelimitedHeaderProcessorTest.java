package org.jbake.parser;

import org.apache.commons.configuration.Configuration;
import org.jbake.app.ConfigUtil;
import org.jbake.app.Crawler;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.jbake.app.Crawler.Attributes.DATE;
import static org.jbake.app.Crawler.Attributes.TAGS;
import static org.jbake.parser.TildeDelimitedHeaderProcessor.HEADER_SEPARATOR;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class TildeDelimitedHeaderProcessorTest {

    private TildeDelimitedHeaderProcessor headerProcessor = new TildeDelimitedHeaderProcessor();

    @Test
    public void testIsHeaderValid() {
        //given:
        List<String> hasHeader = asList("", "type = post", "status\t= published ", "optional=value",
                "\tsetting\t= anotherConfig  ", "date=2017-08-01", HEADER_SEPARATOR, "body text");

        //and:
        String typeProperty = "type=post";
        String statusProperty = "status=published";

        //and:
        List<String> noType = asList(statusProperty, HEADER_SEPARATOR, "body text");
        List<String> noStatus = asList(typeProperty, HEADER_SEPARATOR, "\n", "some text");
        List<String> noSeparator = asList(typeProperty, statusProperty, "this is the body");
        List<String> misplacedSeparator = asList(statusProperty, HEADER_SEPARATOR, typeProperty,
                "body");
        List<String> invalidOption = asList(statusProperty, typeProperty, "this is not valid",
                HEADER_SEPARATOR, "this is the body");
        List<String> invalidOptionWithEqualSign = asList(statusProperty, typeProperty,
                "=hasequalsign=", "valid=option", HEADER_SEPARATOR, "body text");

        //TODO add check for entries that begin with \uFEFF (BOM)

        //expect:
        assertThat(headerProcessor.isHeaderValid(hasHeader)).isTrue();

        //and:
        assertThat(headerProcessor.isHeaderValid(noType)).as("no type").isFalse();
        assertThat(headerProcessor.isHeaderValid(noStatus)).as("no status").isFalse();
        assertThat(headerProcessor.isHeaderValid(noSeparator)).as("no separator").isFalse();
        assertThat(headerProcessor.isHeaderValid(misplacedSeparator))
                .as("misplaced separator").isFalse();
        assertThat(headerProcessor.isHeaderValid(invalidOption))
                .as("invalid option").isFalse();
        assertThat(headerProcessor.isHeaderValid(invalidOptionWithEqualSign))
                .as("invalid option with equal sign").isFalse();
    }

    @Test
    public void testProcessHeader() {
        //given:
        String dateOption = String.format("%s=2013-02-27", DATE);
        List<String> contents = asList("title=About", dateOption, "type= page  ",
                "status=published", HEADER_SEPARATOR, "", "All about stuff!");

        //and:
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 1, 27, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        //and:
        Configuration configuration = setUpConfiguration();

        //when:
        Map<String, Object> header = headerProcessor.processHeader(configuration, contents);

        //then:
        assertThat(header).containsOnly(
                entry("title", "About"), entry("type", "page"), entry("status", "published"),
                entry("date", date)
        );
    }

    @Test
    public void testProcessHeaderWithBOMKey() {
        //given:
        List<String> contents = asList("type=post", "status=published", "\uFEFFkey=value",
                HEADER_SEPARATOR, "", "this is the body");

        //when:
        Map<String, Object> header = headerProcessor.processHeader(setUpConfiguration(), contents);

        //then:
        assertThat(header).containsOnly(
                entry("type", "post"), entry("status", "published"), entry("key", "value")
        );
    }

    @Test
    public void testProcessHeaderContainingTags() {
        //given:
        String tags = String.format("%s=java,programming,algorithms", TAGS);
        List<String> contents = asList("type=post", "status=published", tags, HEADER_SEPARATOR,
                "", "this is the body");

        //when:
        Map<String, Object> header = headerProcessor.processHeader(setUpConfiguration(), contents);

        //then:
        assertThat(header).containsKey(TAGS);
        Object tagsObject = header.get(TAGS);
        assertThat(tagsObject).isInstanceOf(String[].class);
    }

    private Configuration setUpConfiguration() {
        Configuration configuration = mock(Configuration.class);
        doReturn("yyyy-MM-dd").when(configuration).getString(ConfigUtil.Keys.DATE_FORMAT);
        return configuration;
    }

}
