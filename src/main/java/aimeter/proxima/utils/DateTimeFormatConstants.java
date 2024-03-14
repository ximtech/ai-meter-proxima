package aimeter.proxima.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeFormatConstants {

    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy.MM.dd HH:mm";
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_YYYY_MM_DD_HH_MM);
    
    public static final DateTimeFormatter IMAGE_FILE_NAME_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");

}
