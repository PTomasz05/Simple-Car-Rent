package PT.s33623.carapp;

import PT.s33623.carapp.entity.Basket;
import PT.s33623.carapp.entity.Client;
import PT.s33623.carapp.entity.Pricelist;
import PT.s33623.carapp.entity.Wishlist;
import PT.s33623.carapp.entity.vehicle.Vehicle;
import PT.s33623.carapp.entity.vehicle.impl.Car;
import PT.s33623.carapp.entity.vehicle.impl.Delivery;
import PT.s33623.carapp.entity.vehicle.impl.Free;
import PT.s33623.carapp.entity.vehicle.impl.Vintage;
import PT.s33623.carapp.enums.PaymentMethod;
import PT.s33623.carapp.enums.VehicleType;

public class VehicleShareTest {

    static double price(Basket b, String vehicalName, boolean abonent) {
        Pricelist pl = Pricelist.getPricelist();
        double sum = 0;
        for (Vehicle v : b.items()) {
            if (v.getModel().equalsIgnoreCase(vehicalName)) {
                sum += pl.getCost(v, abonent);
            }
        }
        return sum;
    }

    public static void main(String[] args) {

        // cennik
        Pricelist cennik = Pricelist.getPricelist();

        // dodawanie nowych cen do cennika
        cennik.add(VehicleType.CAR, "Syrena", 1.5, 2.5, 100, 1.85);  	// metoda przyjmująca 6 parametrów:
        // 1.5 zł za 1 km jeśli klient posiada abonament
        // próg odległości (km): 100
        // w przeciwnym przypadku: 2.5 zł za 1 km (do 100 km), 1.85 zł za 1 km (od 101-ego kilometra)

        cennik.add(VehicleType.DELIVERY, "Żuk", 4, 150, 3);			// metoda przyjmująca 5 parametrów:
        // próg odległości (km): 150
        // 4 zł za 1 km (do 150 km)
        // 3 zł za 1 km (od 151-tego kilometra)

        cennik.add(VehicleType.VINTAGE, "Ford T", 10);			// metoda przyjmująca 3 parametry (z których drugi jest typu String):
        // 10 zł za 1 km


        cennik.add(VehicleType.FREE, 50, "Tuk-Tuk");			// metoda przyjmująca 3 parametry (z których trzeci jest typu String):
        // darmowy przejazd tylko dla abonentów (do 50 km)


        // Klient f1 deklaruje kwotę 900 zł na zamównienia; true oznacza, że klient posiada abonament
        Client f1 = new Client("f1", 900, true);

        // Klient f1 dodaje do listy życzeń różne pojazdy:
        // "Syrena" typu osobowego na maks. 80 km
        // "Żuk" typu dostawczego na maks. 200 km,
        // "Lublin" typu zabytkowego na maks. 30 km,
        // "Tuk-Tuk" typu darmowego na maks. 60 km (ale może tylko do 50 km).
        f1.add(new Car("Syrena", 80));
        f1.add(new Delivery("Żuk", 200));
        f1.add(new Vintage("Lublin", 30));
        f1.add(new Free("Tuk-Tuk", 60));

        // Lista życzeń klienta f1
        Wishlist listaF1 = f1.getWishlist();

        System.out.println("Lista życzeń klienta " + f1.getId() + ":\n" + listaF1);

        // Przed płaceniem, klient przepakuje pojazdy z listy życzeń do koszyka (po uprzednim wyczyszczeniu).
        // Możliwe, że na liście życzeń są pojazdy niemające ceny w cenniku,
        // w takim przypadku nie trafiłyby do koszyka
        Basket koszykF1 = f1.getBasket();
        f1.pack();

        // Co jest na liście życzeń klienta f1
        System.out.println("Po przepakowaniu, lista życzeń klienta " + f1.getId() + ":\n" + f1.getWishlist());

        // Co jest w koszyku klienta f1
        System.out.println("Po przepakowaniu, koszyk klienta " + f1.getId() + ":\n" + koszykF1);

        // Ile wynosi cena wszystkich pojazdów "Syrena" w koszyku klienta f1
        System.out.println("Pojazdy Syrena w koszyku klienta %client_id% kosztowały: ".replace("%client_id%", f1.getId()) + price(koszykF1, "Syrena", true));

        // Klient zapłaci...
        f1.pay(PaymentMethod.CARD, false);		// płaci kartą płatniczą, prowizja 1%
        // true oznacza, że w przypadku braku środków aplikacja sam odłoży nadmiarowe kilometry/pojazdy,
        // false oznacza rezygnację z płacenia razem z wyczyszczeniem koszyka i listy życzeń

        // Ile klientowi f1 zostało pieniędzy?
        System.out.printf("Po zapłaceniu, klientowi %client_id% zostało: %.2f zł%n".replace("%client_id%", f1.getId()),
                f1.getWallet());

        // Mogło klientowi zabraknąć srodków, wtedy, opcjonalnie, pojazdy/kilometry mogą być odkładane,
        // w przeciwnym przypadku, koszyk jest pusty po zapłaceniu
        System.out.println("Po zapłaceniu, koszyk klienta " + f1.getId() + ": " + f1.getBasket());
        System.out.println("Po zapłaceniu, koszyk klienta " + f1.getId() + ": " + koszykF1);

        // Teraz przychodzi klient dakar,
        // deklaruje 850 zł na zamówienia
        Client dakar = new Client("dakar", 850, false);

        // Zamówił za dużo jak na tę kwotę
        dakar.add(new Delivery("Żuk", 100));
        dakar.add(new Vintage("Ford T", 50));

        // Co klient dakar ma na swojej liście życzeń
        System.out.println("Lista życzeń klienta dakar: \n" + dakar.getWishlist());

        Basket koszykDakar = dakar.getBasket();
        dakar.pack();

        // Co jest na liście życzeń klienta dakar
        System.out.println("Po przepakowaniu, lista życzeń klienta " + dakar.getWishlist());

        // A co jest w koszyku klienta dakar
        System.out.println("Po przepakowaniu, koszyk klienta " + dakar.getBasket());

        // klient dakar płaci
        dakar.pay(PaymentMethod.TRANSFER, true);	// płaci przelewem, bez prowizji

        // Ile klientowi dakar zostało pieniędzy?
        System.out.printf("Po zapłaceniu, klientowi dakar zostało: %.2f zł%n",
                dakar.getWallet());

        // Co zostało w koszyku klienta dakar (za mało pieniędzy miał)
        System.out.println("Po zapłaceniu, koszyk klienta " + koszykDakar);

        dakar.returnVehicle(VehicleType.DELIVERY, "Żuk", 50);	// zwrot (do koszyka) 50 km dostawczego "Żuka" z ostatniej transakcji

        // Ile klientowi dakar zostało pieniędzy?
        System.out.println("Po zwrocie, klientowi dakar zostało: " + dakar.getWallet() + " zł");

        // Co zostało w koszyku klienta dakar
        System.out.println("Po zwrocie, koszyk klienta " + koszykDakar);

    }
}