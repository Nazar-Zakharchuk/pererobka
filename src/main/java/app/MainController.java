package app;

import app.model.Appliance;
import app.model.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    @FXML
    ListView<Appliance> applianceList;

    @FXML
    TextField nameField;

    @FXML
    TextField powerField;

    @FXML
    TextField quantityField;

    @FXML
    TextField searchField;

    @FXML
    ComboBox<String> sortComboBox;

    @FXML
    Label totalPowerLabel;

    ObservableList<Appliance> appliances = FXCollections.observableArrayList();
    FilteredList<Appliance> filteredAppliances;
    SortedList<Appliance> sortedAppliances;
    Database database;

    // Логгер
    private static final Logger logger = LoggerConfig.getLogger();

    @FXML
    public void initialize() {
        LoggerConfig.setup();
        logger.addHandler(new EmailErrorHandler());

        try {
            database = new Database();
            appliances.addAll(database.getAllAppliances());
            logger.info("Зчитано прилади з бази даних");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Помилка при зчитуванні з бази даних", e);
            showAlert("Помилка бази даних", e.getMessage());
        }

        filteredAppliances = new FilteredList<>(appliances, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase().trim();
            filteredAppliances.setPredicate(appliance -> {
                if (filter.isEmpty()) {
                    return true;
                }
                boolean matchesName = appliance.getName().toLowerCase().contains(filter);
                boolean matchesPower = String.valueOf(appliance.getPower()).contains(filter);
                return matchesName || matchesPower;
            });
        });

        sortedAppliances = new SortedList<>(filteredAppliances);
        applianceList.setItems(sortedAppliances);

        sortComboBox.setItems(FXCollections.observableArrayList(
                "Без сортування",
                "За зростанням потужності",
                "За спаданням потужності"
        ));
        sortComboBox.getSelectionModel().selectFirst();

        sortComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.equals("Без сортування")) {
                sortedAppliances.setComparator(null);
            } else if (newVal.equals("За зростанням потужності")) {
                sortedAppliances.setComparator(Comparator.comparingDouble(Appliance::getPower));
            } else if (newVal.equals("За спаданням потужності")) {
                sortedAppliances.setComparator(Comparator.comparingDouble(Appliance::getPower).reversed());
            }
        });
    }

    @FXML
    void handleAddAppliance() {
        String name = nameField.getText().trim();
        String powerStr = powerField.getText().trim();
        String quantityStr = quantityField.getText().trim();

        if (name.isEmpty() || powerStr.isEmpty() || quantityStr.isEmpty()) {
            showAlert("Помилка вводу", "Будь ласка, заповніть усі поля.");
            logger.warning("Спроба додати прилад з пустими полями");
            return;
        }

        double power;
        int quantity;
        try {
            power = Double.parseDouble(powerStr);
            quantity = Integer.parseInt(quantityStr);
            if (power <= 0 || quantity <= 0) {
                showAlert("Помилка вводу", "Потужність і кількість мають бути більшими за 0.");
                logger.warning("Некоректні значення потужності або кількості при додаванні");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Помилка вводу", "Потужність і кількість мають бути числовими значеннями.");
            logger.warning("Невірний формат числових даних при додаванні приладу");
            return;
        }

        Appliance newAppliance = new Appliance(name, power, quantity);

        try {
            database.addAppliance(newAppliance);
            appliances.add(newAppliance);
            logger.info("Додано прилад: " + newAppliance);
            clearInputFields();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Помилка при додаванні приладу до бази даних", e);
            showAlert("Помилка бази даних", e.getMessage());
        }
    }

    @FXML
    void handleDeleteAppliance() {
        Appliance selected = applianceList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Помилка", "Будь ласка, виберіть прилад для видалення.");
            logger.warning("Спроба видалити прилад без вибору");
            return;
        }

        try {
            database.deleteAppliance(selected);
            appliances.remove(selected);
            logger.info("Видалено прилад: " + selected);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Помилка при видаленні приладу з бази даних", e);
            showAlert("Помилка бази даних", e.getMessage());
        }
    }

    @FXML
    void handleUpdateAppliance() {
        Appliance selected = applianceList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Помилка", "Будь ласка, виберіть прилад для оновлення.");
            logger.warning("Спроба оновити прилад без вибору");
            return;
        }

        String name = nameField.getText().trim();
        String powerStr = powerField.getText().trim();
        String quantityStr = quantityField.getText().trim();

        if (name.isEmpty() || powerStr.isEmpty() || quantityStr.isEmpty()) {
            showAlert("Помилка вводу", "Будь ласка, заповніть усі поля.");
            logger.warning("Некоректні дані для оновлення приладу");
            return;
        }

        double power;
        int quantity;
        try {
            power = Double.parseDouble(powerStr);
            quantity = Integer.parseInt(quantityStr);
            if (power <= 0 || quantity <= 0) {
                showAlert("Помилка вводу", "Потужність і кількість мають бути більшими за 0.");
                logger.warning("Некоректні значення потужності або кількості при оновленні");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Помилка вводу", "Потужність і кількість мають бути числовими значеннями.");
            logger.warning("Невірний формат числових даних при оновленні приладу");
            return;
        }

        selected.setName(name);
        selected.setPower(power);
        selected.setQuantity(quantity);

        try {
            database.updateAppliance(selected);
            applianceList.refresh();
            logger.info("Оновлено прилад: " + selected);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Помилка при оновленні приладу в базі даних", e);
            showAlert("Помилка бази даних", e.getMessage());
        }
    }

    @FXML
    void handleCalculateTotalPower() {
        double totalPower = appliances.stream()
                .mapToDouble(a -> a.getPower() * a.getQuantity())
                .sum();
        totalPowerLabel.setText(String.format("Загальна потужність: %.2f Вт", totalPower));
        logger.info("Розраховано загальну потужність: " + totalPower + " Вт");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearInputFields() {
        nameField.clear();
        powerField.clear();
        quantityField.clear();
    }
}
