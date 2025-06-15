import app.model.Appliance;
import app.model.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseTest {

    private Database db;

    @Before
    public void setUp() throws SQLException {
        db = new Database("test_appliances"); // ізольована тестова таблиця
        db.clearAll(); // очистити перед кожним тестом
    }

    @After
    public void tearDown() throws SQLException {
        db.clearAll(); // очистити після кожного тесту
    }

    @Test
    public void testAddAndGetAppliance() throws SQLException {
        Appliance appliance = new Appliance("Тостер", 700, 1);
        db.addAppliance(appliance);

        List<Appliance> appliances = db.getAllAppliances();

        assertEquals(1, appliances.size());
        assertEquals("Тостер", appliances.get(0).getName());
        assertEquals(700, appliances.get(0).getPower(), 0.001);
        assertEquals(1, appliances.get(0).getQuantity());
    }

    @Test
    public void testUpdateAppliance() throws SQLException {
        Appliance appliance = new Appliance("Холодильник", 1500, 1);
        db.addAppliance(appliance);

        appliance.setName("Новий Холодильник");
        appliance.setPower(1800);
        appliance.setQuantity(2);
        db.updateAppliance(appliance);

        List<Appliance> appliances = db.getAllAppliances();

        assertEquals(1, appliances.size());
        assertEquals("Новий Холодильник", appliances.get(0).getName());
        assertEquals(1800, appliances.get(0).getPower(), 0.001);
        assertEquals(2, appliances.get(0).getQuantity());
    }

    @Test
    public void testDeleteAppliance() throws SQLException {
        Appliance appliance = new Appliance("Пилосос", 1200, 1);
        db.addAppliance(appliance);
        assertEquals(1, db.getAllAppliances().size());

        db.deleteAppliance(appliance);
        assertEquals(0, db.getAllAppliances().size());
    }
}
