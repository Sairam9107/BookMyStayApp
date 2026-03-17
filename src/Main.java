/**
 * UseCase2RoomInitialization.java
 *
 * This class demonstrates Use Case 2 of the Book My Stay App:
 * Basic Room Types & Static Availability.
 * It introduces abstraction, inheritance, and simple availability representation.
 *
 * @author YourName
 * @version 2.1
 */

// Step 1: Abstract Room class
abstract class Room {
    private String type;
    private int beds;
    private double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    // Abstract method to display room details
    public abstract void displayDetails();
}

// Step 2: Concrete Room classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getType() +
                " | Beds: " + getBeds() +
                " | Price: ₹" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getType() +
                " | Beds: " + getBeds() +
                " | Price: ₹" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getType() +
                " | Beds: " + getBeds() +
                " | Price: ₹" + getPrice());
    }
}

// Step 3: Application Entry Point
 class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println("   Welcome to Book My Stay App!");
        System.out.println("   Hotel Booking System v2.1");
        System.out.println("=======================================\n");

        // Initialize room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability representation
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        // Display room details and availability
        single.displayDetails();
        System.out.println("Availability: " + singleAvailability + " rooms\n");

        doubleRoom.displayDetails();
        System.out.println("Availability: " + doubleAvailability + " rooms\n");

        suite.displayDetails();
        System.out.println("Availability: " + suiteAvailability + " rooms\n");
    }
}