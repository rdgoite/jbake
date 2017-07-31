package org.jbake.parser;

import java.util.List;

public interface HeaderProcessor {

    boolean isHeaderValid(List<String> contents);

}
