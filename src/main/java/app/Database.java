package app.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/appliances_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin123";

    private Connection connection;
    private String tableName = "appliances"; // за замовчуванням

    public Database() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        createTableIfNotExists();
    }

    public Database(String tableName) throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        this.tableName = tableName;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "power DOUBLE, " +
                "quantity INT)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    public void addAppliance(Appliance appliance) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (name, power, quantity) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, appliance.getName());
        stmt.setDouble(2, appliance.getPower());
        stmt.setInt(3, appliance.getQuantity());
        stmt.executeUpdate();

        ResultSet keys = stmt.getGeneratedKeys();
        if (keys.next()) {
            appliance.setId(keys.getInt(1));
        }
    }

    public List<Appliance> getAllAppliances() throws SQLException {
        List<Appliance> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            double power = rs.getDouble("power");
            int quantity = rs.getInt("quantity");
            Appliance appliance = new Appliance(id, name, power, quantity);
            list.add(appliance);
        }

        return list;
    }

    public void deleteAppliance(Appliance appliance) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, appliance.getId());
        stmt.executeUpdate();
    }

    public void updateAppliance(Appliance appliance) throws SQLException {
        String sql = "UPDATE " + tableName + " SET name = ?, power = ?, quantity = ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, appliance.getName());
        stmt.setDouble(2, appliance.getPower());
        stmt.setInt(3, appliance.getQuantity());
        stmt.setInt(4, appliance.getId());
        stmt.executeUpdate();
    }

    public void clearAll() throws SQLException {
        String sql = "DELETE FROM " + tableName;
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
    }
}
