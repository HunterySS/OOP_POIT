package model;

public class ElectricCar extends Car {
    private int batteryRange;

    public ElectricCar() {}

    public ElectricCar(String brand, int year, double price, int doors, int batteryRange) {
        super(brand, year, price, doors);
        this.batteryRange = batteryRange;
    }

    public int getBatteryRange() { return batteryRange; }
    public void setBatteryRange(int batteryRange) { this.batteryRange = batteryRange; }

    @Override
    public String toString() {
        return super.toString() + ", batteryRange=" + batteryRange;
    }
}