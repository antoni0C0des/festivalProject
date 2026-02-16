public class Security implements NumberInterface {
    private String nameSecurity; // Declaring here can be skipped just fine
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

// independent class: Festival depends on Security, like a car depends on an engine. Festival can't exist without Security,
// but Security can exist by itself