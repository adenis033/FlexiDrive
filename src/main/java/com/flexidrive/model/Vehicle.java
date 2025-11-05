public abstract class Vehicle {
    protected String licensePlate;
    protected String brand;
    protected String model;
    protected int year;

    public Vehicle(String licensePlate, String brand, String model, int year) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public abstract double calculateRentalCost(int duration);

    @Override
    public String toString() {
        return brand + " " + model + " (" + year + ") - " + licensePlate;
    }
}
