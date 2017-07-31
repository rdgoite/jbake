package org.jbake.parser;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jbake.parser.TildeDelimitedHeaderProcessor.HEADER_SEPARATOR;
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
        List<String> contents = asList("title=About", "date=2013-02-27", "type=page",
                "status=published", "~~~~~~", "", "All about stuff!");

        //when:
        Configuration configuration = mock(Configuration.class);
        Map<String, Object> header = headerProcessor.processHeader(configuration, contents);

        //then:
        assertThat(header).isNotNull();
    }

}
