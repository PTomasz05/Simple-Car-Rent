package PT.ptomasz05.carapp.entity;

import PT.ptomasz05.carapp.entity.vehicle.Vehicle;
import PT.ptomasz05.carapp.entity.vehicle.impl.Car;
import PT.ptomasz05.carapp.entity.vehicle.impl.Delivery;
import PT.ptomasz05.carapp.entity.vehicle.impl.Free;
import PT.ptomasz05.carapp.entity.vehicle.impl.Vintage;
import PT.ptomasz05.carapp.enums.PaymentMethod;
import PT.ptomasz05.carapp.enums.VehicleType;

import java.util.*;
import java.util.function.BiFunction;

public class Client {
    private String id;
    private double wallet;
    private boolean abon;
    private Wishlist wish = new Wishlist();
    private Basket basket = new Basket();

    // historia ostatniej transakcji

    private Map<String, Integer> last = new HashMap<>();

    public Client(String id, double money, boolean abon) {
        this.id = id;
        wallet = money;
        this.abon = abon;
    }

    public void add(Vehicle v) {
        wish.add(v);
    }

    public Wishlist getWishlist() {
        return wish;
    }

    public Basket getBasket() {
        return basket;
    }

    public double getWallet() {
        return wallet;
    }

    public String getId() {
        return id;
    }

    private static final Map<VehicleType,
            BiFunction<String, Integer, Vehicle>> factory = Map.of(
            VehicleType.CAR, Car::new,
            VehicleType.DELIVERY, Delivery::new,
            VehicleType.VINTAGE, Vintage::new,
            VehicleType.FREE, Free::new
    );

    public void pack() {
        basket.clear();
        List<Vehicle> kopia = new ArrayList<>(wish.items());
        Pricelist p1 = Pricelist.getPricelist();

        for (Vehicle v : kopia) {
            if (p1.getCost(v, abon) >= 0) {
                if (v.getType() == VehicleType.FREE) {
                    int lim = p1.getFreeLimit(v);
                    v.setKm(Math.min(v.getKm(), lim));
                }
                basket.add(v);
                wish.remove(v);
            }
        }
    }

    public void pay(PaymentMethod pm, boolean partial) {
        Pricelist p1 = Pricelist.getPricelist();
        double prov = (pm == PaymentMethod.CARD) ? 0.01 : 0;

        last.clear();

        double fullCost = 0;
        for (Vehicle v : basket.items()) fullCost += p1.getCost(v, abon);
        fullCost += fullCost * prov;

        if (!partial && wallet < fullCost) {
            basket.clear();
            wish.items().clear();
            return;
        }

        basket.items().sort(Comparator
                .comparingDouble(v -> -p1.getCost(v,abon)/v.getKm()));
        Iterator<Vehicle> it = basket.items().iterator();
        while (it.hasNext() && wallet > 0.001) {
            Vehicle v = it.next();

            double cost = p1.getCost(v, abon);
            cost += cost * prov;

            if (wallet >= cost) {
                wallet -= cost;
                last.put(v.getType() + "#" + v.getModel(), v.getKm());
                it.remove();
            } else if (partial) {
                double unitCost = cost / v.getKm();
                int kmWeCan = (int) Math.floor(wallet / unitCost);

                if (kmWeCan > 0) {
                    double partialCost = kmWeCan * unitCost;
                    wallet -= partialCost;
                    last.put(v.getType() + "#" + v.getModel(), kmWeCan);
                    v.setKm(v.getKm() - kmWeCan);
                }
            }
        }
    }


    public void returnVehicle(VehicleType t, String model, int km) {
        String k = t + "#" + model;
        int paid = last.getOrDefault(k, 0);
        if (paid < km) {
            System.out.println("Zwrot nie-możliwy.");
            return;
        }

        Pricelist p1 = Pricelist.getPricelist();
//        Vehicle dummy = new Car(model, km);
        Vehicle dummy = new Vehicle(model, km) {
            @Override
            public VehicleType getType() {
                return t;
            }
        };

        double refund = p1.getCost(dummy, abon);
        wallet += refund;
        last.put(k, paid - km);

        basket.add(factory.get(t).apply(model, km));

    }
}
