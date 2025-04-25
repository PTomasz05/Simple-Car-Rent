package PT.s33623.carapp.entity.vehicle;

import PT.s33623.carapp.entity.Pricelist;
import PT.s33623.carapp.enums.VehicleType;

public abstract class Vehicle {

    protected String model;
    protected int km;    // ile km chce klient

    public Vehicle(String model, int km) {
        this.model = model;
        this.km = km;
    }

    public abstract VehicleType getType();

    public String getModel() {
        return model;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    @Override public String toString(){
        Pricelist pl = Pricelist.getPricelist();
        return String.format("%s, typ: %s, ile: %d km, %s",
                model, getType(), km, pl.describe(this, true));
    }
}
