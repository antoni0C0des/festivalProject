public class MaxTicketsReachedException extends Exception {
    public MaxTicketsReachedException(String e) {
        super(e);
    }
}

// Exception made for the addTicket method that gets thrown if Attendee tries to buy more than 7 tickets
