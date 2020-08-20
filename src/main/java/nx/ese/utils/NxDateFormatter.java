package nx.ese.utils;

import nx.ese.dtos.validators.NxPattern;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NxDateFormatter {

    private NxDateFormatter() {
        throw new IllegalStateException("Utility class");
    }

    public static String formatterDate(Date date) {
        String fDate = "null";
        if (date != null)
            fDate = new SimpleDateFormat(NxPattern.DATE_FORMAT_SHORT).format(date.getTime());
        return fDate;
    }
}
