import java.io.*;
import java.util.*;

// 1. Reservation Class (Serializable)
class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String guestName;
    private String roomType;
    private String assignedRoomId;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setAssignedRoomId(String id) {
        this.assignedRoomId = id;
    }

    public String getAssignedRoomId() {
        return assignedRoomId;
    }

    public String toString() {
        return "Guest: " + guestName +
                ", RoomType: " + roomType +
                ", RoomID: " + assignedRoomId;
    }
}


// 2. Inventory Service (Serializable)
class InventoryService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availableCounts = new HashMap<>();
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public InventoryService() {

        availableCounts.put("Single", 5);
        availableCounts.put("Double", 3);
        availableCounts.put("Suite", 2);

        allocatedRooms.put("Single", new HashSet<>());
        allocatedRooms.put("Double", new HashSet<>());
        allocatedRooms.put("Suite", new HashSet<>());
    }

    public boolean isAvailable(String roomType) {
        return availableCounts.getOrDefault(roomType, 0) > 0;
    }

    public String allocateRoom(String roomType) {

        if (!isAvailable(roomType))
            return null;

        String roomId =
                roomType.substring(0,1).toUpperCase()
                        + (100 + allocatedRooms.get(roomType).size() + 1);

        allocatedRooms.get(roomType).add(roomId);

        availableCounts.put(
                roomType,
                availableCounts.get(roomType) - 1
        );

        return roomId;
    }

    public void printInventory() {

        System.out.println("\nCurrent Inventory State:");

        for(String type : availableCounts.keySet()) {
            System.out.println(type + " available: " + availableCounts.get(type));
        }
    }
}


// 3. Booking History (Serializable)
class BookingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Reservation> confirmedBookings = new ArrayList<>();

    public void addBooking(Reservation r) {
        confirmedBookings.add(r);
    }

    public List<Reservation> getAllBookings() {
        return confirmedBookings;
    }

    public void printHistory() {

        System.out.println("\nBooking History:");

        for(Reservation r : confirmedBookings) {
            System.out.println(r);
        }
    }
}


// 4. Booking Service
class BookingService {

    private InventoryService inventory;
    private BookingHistory history;

    public BookingService(
            InventoryService inventory,
            BookingHistory history
    ) {
        this.inventory = inventory;
        this.history = history;
    }

    public void bookRoom(Reservation reservation) {

        String roomId =
                inventory.allocateRoom(reservation.getRoomType());

        if(roomId != null) {

            reservation.setAssignedRoomId(roomId);

            history.addBooking(reservation);

            System.out.println(
                    "✅ Confirmed: "
                            + reservation.getGuestName()
                            + " -> " + roomId
            );

        } else {

            System.out.println(
                    "❌ Booking failed for "
                            + reservation.getGuestName()
            );
        }
    }
}


// 5. Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "hotel_state.dat";

    public static void saveState(
            InventoryService inventory,
            BookingHistory history
    ) {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream(FILE_NAME));

            out.writeObject(inventory);
            out.writeObject(history);

            out.close();

            System.out.println("\n💾 System state saved to file.");

        } catch(Exception e) {

            System.out.println(
                    "⚠ Error saving state: " + e.getMessage()
            );
        }
    }


    public static Object[] loadState() {

        try {

            ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream(FILE_NAME));

            InventoryService inventory =
                    (InventoryService) in.readObject();

            BookingHistory history =
                    (BookingHistory) in.readObject();

            in.close();

            System.out.println(
                    "📂 System state restored from file."
            );

            return new Object[]{inventory, history};

        } catch(Exception e) {

            System.out.println(
                    "⚠ No previous state found. Starting fresh."
            );

            return new Object[]{
                    new InventoryService(),
                    new BookingHistory()
            };
        }
    }
}


// 6. Main Application
class UseCase12PersistenceRecovery {

    public static void main(String[] args) {

        // Restore system state
        Object[] state = PersistenceService.loadState();

        InventoryService inventory =
                (InventoryService) state[0];

        BookingHistory history =
                (BookingHistory) state[1];

        BookingService bookingService =
                new BookingService(inventory, history);


        // Simulate bookings
        bookingService.bookRoom(new Reservation("Abhi", "Suite"));
        bookingService.bookRoom(new Reservation("Subha", "Suite"));
        bookingService.bookRoom(new Reservation("Vanmathi", "Suite"));


        // Show current system state
        history.printHistory();
        inventory.printInventory();


        // Save system state before shutdown
        PersistenceService.saveState(inventory, history);
    }
}