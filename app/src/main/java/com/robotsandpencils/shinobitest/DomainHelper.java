package com.robotsandpencils.shinobitest;

import java.util.Date;

/**
 * Created by neal on 2014-09-25.
 */
public class DomainHelper {

    public static Date dateFromDouble(Double value) {
        if (value == null) return null;
        long time = (long) (double) (value);
        return new Date(time);
    }

    public static Double doubleFromDate(Date value) {
        long time = value.getTime();
        return (double) time;
    }

    public static Double doubleFromLong(Long value) {
        if (value == null) return null;
        return (double) (long) value;
    }

    public static Long longFromDouble(Double value) {
        if (value == null) return null;
        return (long) (double) value;
    }
}
