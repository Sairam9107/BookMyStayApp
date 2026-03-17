import java.util.*;

// 1. Reservation Class
class Reservation {

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
}


// 2. Shared Booking Request Queue
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.add(r);
    }

    public synchronized Reservation getNextRequest() {

        if(queue.isEmpty())
            return null;

        return queue.poll();
    }
}


// 3. Inventory Service (Thread Safe)
class InventoryService {

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

    // Critical Section (Thread-safe allocation)
    public synchronized String allocateRoom(String roomType) {

        int available = availableCounts.getOrDefault(roomType, 0);

        if (available <= 0) {
            return null;
        }

        String roomId =
                roomType.substring(0,1).toUpperCase()
                        + (100 + allocatedRooms.get(roomType).size() + 1);

        allocatedRooms.get(roomType).add(roomId);

        availableCounts.put(roomType, available - 1);

        return roomId;
    }
}


// 4. Concurrent Booking Processor (Thread)
class ConcurrentBookingProcessor extends Thread {

    private BookingRequestQueue queue;
    private InventoryService inventory;

    public ConcurrentBookingProcessor(
            String name,
            BookingRequestQueue queue,
            InventoryService inventory
    ) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while(true) {

            Reservation request = queue.getNextRequest();

            if(request == null)
                break;

            String roomId =
                    inventory.allocateRoom(request.getRoomType());

            if(roomId != null) {

                request.setAssignedRoomId(roomId);

                System.out.println(
                        "✅ " + getName() +
                                " confirmed booking for " +
                                request.getGuestName() +
                                " -> Room " + roomId
                );

            } else {

                System.out.println(
                        "❌ " + getName() +
                                " failed booking for " +
                                request.getGuestName() +
                                " (No " + request.getRoomType() + " available)"
                );
            }
        }
    }
}


// 5. Main Application
 class UseCase11ConcurrentBooking {

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        InventoryService inventory = new InventoryService();


        // Simulate multiple booking requests
        bookingQueue.addRequest(new Reservation("Abhi", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Suite"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));

        bookingQueue.addRequest(new Reservation("Rahul", "Double"));
        bookingQueue.addRequest(new Reservation("Arjun", "Double"));
        bookingQueue.addRequest(new Reservation("Karthik", "Double"));
        bookingQueue.addRequest(new Reservation("Meena", "Double"));

        bookingQueue.addRequest(new Reservation("Priya", "Single"));
        bookingQueue.addRequest(new Reservation("Divya", "Single"));
        bookingQueue.addRequest(new Reservation("Ravi", "Single"));


        // Create multiple worker threads
        ConcurrentBookingProcessor t1 =
                new ConcurrentBookingProcessor("Processor-1", bookingQueue, inventory);

        ConcurrentBookingProcessor t2 =
                new ConcurrentBookingProcessor("Processor-2", bookingQueue, inventory);

        ConcurrentBookingProcessor t3 =
                new ConcurrentBookingProcessor("Processor-3", bookingQueue, inventory);


        // Start concurrent processing
        t1.start();
        t2.start();
        t3.start();
    }
}