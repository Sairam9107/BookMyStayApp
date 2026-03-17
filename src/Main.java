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
}

// 2. Booking Request Queue
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
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

        Set<String> rooms = allocatedRooms.get(roomType);

        String roomId = roomType.substring(0,1).toUpperCase() + (100 + rooms.size() + 1);

        rooms.add(roomId);
        availableCounts.put(roomType, availableCounts.get(roomType) - 1);

        return roomId;
    }
}

// 4. Booking Service
class BookingService {
    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processQueue(BookingRequestQueue queue) {
        while (queue.hasPendingRequests()) {
            Reservation request = queue.getNextRequest();

            if (inventory.isAvailable(request.getRoomType())) {
                String roomId = inventory.allocateRoom(request.getRoomType());
                request.setAssignedRoomId(roomId);

                System.out.println("✅ Confirmed: " + request.getGuestName() +
                        " assigned to " + roomId);
            } else {
                System.out.println("❌ Failed: No availability for "
                        + request.getRoomType()
                        + " (Guest: " + request.getGuestName() + ")");
            }
        }
    }
}

// 5. Main Class
 class HotelBookingApp {
    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        InventoryService inventory = new InventoryService();
        BookingService service = new BookingService(inventory);

        bookingQueue.addRequest(new Reservation("Abhi", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Suite"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite")); // exceeds capacity

        service.processQueue(bookingQueue);
    }
}