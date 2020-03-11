package io.alerium.timednpc.utils;

import java.util.concurrent.TimeUnit;

public final class TimeUtil {

    private TimeUtil() {
    }

    /**
     * This method parse a human time format to milliseconds
     * @param s The time
     * @return The parsed milliseconds
     */
    public static long toMillis(String s) {
        String[] split = s.split(" ");
        
        long time = 0;
        for (String part : split)
            time += partToMillis(part);
        
        return time;
    }

    /**
     * This method formats the seconds to a format like this 10h3m10s
     * @param millis The milliseconds to parse
     * @return The parsed String
     */
    public static String toFormatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        seconds -= minutes * 60;
        minutes -= hours * 60;
        hours -= days * 24;
        
        StringBuilder builder = new StringBuilder();
        
        if (days > 0)
            builder.append(days).append("d ");
        if (hours > 0)
            builder.append(hours).append("h ");
        if (minutes > 0)
            builder.append(minutes).append("m ");
        if (seconds > 0)
            builder.append(seconds);
        else 
            builder.append(0);
        
        builder.append("s");
        
        return builder.toString();
    }

    /**
     * This method parses a part of a human time format
     * @param part The part of the time
     * @return The time in milliseconds, 0 if there is an error
     */
    private static long partToMillis(String part) {
        char type = part.charAt(part.length() - 1);
        int time = Integer.parseInt(part.substring(0, part.length() - 1));
        
        switch (type) {
            case 'd': return TimeUnit.DAYS.toMillis(time);
            case 'h': return TimeUnit.HOURS.toMillis(time);
            case 'm': return TimeUnit.MINUTES.toMillis(time);
            case 's': return TimeUnit.SECONDS.toMillis(time);
                
            default:
                return 0;
        }
    }
    
}
