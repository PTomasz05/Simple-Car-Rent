package PT.s33623.carapp.enums;

public enum VehicleType {

    CAR, DELIVERY, VINTAGE, FREE;


    @Override
    public String toString() {
        return switch (this) {
            case CAR -> "osobowy";
            case DELIVERY -> "dostawczy";
            case VINTAGE -> "zabytkowy";
            case FREE -> "darmowy";
        };
    }
}

