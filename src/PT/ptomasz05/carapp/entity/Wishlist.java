package PT.ptomasz05.carapp.entity;

import PT.ptomasz05.carapp.entity.vehicle.Vehicle;

import java.util.*;

public class Wishlist {
    private final List<Vehicle> list = new ArrayList<>();

    public void add(Vehicle v) {
        list.add(v);
    }

    public void remove(Vehicle v) {
        list.remove(v);
    }

    public List<Vehicle> items() {
        return list;
    }

    @Override
    public String toString() {
        if (list.isEmpty()) {
            return "-- pusto";
        }
        StringBuilder sb = new StringBuilder();
        list.forEach(v -> sb.append(v).append('\n'));
        return sb.toString();
    }
}
