public class Security implements NumberInterface {
    private String nameSecurity;
    private double pricePerGuard;

    // Constructor
    public Security(String nameSecurity, double pricePerGuard) {
        this.nameSecurity = nameSecurity;
        this.pricePerGuard = pricePerGuard;
    }

    public double getPricePerGuard() {
        return pricePerGuard;
    }
}

// indepenent class: Festival depends on Security, like a car depends on an engine. Festival can't exist without Security,
// but Security can exist by itself