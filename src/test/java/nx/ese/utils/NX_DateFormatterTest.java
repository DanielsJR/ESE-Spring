package nx.ese.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NX_DateFormatterTest {

    @Test
    public void formatterDate(){
        Date date = new GregorianCalendar(2020, Calendar.NOVEMBER, 27).getTime();
        String formattedDate = NX_DateFormatter.formatterDate(date);
        assertEquals("27/11/2020",formattedDate);
    }
}
