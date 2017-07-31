package org.jbake.parser;

import java.util.List;
import java.util.regex.Pattern;

public class HeaderProcessor {

    public static final String HEADER_SEPARATOR = "~~~~~~";

    private static final Pattern OPTION_PATTERN = Pattern.compile(
            "^\\p{Space}*\\p{Alnum}+\\p{Space}*=\\p{Space}*\\p{Alnum}+\\p{Space}*$");

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

}
