public class VIP extends Attendee {

    protected int cardNumberVIP;

    // Constructor
    public VIP(String nombre, String dni, String tarjeta, boolean frecuente, boolean vip, int cardNumberVIP) {
        super(nombre, dni, tarjeta, frecuente, vip);
        this.cardNumberVIP = cardNumberVIP;
    }

    @Override
    public String toString() {
        return "VIP: " + nameAttendee +
                " | DNI: " + dni +
                " | CC: " + creditCard +
                " | Previous attendee? " + frequent +
                " | VIP? " + vip +
                " | VIP number: " + cardNumberVIP;
    }
}
