package PT.ptomasz05.carapp.entity.vehicle.impl;

import PT.ptomasz05.carapp.entity.vehicle.Vehicle;
import PT.ptomasz05.carapp.enums.VehicleType;

public class Delivery extends Vehicle {

    public Delivery(String model, int km) {
        super(model, km);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.DELIVERY;
    }
}
