package com.example.tunnect;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

// This class gets a timestamp and can return a time or a date based on it
public class MessageTime {

    private long MILLISECONDS_IN_HOUR = 3600000;
    private long timestamp;

    public MessageTime(long timestamp) {
        this.timestamp = timestamp;
    }

    // Determines if a time or date should be returned and does so accordingly
    public String getTimeDate() {
        ZoneId zoneId = getTimezoneID();
        ZonedDateTime currentZonedTime = ZonedDateTime.now(zoneId);
        int currentHour = currentZonedTime.getHour();
        Date date = new Date();
        long timeLastDay = date.getTime() - (MILLISECONDS_IN_HOUR * currentHour);

        if (timeLastDay > timestamp) {
            return getDate(zoneId);
        } else {
            return getTime(zoneId);
        }
    }

    // Returns a time corresponding to timestamp
    private String getTime(ZoneId zoneId) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.timestamp), zoneId);
        String hours = Integer.toString(localDateTime.getHour());
        int min = localDateTime.getMinute();
        String minutes = Integer.toString(min);

        if(min <= 9) {
            return hours+":"+"0"+minutes;
        } else {
            return hours+":"+minutes;
        }
    }

    // Returns a date corresponding to timestamp
    private String getDate(ZoneId zoneId) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.timestamp), zoneId);
        String month = getMonthAsString(localDateTime.getMonthValue());
        String day = Integer.toString(localDateTime.getDayOfMonth());

        return month+". "+day;
    }

    // Gets the timezone id
    private ZoneId getTimezoneID() {
        Calendar cal = Calendar.getInstance();
        long milliDiff = cal.get(Calendar.ZONE_OFFSET);

        // Got local offset, now loop through available timezone id(s).
        String [] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            TimeZone tz = TimeZone.getTimeZone(id);
            if (tz.getRawOffset() == milliDiff) {
                return ZoneId.of(id);
            }
        }

        return ZoneId.of("Pacific Standard Time");
    }

    // Gets an int for month and turns it into a string
    private String getMonthAsString(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            default:
                return "Dec";
        }
    }
}
