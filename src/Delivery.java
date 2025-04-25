public class Delivery extends Vehicle {

    public Delivery(String model, int km) {
        super(model, km);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.DELIVERY;
    }
}
