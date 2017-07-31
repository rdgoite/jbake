package org.jbake.parser;

import java.util.List;
import java.util.regex.Pattern;

public class HeaderProcessor {

    /*TODO consider white spaces using the following pattern (also consider horizontal tabs):
          "^\\p{Space}*\\p{Alnum}+\\p{Space}*=\\p{Space}*\\p{Alnum}+\\p{Space}*$"
    */
    private static final Pattern OPTION_PATTERN = Pattern.compile(
            "^\\p{Space}*\\p{Alnum}+=\\p{Alnum}+\\p{Space}*$");

    public boolean isHeaderValid(List<String> contents) {
        boolean hasType = false;
        boolean hasStatus = false;
        boolean hasSeparator = false;
        boolean wellFormedOptions = true;
        for (String line : contents) {
            if (!line.isEmpty()) {
                if (OPTION_PATTERN.matcher(line).matches()) {
                    if (line.startsWith("type=")) hasType = true;
                    if (line.startsWith("status=")) hasStatus = true;
                } else if (line.equals("~~~~~~")) {
                    hasSeparator = true;
                    break;
                } else {
                    wellFormedOptions = false;
                    break;
                }
            }
        }
        return hasType && hasStatus && hasSeparator && wellFormedOptions;
    }

}
