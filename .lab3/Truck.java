package model;

public class Truck extends Vehicle {
    private double loadCapacity;

    public Truck() {}

    public Truck(String brand, int year, double price, double loadCapacity) {
        super(brand, year, price);
        this.loadCapacity = loadCapacity;
    }

    public double getLoadCapacity() { return loadCapacity; }
    public void setLoadCapacity(double loadCapacity) { this.loadCapacity = loadCapacity; }

    @Override
    public String toString() {
        return super.toString() + ", loadCapacity=" + loadCapacity;
    }
}