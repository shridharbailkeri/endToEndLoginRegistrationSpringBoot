package com.dailycodework.sbend2endapplication.registration.token;

import java.util.Calendar;
import java.util.Date;

public class TokenExpirationTime {

    private static final int EXPIRATION_TIME = 10;

    public static Date getExpirationTime() {
        //It creates a new instance of the Calendar class using the getInstance() method.
        //The calendar is set to the current time by invoking the setTimeInMillis() method
        //with the argument new Date().getTime()
        // This sets the calendar's time to the current system time in milliseconds.
        //Retrieving the time value of a Date object using the getTime() method, which returns the number of milliseconds since January 1, 1970
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        //The method adds a specified number of minutes to the calendar using the add() method.
        // add few minutes after the token gets generated
        //The constant EXPIRATION_TIME represents the number of minutes to add.
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
        //The expression calendar.getTime().getTime() is used to obtain the time value in
        // milliseconds from a Calendar object and then create a Date object using that time value.
    }
}
