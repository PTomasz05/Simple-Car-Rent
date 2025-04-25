public class Car extends Vehicle {

    public Car(String model, int km) {
        super(model, km);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.CAR;
    }
}
