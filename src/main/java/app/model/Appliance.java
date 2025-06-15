package app.model;

public class Appliance {
    private int id;             // унікальний ідентифікатор (для БД)
    private String name;        // назва приладу
    private double power;       // потужність у ватах
    private int quantity;       // кількість одиниць

    public Appliance(int id, String name, double power, int quantity) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.quantity = quantity;
    }

    public Appliance(String name, double power, int quantity) {
        this(-1, name, power, quantity);
    }

    // геттери і сеттери
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name + " (" + power + "W) x " + quantity;
    }
}
