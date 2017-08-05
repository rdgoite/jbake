package org.jbake.parser;

import org.apache.commons.configuration.Configuration;
import org.jbake.app.ConfigUtil;
import org.jbake.app.ConfigUtil.Keys;
import org.jbake.app.Crawler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TildeDelimitedHeaderProcessor implements HeaderProcessor {

    public static final String HEADER_SEPARATOR = "~~~~~~";

    private static final Pattern OPTION_PATTERN = Pattern.compile(
            "^\uFEFF??\\p{Space}*(\\p{Alnum}+)\\p{Space}*=\\p{Space}*(\\p{ASCII}+)\\p{Space}*$");

    private static final char BYTE_ORDER_MARKER = '\uFEFF';
    private static final char BLANK = '\u0000';

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
        DateFormat dateFormat = new SimpleDateFormat(configuration.getString(Keys.DATE_FORMAT));
        for (String line : contents) {
            Matcher matcher = OPTION_PATTERN.matcher(line);
            if (matcher.matches()) {
                String key = matcher.group(1).trim().replace(BYTE_ORDER_MARKER, BLANK);
                Object value = matcher.group(2).trim();
                if (Crawler.Attributes.DATE.equalsIgnoreCase(key))  {
                    try {
                        value = dateFormat.parse(value.toString());
                    } catch (ParseException e) {
                        //TODO handle this better
                        e.printStackTrace();
                    }
                }
                if (Crawler.Attributes.TAGS.equalsIgnoreCase(key)) {
                    value = new String[0];
                }
                header.put(key, value);
            }
        }
        return header;
    }

}
