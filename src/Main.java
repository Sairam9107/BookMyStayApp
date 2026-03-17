abstract class Room{
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    Room(int numberOfBeds,int squareFeet,double pricePerNight){
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }
    public void displayRoomDetails(){
        System.out.println("Beds: " +numberOfBeds);
        System.out.println("Size: " +squareFeet+ " sqft");
        System.out.println("Price per night: " +pricePerNight);
    }
}
class SingleRoom extends Room{
    SingleRoom(){
        super(1,250,1500.0);
    }
    public void displaySingleRoomDetails(){
        System.out.println("Single Room: ");
        super.displayRoomDetails();
        System.out.println("Available: 5");
    }
}
class DoubleRoom extends Room{
    DoubleRoom(){
        super(2,400,2500.0);
    }
    public void displayDoubleRoomDetails(){
        System.out.println("Double Room: ");
        super.displayRoomDetails();
        System.out.println("Available: 3");
    }
}
class SuiteRoom extends Room{
    SuiteRoom(){
        super(3,750,5000.0);
    }
    public void displaySuiteRoomDetails(){
        System.out.println("Suite Room: ");
        super.displayRoomDetails();
        System.out.println("Available: 2");
    }
}
 class HotelBookingApp {
    public static void main(String[] args){
        System.out.println("Hotel Room Initialization");
        SingleRoom s1 = new SingleRoom();
        s1.displaySingleRoomDetails();
        System.out.println();
        DoubleRoom d1 = new DoubleRoom();
        d1.displayDoubleRoomDetails();
        System.out.println();
        SuiteRoom s2 = new SuiteRoom();
        s2.displaySuiteRoomDetails();
    }
}