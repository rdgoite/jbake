package org.jbake.parser;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class HeaderProcessorTest {

    private HeaderProcessor headerProcessor = new HeaderProcessor();

    @Test
    public void testHasHeader() {
        //given:
        String typeProperty = "type=post";
        String statusProperty = "status=published";
        String separator = "~~~~~~";

        //and:
        List<String> hasHeader = asList("", typeProperty, statusProperty, separator, "body text");
        List<String> noType = asList(statusProperty, separator, "body text");
        List<String> noStatus = asList(typeProperty, separator, "\n", "some text");

        //expect:
        assertThat(headerProcessor.hasHeader(hasHeader)).isTrue();
        assertThat(headerProcessor.hasHeader(noType)).as("no type = no header").isFalse();
        assertThat(headerProcessor.hasHeader(noStatus)).as("no status = no header").isFalse();
    }

}
