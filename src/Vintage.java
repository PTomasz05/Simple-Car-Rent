public class Vintage extends Vehicle {

    public Vintage(String model, int km) {
        super(model, km);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.VINTAGE;
    }
}
