package org.jbake.parser;

import org.apache.commons.configuration.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TildeDelimitedHeaderProcessor implements HeaderProcessor {

    public static final String HEADER_SEPARATOR = "~~~~~~";

    private static final Pattern OPTION_PATTERN = Pattern.compile(
            "^\\p{Space}*(\\p{Alnum}+)\\p{Space}*=\\p{Space}*(\\p{ASCII}+)\\p{Space}*$");

    @Override
    public boolean isHeaderValid(List<String> contents) {
        boolean hasType = false;
        boolean hasStatus = false;
        boolean hasSeparator = false;
        boolean wellFormedOptions = true;
        for (String line : contents) {
            if (!line.isEmpty()) {
                if (OPTION_PATTERN.matcher(line).matches()) {
                    if (line.trim().startsWith("type")) hasType = true;
                    if (line.trim().startsWith("status")) hasStatus = true;
                } else {
                    if (line.equals(HEADER_SEPARATOR)) hasSeparator = true;
                    else wellFormedOptions = false;
                    break;
                }
            }
        }
        return hasType && hasStatus && hasSeparator && wellFormedOptions;
    }

    @Override
    public Map<String, Object> processHeader(Configuration configuration, List<String> contents) {
        Map<String, Object> header = new HashMap<String, Object>();
        for (String line : contents) {
            Matcher matcher = OPTION_PATTERN.matcher(line);
            if (matcher.matches()) header.put(matcher.group(1), matcher.group(2));
        }
        return header;
    }

}
