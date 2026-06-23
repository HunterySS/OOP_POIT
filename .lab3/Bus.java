package model;

public class Bus extends Vehicle {
    private int passengerCapacity;

    public Bus() {}

    public Bus(String brand, int year, double price, int passengerCapacity) {
        super(brand, year, price);
        this.passengerCapacity = passengerCapacity;
    }

    public int getPassengerCapacity() { return passengerCapacity; }
    public void setPassengerCapacity(int passengerCapacity) { this.passengerCapacity = passengerCapacity; }

    @Override
    public String toString() {
        return super.toString() + ", passengerCapacity=" + passengerCapacity;
    }
}