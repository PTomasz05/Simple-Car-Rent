public class Free extends Vehicle {

    public Free(String model, int km) {
        super(model, km);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.FREE;
    }
}
