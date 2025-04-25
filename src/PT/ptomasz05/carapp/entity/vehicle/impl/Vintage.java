package PT.s33623.carapp.entity.vehicle.impl;

import PT.s33623.carapp.entity.vehicle.Vehicle;
import PT.s33623.carapp.enums.VehicleType;

public class Vintage extends Vehicle {

    public Vintage(String model, int km) {
        super(model, km);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.VINTAGE;
    }
}
