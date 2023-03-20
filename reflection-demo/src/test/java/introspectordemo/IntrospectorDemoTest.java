package introspectordemo;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntrospectorDemoTest {

    @Test
    public void introspector() throws Exception {
        new IntrospectorDemo().inspector();
    }

    @Test
    public void introspector2() throws Exception {
        new IntrospectorDemo().inspector2();
    }
}