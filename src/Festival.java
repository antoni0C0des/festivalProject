public class Festival implements NumberInterface {

    private Artist[] artistList;
    private Attendee[] attendeeList;

    //Constructor
    public Festival(Artist[] artistList, Attendee[] attendeeList) {
        this.artistList = artistList;
        this.attendeeList = attendeeList;
    }

    // ---------- CORE METHODS ------------------------------------- //
    private int fetchArtistArrayPos(String artistName) {
        for (int i=0; i<artistList.length; i++) {
            if (artistList[i] != null && artistList[i].getArtistName() != null &&
                    artistList[i].getArtistName().equalsIgnoreCase(artistName)) {
                return i;
            }
        }
        return -1;
    } // These methods can look for the position of an Artist or Attendee, by just using as input a dni or artistName

    private int fetchAttendeePos(String dni) {
        for (int i = 0; i < attendeeList.length; i++) {
            if (attendeeList[i] != null && attendeeList[i].getDni() != null &&
                    attendeeList[i].getDni().equalsIgnoreCase(dni)) {
                return i;
            }
        }
        return -1; // Failure of search gives -1. This is handled by the bottom auxiliary methods
    }

    public boolean doesArtisExist(String artistName) {
        return fetchArtistArrayPos(artistName) != -1;
    } // These are handler methods that avoid crashes. They check if poseFetcher methods failed and give the menu ways to handle wrong dni/name inputs

    public boolean doesAttendeeExist(String dni) {
        return fetchAttendeePos(dni) != -1;
    } // They depend on the poseFetcher methods, but these checkers are used often in the menu, that's why they're public

    // --------- METHODS --------------------------------------- //
    // 1. method showInfoArtist. toString turns the data from Artist(s) into readable String(s), we loop in the Artist array so we can run toString at each position
    public String showArtistInfo() {
        String info = "";

        for (int i=0; i < artistList.length; i++) {
            if (artistList[i] != null) {
                info += artistList[i].toString() + "\n";
            }
        }
        return info;
    }

    // 2. method calculatePriceSecurityCompany. It gets an amount of things to protect (assuming
    // the festival is full) and distributes it to an amount of guards. Data: 250E per guard, 1 for every 500 ppl, 2 for every merch, 1 per dressRoom
    public double calcPriceSecurityCompany(Security security) {
        int merchToProtect = 0;
        int dressingRoomToProtect = 0;
        int peopleToProtect = 0; // start with empty values and keep adding up when iterating
        double pricePerGuard = security.getPricePerGuard(); // we call our dependency class Security for the price of a guard

        for (int i=0; i< artistList.length; i++) {
            if (artistList[i] != null) { // SKIP NULL POSITIONS
                peopleToProtect += artistList[i].getCapacity(); // gets how many people are in the festival and adds up that num in each iteration
                if (artistList[i].hasStand()) {
                    merchToProtect++;
                } //adds up if the artist has a stand aka merch
                if (artistList[i] instanceof Solo && ((Solo) artistList[i]).getDressingRoom()) {
                    dressingRoomToProtect++;
                } //adds up if the artist has a dressing room. Only Solo has this property so we use instanceof
            }
        }
        int numGuards = (peopleToProtect * PEOPLE_RATE / 500) + (merchToProtect * 2) + dressingRoomToProtect; // the code gets the num of guards needed given the criteria and all the things to protect
        return numGuards * pricePerGuard; // It is a good practice to always return price values as doubles, even if we get an int in this case
    }

    // 3. method calculateCostTicket and showTicketCost. calculateCostTicket checks registered attendees (By DNI input), gets a starting price (from artistList[pos].getTicketPrice),
    // and calculates a ticket price based on DISCOUNTS from the Interface class.
    private double fetchDiscountTicket(String artistName, String dni) {
        int artistPos = fetchArtistArrayPos(artistName);
        int attendeePos = fetchAttendeePos(dni);
        double finalPrice = artistList[artistPos].getTicketPrice();

        if (attendeeList[attendeePos].getVip()) { //are they VIP?
            finalPrice *= (1 - DISCOUNT_VIP / 100.0);
        }
        if (attendeeList[attendeePos].getFrequent()) { // are they frequent?
            finalPrice *= (1 - DISCOUNT_PREVIOUS_ATTENDEE / 100.0);
        }
        return Math.round(finalPrice * 100.0) / 100.0; // math.round keeps the two decimals by multiply by 100 and then dividing by 100
    }

    public String showTicketCost(String dni, String artistName) {
        int attendeePos = fetchAttendeePos(dni);
        int artistPos = fetchArtistArrayPos(artistName);
        String informationString = "";

        if (!artistList[artistPos].getConfirmed()) {
            informationString += "WARNING: artist is not confirmed to assist yet. \n";
        }
        if (attendeeList[attendeePos].getVip()) {
            informationString += "VIP attendee! Will add " + DISCOUNT_VIP + " % discount...\n";
        }
        if (attendeeList[attendeePos].getFrequent()) {
            informationString += "Previous attendee! Will add " + DISCOUNT_PREVIOUS_ATTENDEE + " % discount...\n";
        }

        informationString += "Price to the concert is: " + artistList[artistPos].getTicketPrice() + " euros\n"; // original price

        if (attendeeList[attendeePos].getVip() || attendeeList[attendeePos].getFrequent()) { //if an attendee is not VIP or frequent it is useless to attempt calculations (fetchDiscount)
            informationString += "For being eligible to discounts, the final price is: " + fetchDiscountTicket(artistName, dni) + " euros\n" + "Disclaimer: " +
                    "discounts are accumulative" + "\n"; // calc price with discounts if applicable
        }
        return informationString;
    }

    // 4. method buyEverythingConfirmed: tot. money if attendee wishes to go in all CONFIRMED HEADLINE artists
    // and buy a t-shirt at each concert (with applied discounts)
    public double buyEverythingConfirmed(String dni) {
        int attendeePos = fetchAttendeePos(dni);
        double fullConcertPrice = 0;
        double fullMerchPrice = 0;
        double ticketPrice;
        double tShirtPrice = SHIRT_PRICE; // can suffer changes so we use the constant to load a variable

        for (int i = 0; i < artistList.length; i++) {
            if (artistList[i] != null) {
                // SKIP empty Artist in the array. Such skippers will be used often
                if (artistList[i].getConfirmed() && artistList[i].getMainEvent()) { // checker if they're confirmed AND main artists (headliner)
                    ticketPrice = fetchDiscountTicket(artistList[i].getArtistName(), dni); // discount fetcher
                    fullConcertPrice += ticketPrice;
                }
                if (artistList[i].hasStand()) {
                    if (attendeeList[attendeePos].getFrequent()) {
                        tShirtPrice *= (1 - (double) MERCH_DISCOUNT_PREVIOUS / 100); // here is an integer division in a float-point context, so we cast numerator as a double.
                    }                                                                // why? because this makes sure the result is a double (we are in a price-value context, we might manage decimals)
                    fullMerchPrice += tShirtPrice;
                }
            }
        }
        return Math.round((fullConcertPrice + fullMerchPrice) * 100.0) / 100.0;
    }

    // 5. purchaseTicket: Query 5 in menu can create Attendee(s) or purchase Ticket(s) for Attendee. If we try to look for an Attendee with an unknown dni input,
    // we pass input data to attendeeMaker to create Attendee. If attendee happens to exist, the logic to buy tickets is done by Attendee.addTicket, check execution flow at case 5 of menu.
    public Attendee getAttendeeByDni(String dni) {
        int attendeePos = fetchAttendeePos(dni);
        return attendeeList[attendeePos];
    } // UTIL METHODS that return Object(s)
    public Artist getArtistByName (String artistName) {
        int artistPos = fetchArtistArrayPos(artistName);
        return artistList[artistPos];
    }

    public void attendeeMaker(String dni, String nameAttendee, String creditCard, boolean frequent, boolean vip, int cardNumberVIP) {
        for (int i = 0; i < attendeeList.length; i++) {
            if (attendeeList[i] == null) { // look for empty positions to write a new Attendee in the array
                if (vip) {
                    Attendee newVIPAttendee = new VIP(nameAttendee, dni, creditCard, frequent, true, cardNumberVIP); // mk new VIP attendee with input data
                    attendeeList[i] = newVIPAttendee; //we're at a null pos, so we can add the data at the position we're at
                }
                else {
                    Attendee newAttendee = new Attendee(nameAttendee, dni, creditCard, frequent, false);
                    attendeeList[i] = newAttendee;
                }
                return; // without this return, the method loops until it fills the Attendee array (this is bad!)
            }
        }
        throw new IllegalStateException("No more attendees can be added (max of " + MAX_ATTENDEES + ")"); //
    }

    public void artistMaker(String type, String artistName, String genre, boolean mainArtist, int ticketPrice, int duration,int capacity, boolean assisting, int numMembers, boolean sellMerch, boolean needsDressRoom, int mgrPhone) {
        for (int i = 0; i < artistList.length; i++) {
            if (artistList[i] == null) {
                if (type.equalsIgnoreCase("g")) {
                    Artist newGroup = new Group(type, artistName, genre, mainArtist, ticketPrice, duration, capacity, assisting, numMembers, sellMerch);
                    artistList[i] = newGroup;
                }
                else {
                    Artist newSolo = new Solo(type, artistName, genre, mainArtist, ticketPrice, duration, capacity, assisting, needsDressRoom, mgrPhone);
                    artistList[i] = newSolo;
                }
                return;
            }
        }
    }

    // 6. Show info of ticket that an attendee has purchased. It gets the specific Attendee out of input dni, then we loop through
    // their Ticket array and perform .toString to each Object instance to get readable data
    public String showTicketInfo(String dni){
        int attendeePos = fetchAttendeePos(dni);
        Attendee attendee = attendeeList[attendeePos];
        String ticketInfo = "Tickets for " + attendee.getNameAttendee() + ":\n";

        if (attendee.getNumberTickets() != 0) {
            for (int i = 0; i < attendee.getNumberTickets(); i++) {
                Ticket ticket = attendee.getTickets()[i]; //note - getTickets returns an array so we loop through the array
                ticketInfo += ticket.toString() + "\n"; // concatenate all the data
            }
        } else ticketInfo = "Attendee " + attendee.getNameAttendee() + " has purchased no tickets!"; // if no tickets bought, warn and halt
        return ticketInfo;
    }

    // 7. Show info of concerts with merch stand for which a VIP attendee has purchased tickets. Like query 6 but more case specific.
    public String concertStandInfo4Vip(String dni) {
        int attendeePos = fetchAttendeePos(dni);
        Attendee attendee = attendeeList[attendeePos];
        String concertInfo = "Concerts with merch for VIP attendee " + attendee.getNameAttendee() + ":\n";
        boolean standDetected = false; // small checker for cases where a VIP attendee has bought concerts with NO stands

        if (attendee instanceof VIP) {
        // if (attendee.getVip()) { ----- used to be like this, but instanceof is better
            if (attendee.getNumberTickets() != 0) {
                for (int i = 0; i < attendee.getNumberTickets(); i++) {
                    Ticket ticket = attendee.getTickets()[i]; // get Ticket(s) of that Attendee & loop to get all instances.
                    Artist artist = ticket.getArtist(); // get Artist corresponding to that ticket
                    if (artist.hasStand()) {
                        standDetected = true;
                        concertInfo += ticket.toString() + "\n"; // Sometimes the compiler does implicit calls at toString *(when doing string concatenation) Sometimes, it won't. You might see this grayed out as redundant
                    }
                    if (!standDetected) {
                        concertInfo = "VIP attendee " + attendee.getNameAttendee() + " has purchased no tickets with stands!" + "\n";
                    }
                }
            } else concertInfo = "VIP attendee " + attendee.getNameAttendee() + " has purchased no tickets!" + "\n"; // if 0 tickets purchased, warn the user and halt
        } else concertInfo = attendee.getNameAttendee() + " is not VIP!"; // if not VIP, warn, over-write and halt
        return concertInfo;
    }

    // Secret debug method for attendees (call as if it was option 9)
    public String showAttendeeInfo() {
        String info = "";

        for (int i=0; i < attendeeList.length; i++) {
            if (attendeeList[i] != null) {
                info += attendeeList[i].toString() + "\n";
            }
        }
        return info; // NOT A QUERY - ONLY SERVES FOR DEBUG PURPOSES / DATA FETCHING
    }
}

