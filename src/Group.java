public class Group extends Artist {

    protected boolean stand;
    protected int groupNumber;

    // Constructor method
    public Group(String type, String artistName, String genre, boolean main_event,
                 int price, int time, int numPeople, boolean confirmed, int groupNumber, boolean stand) {
        super(type, artistName, genre, main_event, price, time, numPeople, confirmed);
        this.stand = stand;
        this.groupNumber = groupNumber;
    }

    @Override
    public boolean hasStand() {
        return stand;
    }

    public String toString() {
        return "Group: " + artistName +
                " | Genre: " + genre +
                " | Principal artist? " + mainEvent +
                " | Ticket price: " + ticketPrice + " euros" +
                " | Duration: " + duration + " minutes" +
                " | Capacity: " + capacity + " thousand attendees" +
                " | Is assisting? " + confirmed +
                " | Members number: " + groupNumber +
                " | Selling merch? " + stand;
    } // the toString does not need to fetch the type of artist. We have two different toStrings per child class,
    // so we can write Group or Soloist next to the name. This makes better formatting
}
