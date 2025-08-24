// NumberInterface: an Interface that stores all values in this project that are constants, with the purpose of
// having a centralized place to manage them across the project
// Most frequent implementation of the Interface are in Festival. But other usages in other classes are present.

public interface NumberInterface {

    // Section of discounts
    int DISCOUNT_PREVIOUS_ATTENDEE = 10;
    int DISCOUNT_VIP = 15;
    int MERCH_DISCOUNT_PREVIOUS = 15;

    // Section of prices (Euros)
    int TSHIRT_PRICE = 25;
    int GUARD_PRICE = 250;

    // Section of limits
    int MAX_ARTISTS = 20;
    int MAX_ATTENDEES = 20;
    int MAX_TICKETS = 7;

     // Values are in thousands of people. For calculations related to this, we need to multiply by 1000
    int PEOPLE_RATE = 1000;
}
