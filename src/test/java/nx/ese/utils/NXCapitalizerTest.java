package nx.ese.utils;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NXCapitalizerTest {

    @Test
    public void testCapitalizer() {
        String withoutCap = "san ramon";
        String cap = NX_Capitalizer.capitalizer(withoutCap);
        assertEquals("San Ramon", cap);
    }

}
