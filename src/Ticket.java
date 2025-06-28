public class Ticket {

    private final int ticketNumber;
    private final Artist artist;

    // Constructor
    public Ticket(int ticketNumber, Artist artist) {
        this.ticketNumber = ticketNumber;
        this.artist = artist;
    }

    // Getters
    public Artist getArtist() {
        return artist;
    }
    // we get the artist data because that's what the info in the ticket is

    @Override
    public String toString() {
        return "Ticket ID: " + ticketNumber +
                " | Artist: " + artist.getArtistName() +
                " | (g)roup/(s)oloist? " + artist.getType() +
                " | Genre: " + artist.getGenre() +
                " | Main event? " + artist.getMainEvent() +
                " | Price: " + artist.getTicketPrice() + " euros" +
                " | Duration: " + artist.getDuration() + " minutes" +
                " | Capacity: " + artist.getCapacity() + " thousands" +
                " | Confirmed artist? " + artist.getConfirmed();
    }
}