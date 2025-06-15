package test;

import app.model.Appliance;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplianceTest {

    @Test
    public void testGettersAndSetters() {
        Appliance appliance = new Appliance(1, "Мікрохвильовка", 1200.0, 1);

        assertEquals(1, appliance.getId());
        assertEquals("Мікрохвильовка", appliance.getName());
        assertEquals(1200.0, appliance.getPower(), 0.0001);
        assertEquals(1, appliance.getQuantity());

        appliance.setId(2);
        appliance.setName("Духовка");
        appliance.setPower(1500.0);
        appliance.setQuantity(3);

        assertEquals(2, appliance.getId());
        assertEquals("Духовка", appliance.getName());
        assertEquals(1500.0, appliance.getPower(), 0.0001);
        assertEquals(3, appliance.getQuantity());
    }

    @Test
    public void testToString() {
        Appliance appliance = new Appliance("Холодильник", 500.0, 2);
        String expected = "Холодильник (500.0W) x 2";
        assertEquals(expected, appliance.toString());
    }
}
