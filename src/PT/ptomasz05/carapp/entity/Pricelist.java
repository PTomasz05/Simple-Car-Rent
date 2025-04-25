package PT.ptomasz05.carapp.entity;

import PT.ptomasz05.carapp.entity.vehicle.Vehicle;
import PT.ptomasz05.carapp.enums.VehicleType;

import java.util.*;

public class Pricelist {

    private static Pricelist instance = new Pricelist();

    private static class PriceInfo {
        double sub;     // cena dla abonanemtu (-1 jeśli brak)
        double p1, p2;  // cena 1 i 2
        int threshold;
        int freeLimit;
    }

    private final Map<String, PriceInfo> map = new HashMap<>();

    private static String key(VehicleType t, String m) {
        return t + "#" + m;
    }

    private Pricelist() {
    }

    public static Pricelist getPricelist() {
        return instance;
    }

    // 6 parametrów

    public void add(VehicleType t, String m, double sub,
                    double p1, int thr, double p2) {
        PriceInfo pi = new PriceInfo();
        pi.sub = sub;
        pi.p1 = p1;
        pi.threshold = thr;
        pi.p2 = p2;
        map.put(key(t, m), pi);
    }

    // 5 parametrów

    public void add(VehicleType t, String m, double p1, int thr, double p2) {
        add(t, m, -1, p1, thr, p2);
    }

    // 3 parametry

    public void add(VehicleType t, String m, double p1) {
        PriceInfo pi = new PriceInfo();
        pi.sub = -1;
        pi.p1 = p1;
        pi.threshold = 0;
        map.put(key(t, m), pi);
    }

    // free
    public void add(VehicleType t, int limit, String m) {
        PriceInfo pi = new PriceInfo();
        pi.freeLimit = limit;
        map.put(key(t, m), pi);
    }

    // zwraca cenę całości dla danej liczby km lub -1 jeśli brak

    public double getCost(Vehicle v, boolean abonent) {
        PriceInfo pi = map.get(key(v.getType(), v.getModel()));
        if (pi == null) return -1;

        int km = v.getKm();

        if (v.getType() == VehicleType.FREE) {
            if (!abonent) {
                return -1;
            }
            km = Math.min(km, pi.freeLimit);
        }
        if (abonent && pi.sub >= 0) {
            return km * pi.sub;
        }

        if (pi.threshold == 0 || km <= pi.threshold) {
            return km * pi.p1;
        }
        return pi.threshold * pi.p1
                + (km - pi.threshold) * pi.p2;
    }

    public int getFreeLimit(Vehicle v) {
        PriceInfo pi = map.get(key(v.getType(), v.getModel()));
        return (pi == null) ? 0 : pi.freeLimit;
    }

    public String describe(Vehicle v, boolean abon) {
        PriceInfo pi = map.get(key(v.getType(), v.getModel()));
        if (pi == null) {
            return "ceny brak";
        }

        if (v.getType() == VehicleType.FREE) {
            return "cena 0.00";
        }

        if (abon && pi.sub >= 0) {
            //Locale.US użyty, aby zamiast 2,00 było 2.00
            return String.format(Locale.US, "cena %.2f", pi.sub);
        }

        if (pi.threshold == 0) {
            return String.format(Locale.US, "cena %.2f", pi.p1);
        }
        return String.format(Locale.US, "cena %.2f (do %d), %.2f (od %d)",
                pi.p1, pi.threshold, pi.p2, pi.threshold + 1);
    }


}
