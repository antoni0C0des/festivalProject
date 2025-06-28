public class Solo extends Artist {

    protected boolean dressingRoom;
    protected int managerPhone;

    // Constructor
    public Solo(String type, String artistName, String genre, boolean main_event, int price,
                int duration, int capacity, boolean confirmed, boolean dressing_room, int manager_number) {
        super(type, artistName, genre, main_event, price, duration, capacity, confirmed);
        this.dressingRoom = dressing_room;
        this.managerPhone = manager_number;
    }

    // Getters
    public boolean getDressingRoom() {
        return dressingRoom;
    }

    @Override
    public boolean hasStand() {
        return false; // Soloists never have stands
    }

    public String toString() {
        return "Soloist: " + artistName +
                " | Genre: " + genre +
                " | Principal artist? " + mainEvent +
                " | Ticket price: " + ticketPrice + " euros" +
                " | Duration: " + duration + " minutes" +
                " | Capacity: " + capacity + " thousand attendees" +
                " | Is assisting? " + confirmed +
                " | Needs dressing room? " + dressingRoom +
                " | Manager phone: " + managerPhone;
    }
}

