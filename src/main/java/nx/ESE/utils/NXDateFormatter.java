package nx.ESE.utils;

import nx.ESE.dtos.validators.NXPattern;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NXDateFormatter {

    public static String formatterDate(Date date) {
        String fDate = "null";
        if (date != null)
            fDate = new SimpleDateFormat(NXPattern.DATE_FORMAT).format(date.getTime());
        return fDate;
    }
}
