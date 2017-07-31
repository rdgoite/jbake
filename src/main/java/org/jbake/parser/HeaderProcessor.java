package org.jbake.parser;

import org.apache.commons.configuration.Configuration;

import java.util.List;
import java.util.Map;

public interface HeaderProcessor {

    boolean isHeaderValid(List<String> contents);

    Map<String,Object> processHeader(Configuration configuration, List<String> contents);

}
