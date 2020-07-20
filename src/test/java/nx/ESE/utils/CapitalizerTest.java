package nx.ESE.utils;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CapitalizerTest {

    @Test
    public void testCapitalizer() {
        String withoutCap = "san ramon";
        String cap = Capitalizer.capitalizer(withoutCap);
        assertEquals("San Ramon", cap);
    }

}
