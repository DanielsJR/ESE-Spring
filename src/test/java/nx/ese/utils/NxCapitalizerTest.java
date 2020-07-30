package nx.ese.utils;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NxCapitalizerTest {

    @Test
    public void testCapitalizer() {
        String withoutCap = "san ramon";
        String cap = NxCapitalizer.capitalizer(withoutCap);
        assertEquals("San Ramon", cap);
    }

}
