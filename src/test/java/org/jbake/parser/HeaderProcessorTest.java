package org.jbake.parser;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class HeaderProcessorTest {

    private HeaderProcessor headerProcessor = new HeaderProcessor();

    @Test
    public void testIsHeaderValid() {
        //given:
        String typeProperty = "type=post";
        String statusProperty = "status=published";
        String separator = "~~~~~~";

        //and:
        List<String> hasHeader = asList("", typeProperty, statusProperty, separator, "body text");
        List<String> noType = asList(statusProperty, separator, "body text");
        List<String> noStatus = asList(typeProperty, separator, "\n", "some text");
        List<String> noSeparator = asList(typeProperty, statusProperty, "this is the body");
        List<String> misplacedSeparator = asList(statusProperty, separator, typeProperty, "body");

        //expect:
        assertThat(headerProcessor.isHeaderValid(hasHeader)).isTrue();

        //and:
        assertThat(headerProcessor.isHeaderValid(noType)).as("no type").isFalse();
        assertThat(headerProcessor.isHeaderValid(noStatus)).as("no status").isFalse();
        assertThat(headerProcessor.isHeaderValid(noSeparator)).as("no separator").isFalse();
        assertThat(headerProcessor.isHeaderValid(misplacedSeparator))
                .as("misplaced separator").isFalse();
    }

}
