package org.jbake.parser;

import java.util.List;

public class HeaderProcessor {

    public boolean hasHeader(List<String> contents) {
        boolean hasType = false;
        boolean hasStatus = false;
        for (String line : contents) {
            if (!line.isEmpty()) {
                if (line.startsWith("type=")) hasType = true;
                if (line.startsWith("status=")) hasStatus = true;
            }
        }
        return hasType && hasStatus;
    }

}
