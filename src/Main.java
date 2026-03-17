import java.util.*;

// 1. Reservation class
class Reservation {
    private String guestName;
    private String roomType;
    private String assignedRoomId;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void setAssignedRoomId(String id) { this.assignedRoomId = id; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getAssignedRoomId() { return assignedRoomId; }
}

// Queue to hold booking requests
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

// 2. Inventory Service
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

        String roomId = roomType.substring(0,1).toUpperCase()
                + (100 + allocatedRooms.get(roomType).size() + 1);

        allocatedRooms.get(roomType).add(roomId);
        availableCounts.put(roomType, availableCounts.get(roomType) - 1);

        return roomId;
    }
}

// 3. Booking Service
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

                System.out.println("✅ Confirmed: "
                        + request.getGuestName()
                        + " assigned to " + roomId);

            } else {
                System.out.println("❌ Failed: No availability for "
                        + request.getRoomType()
                        + " (Guest: " + request.getGuestName() + ")");
            }
        }
    }
}

// 4. Add-On Service class
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// 5. Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    // Method to add a service to a reservation
    public void addService(String reservationId, AddOnService service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service Added: "
                + service.getServiceName()
                + " for Reservation " + reservationId);
    }

    // Method to calculate additional cost
    public double calculateTotalCost(String reservationId) {

        double total = 0;

        List<AddOnService> services =
                reservationServices.getOrDefault(reservationId, new ArrayList<>());

        for (AddOnService s : services) {
            total += s.getCost();
        }

        return total;
    }

    // Method to display services
    public void showServices(String reservationId) {

        List<AddOnService> services =
                reservationServices.getOrDefault(reservationId, new ArrayList<>());

        System.out.println("\nServices for Reservation " + reservationId);

        for (AddOnService s : services) {
            System.out.println("- " + s.getServiceName()
                    + " ($" + s.getCost() + ")");
        }
    }
}

// Main Application
 class HotelBookingApp {

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        InventoryService inventory = new InventoryService();
        BookingService service = new BookingService(inventory);

        // Create reservations
        Reservation r1 = new Reservation("Abhi", "Suite");
        Reservation r2 = new Reservation("Subha", "Suite");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Process booking queue
        service.processQueue(bookingQueue);

        // Add-On Service Manager
        AddOnServiceManager addOnManager = new AddOnServiceManager();

        // Guest selects services (method calls)
        if (r1.getAssignedRoomId() != null) {

            addOnManager.addService(r1.getAssignedRoomId(),
                    new AddOnService("Breakfast", 20));

            addOnManager.addService(r1.getAssignedRoomId(),
                    new AddOnService("Spa", 50));

            addOnManager.addService(r1.getAssignedRoomId(),
                    new AddOnService("Airport Pickup", 30));

            // Show services
            addOnManager.showServices(r1.getAssignedRoomId());

            // Calculate total add-on cost
            double extraCost =
                    addOnManager.calculateTotalCost(r1.getAssignedRoomId());

            System.out.println("Total Add-On Cost: $" + extraCost);
        }
    }
}