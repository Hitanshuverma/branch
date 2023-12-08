package com.example.branchint.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Constants {
    //api consts
    public static final String ROOT_URL = "https://android-messaging.branch.co/";
    public static final String LOGIN_URL = "api/login";
    public static final String MESSAGE_URL = "api/messages";

    //user consts
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AUTH_TOKEN = "token";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";

    public static final String KEY_AGENT_ID = "id";

    public static final String Key_THREAD = "thread";

    //util functions
    public static String convertToNormalTime(String iso8601DateTime) {
        try {
            // Define the input date format (ISO 8601)
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Define the desired output date format
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = iso8601Format.parse(iso8601DateTime);
            return checkDate(outputFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null; // Return null if the conversion fails
    }
    private static String checkDate(String inputDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(inputDate);

            Calendar calendar = Calendar.getInstance(); // Get the current date and time
            Calendar inputCalendar = Calendar.getInstance();
            inputCalendar.setTime(date);

            if (inputCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                    && inputCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                    && inputCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                // The input date is today, return time only
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                return timeFormat.format(date);
            } else {
                // The input date is not today, return date only
                SimpleDateFormat dateFormatOnly = new SimpleDateFormat("dd MMM yyyy");
                dateFormatOnly.setDateFormatSymbols(new DateFormatSymbols() {
                    @Override
                    public String[] getShortMonths() {
                        return new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    }
                });
                return dateFormatOnly.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null for invalid input or if an error occurs
    }


}
