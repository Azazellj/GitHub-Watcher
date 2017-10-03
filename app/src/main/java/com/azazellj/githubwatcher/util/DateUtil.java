package com.azazellj.githubwatcher.util;

import org.joda.time.DateTime;

/**
 * Created by azazellj on 10/3/17.
 */

public class DateUtil {

    public static final String PATTERN_DEFAULT = "d MMMM',' yyyy";

    public static String getFormattedDate(String date, String pattern) {
        return new DateTime(date).toString(pattern);
    }

}
