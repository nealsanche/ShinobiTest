package com.robotsandpencils.shinobitest;

import com.shinobicontrols.charts.NumberAxis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by neal on 2014-06-03.
 */
public class DomainAxis extends NumberAxis {

    public enum Domain {
        TIME, DEPTH
    }

    private boolean mDisplayEnabled;
    private SimpleDateFormat mTimeFormat;
    private SimpleDateFormat mDateFormat;

    private final Domain mDomain;

    public DomainAxis(Domain domain, TimeZone timeZone) {
        this(domain, false, timeZone);
    }

    public DomainAxis(Domain domain, boolean displayEnabled, TimeZone timeZone) {
        super();
        mDomain = domain;
        mDisplayEnabled = displayEnabled;
        mTimeFormat = new SimpleDateFormat("HH:mm:ss");
        mDateFormat = new SimpleDateFormat("MMM d");
        mTimeFormat.setTimeZone(timeZone);
        mDateFormat.setTimeZone(timeZone);
    }

    @Override
    public String getFormattedString(Double value) {
        if (mDisplayEnabled) {
            switch (mDomain) {
                case TIME:
                    return getFormattedDateString(value, false);
            }
        }
        return null;
    }

    public String getFormattedDateString(Double value, boolean withDepth) {
        Date currentDate = DomainHelper.dateFromDouble(-value);
        String dateString = mTimeFormat.format(currentDate);
        if (dateString.equals("00:00:00")) {
            // Replace with date instead
            dateString = mDateFormat.format(currentDate);
        }

        if (dateString.endsWith(":00")) {
            dateString = dateString.substring(0, 5);
        }

        return String.format("%s", dateString);
    }
}
