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
        List<String> contents = asList("", "type=post", "status=published", "~~~~~~", "body text");

        //expect:
        assertThat(headerProcessor.hasHeader(contents)).isTrue();
    }

}
