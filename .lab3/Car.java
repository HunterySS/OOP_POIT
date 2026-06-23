package model;

public class Car extends Vehicle {
    private int doors;

    public Car() {}

    public Car(String brand, int year, double price, int doors) {
        super(brand, year, price);
        this.doors = doors;
    }

    public int getDoors() { return doors; }
    public void setDoors(int doors) { this.doors = doors; }

    @Override
    public String toString() {
        return super.toString() + ", doors=" + doors;
    }
}