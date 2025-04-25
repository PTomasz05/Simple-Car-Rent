package PT.ptomasz05.carapp.entity.vehicle.impl;

import PT.ptomasz05.carapp.entity.vehicle.Vehicle;
import PT.ptomasz05.carapp.enums.VehicleType;

public class Free extends Vehicle {

    public Free(String model, int km) {
        super(model, Math.min(km, 50));
    }

    @Override
    public VehicleType getType() {
        return VehicleType.FREE;
    }
}
