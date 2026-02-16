public class Attendee {

    protected String nameAttendee, dni, creditCard;
    protected boolean frequent, vip;
    protected Ticket[] ticket;
    protected int numberTickets;

    //Constructor
    public Attendee(String nameAttendee, String dni, String creditCard, boolean frequent, boolean vip) {
        this.nameAttendee = nameAttendee;
        this.dni = dni;
        this.creditCard = creditCard;
        this.frequent = frequent;
        this.vip = vip;
        this.ticket = new Ticket[NumberInterface.MAX_TICKETS]; // Won't pass ticket to constructor, we already initialize it here
        this.numberTickets = 0;
    }

    //Getters
    public String getNameAttendee() {
        return nameAttendee;
    }
    public String getDni() {
        return dni;
    }
    public boolean getFrequent() {
        return frequent;
    }
    public boolean getVip() {
        return vip;
    }
    public Ticket[] getTickets() {
        return ticket;
    }
    public int getNumberTickets() {
        return numberTickets;
    }

    //Ticket adder method
    /* make new Ticket object in the array of tickets that an Attendee has. We add 1
    to numberTickets so it is more human-like for both the client and us the programmers. It has the artist object */
    public boolean addTicket(Artist artist) throws MaxTicketsReachedException {
        if (numberTickets >= ticket.length) {
            throw new MaxTicketsReachedException("The attendee has reached the maximum number of tickets allowed");
        }

        ticket[numberTickets] = new Ticket(numberTickets + 1, artist);
        numberTickets++; // this changes position in the array to add new Ticket(s)
        return true; // If method did its job, we return true (for checking)
    }

    @Override
    public String toString() {
        return "Attendee: " + nameAttendee +
                " | DNI: " + dni +
                " | CC: " + creditCard +
                " | Previous attendee? " + frequent +
                " | VIP? " + vip;
    }
}
