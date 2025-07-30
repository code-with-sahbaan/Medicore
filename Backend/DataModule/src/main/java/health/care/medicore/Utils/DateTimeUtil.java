package health.care.medicore.Utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a");

    public static LocalDateTime convertEpochMillitoLocalDateTime(long epochMilli){
        return Instant.ofEpochMilli(epochMilli)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static long convertLocalDateTimeToMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String formatLocalTime(LocalTime localTime) {
        return localTime.format(formatter);
    }
}
