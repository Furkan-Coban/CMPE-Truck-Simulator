
public class ParkingLot implements Comparable<ParkingLot>{

    private int capacityConstraint;
    private int truckLimit;
    private MyLinkedList<Truck> waitingSection;
    private MyLinkedList<Truck> readySection;


    public ParkingLot(int capacityConstraint, int truckLimit){
        this.capacityConstraint = capacityConstraint;
        this.truckLimit = truckLimit;
        waitingSection = new MyLinkedList<>();
        readySection = new MyLinkedList<>();
    }

    public int getCapacityConstraint() {
        return capacityConstraint;
    }


    public boolean isEmpty(){
        return getCurrentTruckNumber()==0;
    }

    public int getTruckLimit() {
        return truckLimit;
    }

    public int getCurrentTruckNumber(){
        return waitingSection.size() + readySection.size();
    }


    public boolean isFull(){
        return (getTruckLimit()-getCurrentTruckNumber())==0;
    }


    public boolean addWaitingTruck(Truck truck){
        if(getCurrentTruckNumber()<truckLimit){
            waitingSection.add(truck);
            truck.setRemainingCapacity(this.capacityConstraint); //Its remaining capacity is set to capacityConstraint.
            return true;
        }
        else{
            return false;
        }
    }

    public Truck moveReadyTruck(){
        if(!waitingSection.isEmpty()){
            Truck truck = waitingSection.poll();
            readySection.add(truck);
            return truck;
        }
        else{
            return null;
        }
    }
    @Override
    public String toString() {
        return "ParkingLot Capacity: " + capacityConstraint + ", Truck Limit: " + truckLimit +
                ", Waiting Trucks: " + waitingSection.size() + ", Ready Trucks: " + readySection.size();
    }

    public MyLinkedList<Truck> getWaitingSection() {
        return waitingSection;
    }

    public MyLinkedList<Truck> getReadySection() {
        return readySection;
    }

    @Override
    public int compareTo(ParkingLot o) {  //Compares this parking lot to another parking lot based on their capacity constraints.
        return Integer.compare(this.getCapacityConstraint(), o.getCapacityConstraint());

    }
}
