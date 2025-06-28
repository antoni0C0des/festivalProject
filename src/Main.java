import java.io.*;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Artist[] artist = leerArtistas("Artistas.txt");
            Attendee[] attendee = leerAsistentes("Asistentes.txt");
            Security security = new Security("Evento Seguro S.L.", NumberInterface.GUARD_PRICE);
            Festival festival = new Festival("Festival", "Ciudad Real", artist, attendee, NumberInterface.MAX_ARTISTS, NumberInterface.MAX_ATTENDEES);
            System.out.println("Booting festival management system..." + "\n" + "Welcome to the festival management system. Select an option");
            menuExecute(festival, security);
        } catch (FileNotFoundException e) {
            System.out.println("Files missing! Please check that 'Artistas.txt' and 'Attendees.txt' are properly loaded");
        }

        /* test toString section ------ DEBUG LEGACY ------
        Attendee attendeetest = new Attendee("Antonio", "55550000A", "1234567890123456", false, false);
        System.out.println(attendeetest.toString()); //creates objects to be inserted in the array.

        Attendee VIPtest = new VIP("Messi", "555A", "1234567890123456", false, true, 3232);
        System.out.println(VIPtest.toString());

        Artist soloTest = new Solo("s", "Quevedo", "reggaeton", false, 40, 60, 5, true, false, 987654321);
        System.out.println(soloTest.toString());

        Artist groupTest = new Group("g", "Muse", "rock", true, 90, 120, 30, true, 3, false);
        System.out.println(groupTest.toString()); */
    }

    // UTIL METHODS: input reader. Saves up a few lines of code and time
    private static String dniInputTool() {
        System.out.println("Enter DNI/ID of attendee");
        return scanner.next();
    }
    private static String artistNameInputTool() {
        System.out.println("Enter name of the artist");
        return scanner.next();
    }

    // MENU METHOD
    private static void menuExecute(Festival festival, Security security) {
        int option;
        String artistName, dni;
        boolean displayMenu = true;

        while (displayMenu) {
            try {
                System.out.println("1. Show information of all the scheduled artists");
                System.out.println("2. Show the estimated price of the security service");
                System.out.println("3. Show ticket price for registered attendee at specific concert");
                System.out.println("4. Show price for an attendee if they assisted to all headline confirmed concerts & bought a T-shirt at each one");
                System.out.println("5. Register attendee / purchase ticket");
                System.out.println("6. Show ticket info from a specific attendee");
                System.out.println("7. Show info of concerts with merch stands for which a VIP attendee has purchased tickets ");
                System.out.println("8. Exit");
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        System.out.println("Creating list\n");
                        System.out.println(festival.showArtistInfo());
                        break;

                    case 2:
                        System.out.println("Security costs " + festival.calcPriceSecurityCompany(security) + " Euros" + "\n");
                        break;

                    case 3:
                        dni = dniInputTool();
                        artistName = artistNameInputTool();

                        if (festival.doesArtisExist(artistName) && festival.doesAttendeeExist(dni)) {
                            System.out.println(festival.showTicketCost(dni, artistName));
                        } else System.out.println("Either the artist does not exist nor the attendee (or they might not be registered) - please check data (1) or register attendee (5) and try again" + "\n");
                        break;

                    case 4:
                        dni = dniInputTool();

                        if (festival.doesAttendeeExist(dni)) {
                            System.out.println("Calculated estimated cost: " + festival.buyEverythingConfirmed(dni) + " euros");
                            System.out.println("Disclaimer: taking in account if attendee is VIP or assisted previously." + "\n");
                        } else System.out.println("This attendee does not exist nor is registered - please check the data and try again. Option 5 allows to register attendees" + "\n");
                        break;

                    case 5:
                        dni = dniInputTool();
                        Attendee attendeeCall;
                        Artist artistCall;

                        // See this as two modules to gather data: we jump to one or another based on the existence of an Attendee, gathered from input attendeeDni
                        if (festival.doesAttendeeExist(dni)) {
                            attendeeCall = festival.getAttendeeByDni(dni); // create the Attendee corresponding to attendeeDni
                            System.out.println("Attendee appears to exist. Purchasing ticket...");
                            artistName = artistNameInputTool();
                            if (festival.doesArtisExist(artistName)) { // use checker method to see if input artist exist
                                artistCall = festival.getArtistByName(artistName);
                                if (!artistCall.getConfirmed()) System.out.println("Warning: this artist is not confirmed"); // small block to warn for unconfirmed Artist
                                try {
                                    if (attendeeCall.addTicket(artistCall)) { //IMPORTANT: uses addTicket method from Attendee
                                        System.out.println("Ticket added"); // if method returned true, it means it ran successfully
                                    }
                                } catch (MaxTicketsReachedException e) { // if method did not run successfully, exception is thrown, to be caught here
                                    System.out.println("Error: " + e.getMessage());
                                }
                            } else System.out.println("This artist does not seem to exist - please check data (option 1) and try again"); // remember that we halt if artist does not exist

                        } else {
                            System.out.println("Attendee does not exist. You can create one and then add tickets to them. Proceed? (y/n)");
                            String response = scanner.next(); // not a query request but this was used to debug, it's also a QOL feature
                            if (response.equalsIgnoreCase("y")) {
                                System.out.println("Enter the name:");
                                String nameAttendee = scanner.next();
                                System.out.println("Enter their credit card data:");
                                String creditCard = scanner.next();
                                System.out.println("Are they previous attendees to the festival? (true/false)");
                                boolean frequent = scanner.nextBoolean();
                                System.out.println("Are they VIP attendees? (true/false)");
                                boolean vip = scanner.nextBoolean();
                                int vipCardNum = 0;
                                if (vip) {
                                    System.out.println("Enter VIP card number");
                                    vipCardNum = scanner.nextInt();
                                }
                                try {
                                    festival.attendeeMaker(dni, nameAttendee, creditCard, frequent, vip, vipCardNum);
                                    System.out.println("Attendee created! Please return to (5) to add tickets to this attendee. ");
                                } catch (IllegalStateException e) {
                                    System.out.println("Error: " + e.getMessage()); //MAX of 20 attendees. If more than 20, an exception is thrown. Not a query, but important
                                }
                            } else {
                                System.out.println("Operation canceled."); // We halt if user does not type "y"
                            }
                        }
                        break;

                    case 6:
                        dni = dniInputTool();
                        if (festival.doesAttendeeExist(dni)) {
                            String ticketInfo = festival.showTicketInfo(dni);
                            System.out.println(ticketInfo);
                        } else System.out.println("This attendee does not exist nor is registered - please check the data and try again. Option 5 allows to register attendees");
                        break;

                    case 7:
                        dni = dniInputTool();
                        if (festival.doesAttendeeExist(dni)) {
                            System.out.println(festival.concertStandInfo4Vip(dni));
                        } else {
                            System.out.println("Attendee not registered");
                        }
                        break;

                    case 8:
                        displayMenu = false;
                        System.out.println("Stopping system");
                        break;

                    case 9: // DEBUG CASE
                        System.out.println("Creating list (You found a debug module from the dev!)\n");
                        System.out.println(festival.showAttendeeInfo());
                        break;

                    case 10: // implementacion de una query adicional, crear artistas y anadirlos en la lista y al programa
                        boolean caso = true; // esto es pra el while que te obliga a seguir hasta que pongas uno que no existe

                        try (FileWriter escribir = new FileWriter("Artistas.txt", true);
                             BufferedWriter buffer = new BufferedWriter(escribir);
                             PrintWriter salida = new PrintWriter(buffer)) { // todo el porro de meter archivos de texto

                            System.out.println("Registering new artist. Please enter the name");
                            while (caso) {
                                artistName = scanner.next();
                                if (festival.doesArtisExist(artistName)) { // espero que tengas este metodo o algo similar
                                    System.out.println("This artist exists!");
                                } else {
                                    caso = false; // para que el while loop no siga
                                    // variables predeterminadas porque no puedes pasar un metodo que le falte parametros
                                    boolean mainArtist;
                                    int ticketPrice;
                                    int duration;
                                    int capacity;
                                    boolean assisting;
                                    int numMembers = 0;
                                    boolean sellMerch = false;
                                    boolean needsDressingRoom = false; // estas variables las inicializo porq no puedes pasar params vacios a un metodo. De todas formas, el metodo de crear artistas
                                    int managerPhone = 0;              // es listo y no va a meter valores equivocados por lo q no pasa nada si pasas por ej 0 en un tipo grupo

                                    System.out.println("Group or solo? please write 'g' or 's'");
                                    String type = scanner.next();

                                    if (!type.equals("g") && !type.equals("s")) {
                                        throw new InputMismatchException("invalid input"); // cosa opcional que no estaria feo que pongas
                                    }

                                    System.out.println("Genre?");
                                    String genre = scanner.next();
                                    System.out.println("Is main artist?");
                                    mainArtist = scanner.nextBoolean();
                                    System.out.println("Ticket Price?");
                                    ticketPrice = scanner.nextInt();
                                    System.out.println("Duration?");
                                    duration = scanner.nextInt();
                                    System.out.println("Capacity? (in thousands, write the number compacted)");
                                    capacity = scanner.nextInt();
                                    System.out.println("Is assisting?");
                                    assisting = scanner.nextBoolean();

                                    salida.println();
                                    salida.print(type + " ");
                                    salida.print(artistName + " ");
                                    salida.print(genre + " ");
                                    salida.print(mainArtist + " ");
                                    salida.print(ticketPrice + " ");
                                    salida.print(duration + " ");
                                    salida.print(capacity + " ");
                                    salida.print(assisting + " ");

                                    if (type.equals("g")) {
                                        System.out.println("Number of members?");
                                        numMembers = scanner.nextInt();
                                        salida.print(numMembers + " ");

                                        System.out.println("Sells merch?");
                                        sellMerch = scanner.nextBoolean();
                                        salida.print(sellMerch + " ");
                                    }

                                    if (type.equals("s")) {
                                        System.out.println("needs dressing room?");
                                        needsDressingRoom = scanner.nextBoolean();
                                        salida.print(needsDressingRoom + " ");

                                        System.out.println("Enter manager phone number (only numbers!)");
                                        managerPhone = scanner.nextInt();
                                        salida.print(managerPhone + " ");
                                        System.out.println("finished!");
                                    }

                                    System.out.println("Creating artist...");
                                    festival.artistMaker(type, artistName, genre, mainArtist, ticketPrice, duration, capacity, assisting, numMembers, sellMerch, needsDressingRoom, managerPhone);
                                }
                            }
                        }
                        break;

                    default:
                        throw new InputMismatchException();
                }
            } catch (InputMismatchException | IOException e) {
                System.out.println("Unknown/invalid input or not in menu - please try again");
                scanner.nextLine();
            }
        }
    }

    //Attendee reader - DON'T TOUCH
    public static Attendee[] leerAsistentes(String cadena) throws FileNotFoundException {
        File f = new File(cadena);
        Scanner scanner = new Scanner(f);

        Attendee[] attendee = new Attendee[NumberInterface.MAX_ATTENDEES]; // initialize fixed array of 20 positions for Attendee
        int i = 0;

        String nombre, dni, tarjeta;
        boolean frecuente, vip;
        int tarjetaVIP;

        while (scanner.hasNext()) {
            nombre = scanner.next();
            dni = scanner.next();
            tarjeta = scanner.next();
            frecuente = scanner.nextBoolean();
            vip = scanner.nextBoolean();

            if (vip) {
                tarjetaVIP = scanner.nextInt();
                attendee[i] = new VIP(nombre, dni, tarjeta, frecuente, true, tarjetaVIP);
            } else {
                attendee[i] = new Attendee(nombre, dni, tarjeta, frecuente, false);
            }
            i++;
        }
        scanner.close();
        return attendee;

    }

    //Artist reader - DON'T TOUCH
    public static Artist[] leerArtistas(String cadena) throws FileNotFoundException {
        File g = new File(cadena);
        Scanner scanner = new Scanner(g);

        Artist[] artist = new Artist[NumberInterface.MAX_ARTISTS]; // Create fixed array of Artist
        int i = 0;

        String nombre, genre, type;
        boolean attendance, main_event, merch, dressingRoom;
        int price, numPeople, time, phoneMngr, groupNum;

        while (scanner.hasNext()) {
            type = scanner.next();
            nombre = scanner.next();
            genre = scanner.next();
            main_event = scanner.nextBoolean();
            price = scanner.nextInt();
            time = scanner.nextInt();
            numPeople = scanner.nextInt();
            attendance = scanner.nextBoolean();

            if (Objects.equals(type, "g")) {
                groupNum = scanner.nextInt();
                merch = scanner.nextBoolean();
                artist[i] = new Group(type, nombre, genre, main_event, price, time, numPeople, attendance, groupNum, merch);
            } else if (Objects.equals(type, "s")) {
                dressingRoom = scanner.nextBoolean();
                phoneMngr = scanner.nextInt();
                artist[i] = new Solo (type, nombre, genre, main_event, price, time, numPeople, attendance, dressingRoom, phoneMngr);
            }
            i++; // after each read, go to next array
        }
        scanner.close();
        return artist;
    }
}

