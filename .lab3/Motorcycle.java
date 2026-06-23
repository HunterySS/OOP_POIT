package model;

public class Motorcycle extends Vehicle {
    private boolean hasSidecar;

    public Motorcycle() {}

    public Motorcycle(String brand, int year, double price, boolean hasSidecar) {
        super(brand, year, price);
        this.hasSidecar = hasSidecar;
    }

    public boolean isHasSidecar() { return hasSidecar; }
    public void setHasSidecar(boolean hasSidecar) { this.hasSidecar = hasSidecar; }

    @Override
    public String toString() {
        return super.toString() + ", hasSidecar=" + hasSidecar;
    }
}