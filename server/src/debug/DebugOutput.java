package debug;

import java.time.ZonedDateTime;

public class DebugOutput {
    private static String preparePrefix() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        return "[Server @" + dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond() + "] ";
    }
}