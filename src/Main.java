import java.util.*;

// 1. Reservation Class
class Reservation {
    private String guestName;
    private String roomType;
    private String assignedRoomId;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void setAssignedRoomId(String id) {
        this.assignedRoomId = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getAssignedRoomId() {
        return assignedRoomId;
    }

    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + assignedRoomId;
    }
}


// 2. Booking Request Queue
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}


// 3. Inventory Service
class InventoryService {

    private Map<String, Integer> availableCounts = new HashMap<>();
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public InventoryService() {

        availableCounts.put("Single", 10);
        availableCounts.put("Double", 5);
        availableCounts.put("Suite", 2);

        allocatedRooms.put("Single", new HashSet<>());
        allocatedRooms.put("Double", new HashSet<>());
        allocatedRooms.put("Suite", new HashSet<>());
    }

    public boolean isAvailable(String roomType) {
        return availableCounts.getOrDefault(roomType, 0) > 0;
    }

    public String allocateRoom(String roomType) {

        if (!isAvailable(roomType)) return null;

        String roomId =
                roomType.substring(0,1).toUpperCase() +
                        (100 + allocatedRooms.get(roomType).size() + 1);

        allocatedRooms.get(roomType).add(roomId);

        availableCounts.put(
                roomType,
                availableCounts.get(roomType) - 1
        );

        return roomId;
    }
}


// 4. Booking History (Stores Confirmed Reservations)
class BookingHistory {

    private List<Reservation> confirmedBookings = new ArrayList<>();

    public void addConfirmedReservation(Reservation r) {
        confirmedBookings.add(r);
    }

    public List<Reservation> getAllBookings() {
        return confirmedBookings;
    }
}


// 5. Booking Report Service
class BookingReportService {

    public void printAllBookings(List<Reservation> bookings) {

        System.out.println("\n----- BOOKING HISTORY REPORT -----");

        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }

    public void generateSummary(List<Reservation> bookings) {

        Map<String, Integer> roomSummary = new HashMap<>();

        for (Reservation r : bookings) {

            String type = r.getRoomType();

            roomSummary.put(
                    type,
                    roomSummary.getOrDefault(type, 0) + 1
            );
        }

        System.out.println("\n----- BOOKING SUMMARY REPORT -----");

        for (String type : roomSummary.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomSummary.get(type));
        }

        System.out.println("Total Confirmed Bookings: " + bookings.size());
    }
}


// 6. Booking Service (Processes Queue)
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

    public void processQueue(BookingRequestQueue queue) {

        while (queue.hasPendingRequests()) {

            Reservation request = queue.getNextRequest();

            if (inventory.isAvailable(request.getRoomType())) {

                String roomId =
                        inventory.allocateRoom(request.getRoomType());

                request.setAssignedRoomId(roomId);

                System.out.println(
                        "✅ Confirmed: "
                                + request.getGuestName()
                                + " assigned to "
                                + roomId
                );

                // Add to booking history
                history.addConfirmedReservation(request);

            } else {

                System.out.println(
                        "❌ Failed: No availability for "
                                + request.getRoomType()
                                + " (Guest: "
                                + request.getGuestName()
                                + ")"
                );
            }
        }
    }
}



// 7. Main Application
class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        InventoryService inventory = new InventoryService();

        BookingHistory history = new BookingHistory();

        BookingService service =
                new BookingService(inventory, history);

        BookingReportService reportService =
                new BookingReportService();


        // Add Booking Requests
        bookingQueue.addRequest(new Reservation("Abhi", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Suite"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));


        // Process Bookings
        service.processQueue(bookingQueue);


        // Admin Requests Reports
        reportService.printAllBookings(
                history.getAllBookings()
        );

        reportService.generateSummary(
                history.getAllBookings()
        );
    }
}