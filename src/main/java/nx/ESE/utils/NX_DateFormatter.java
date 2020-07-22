package nx.ESE.utils;

import nx.ESE.dtos.validators.NX_Pattern;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NX_DateFormatter {

    public static String formatterDate(Date date) {
        String fDate = "null";
        if (date != null)
            fDate = new SimpleDateFormat(NX_Pattern.DATE_FORMAT).format(date.getTime());
        return fDate;
    }
}
