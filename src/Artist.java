public abstract class Artist {

    protected String artistName, type;
    protected String genre;
    protected boolean mainEvent, confirmed;
    protected int ticketPrice, duration, capacity;

    // Constructor method
    public Artist(String type, String artistName, String genre, boolean mainEvent,
                  int ticketPrice, int duration, int capacity, boolean confirmed) {

        this.type=type;
        this.artistName=artistName;
        this.genre=genre;
        this.mainEvent = mainEvent;
        this.ticketPrice = ticketPrice;
        this.duration=duration;
        this.capacity=capacity;
        this.confirmed= confirmed;
    }

    // Getters
    public String getType() {
        return type;
    }
    public String getArtistName() {
        return artistName;
    }
    public String getGenre() {
        return genre;
    }
    public boolean getMainEvent() {
        return mainEvent;
    }
    public boolean getConfirmed() {
        return confirmed;
    }
    public int getTicketPrice() {
        return ticketPrice;
    }
    public int getDuration() {
        return duration;
    }
    public int getCapacity() {
        return capacity;
    }

    // alt methods
    public abstract boolean hasStand(); // abstract method - Group can have stands or not have them
}


