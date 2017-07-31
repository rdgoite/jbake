package org.jbake.parser;

import java.util.List;

public class HeaderProcessor {

    public boolean hasHeader(List<String> contents) {
        boolean hasType = false;
        for (String line : contents) {
            if (!line.isEmpty()) {
                if (line.startsWith("type=")) hasType = true;
            }
        }
        return hasType;
    }

}
