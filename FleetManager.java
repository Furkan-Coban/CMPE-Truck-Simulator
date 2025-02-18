
public class FleetManager {
    private MyHashMap<Integer, ParkingLot> parkingLotsByCapacity;  // Lookup table for parking lots by capacity constraint

    // 4 different AVL trees are created to handle specific operations.
    private MyAVLTree notFullParkingLots;

    private MyAVLTree parkingLotWithWaiting;

    private MyAVLTree parkingLotWithReady;

    private MyAVLTree notEmptyParkingLots;

    public FleetManager() {
        parkingLotsByCapacity = new MyHashMap<>();
        notFullParkingLots = new MyAVLTree();
        parkingLotWithWaiting = new MyAVLTree();
        parkingLotWithReady  = new MyAVLTree();
        notEmptyParkingLots = new MyAVLTree();
    }

    public void createParkingLot(int capacityConstraint, int truckLimit) {
        if (!parkingLotsByCapacity.containsKey(capacityConstraint)) {
            ParkingLot newLot = new ParkingLot(capacityConstraint, truckLimit);
            parkingLotsByCapacity.put(capacityConstraint, newLot); // Add to HashMap of parkingLots.
            notFullParkingLots.insert(newLot); // Add to AVL tree of not-full parking lots
        }
    }

    public void deleteParkingLot(int capacityConstraint) {
        ParkingLot lot = parkingLotsByCapacity.remove(capacityConstraint);
        if (lot != null) {
            // Remove the lot from relevant AVL trees based on its status
            if(!lot.getWaitingSection().isEmpty()){
                parkingLotWithWaiting.delete(lot);
            }
            if(!lot.isEmpty()){
                notEmptyParkingLots.delete(lot);
            }
            if(!lot.getReadySection().isEmpty()){
                parkingLotWithReady.delete(lot);
            }
            if(!lot.isFull()){
                notFullParkingLots.delete(lot);
            }
        }


    }

    public String addTruck(int truckId, int truckCapacity) {
        ParkingLot target = parkingLotsByCapacity.get(truckCapacity); // finding a parking lot with exact capacity constraint
        if (target == null || target.isFull()) {
            //If there is no such parkingLot, or it is full, finds a lot with the largest capacity below the specified capacity
            // Since it directly looks for notFullParkingLots, if it finds a parkingLot, it is suitable.
            target = notFullParkingLots.getLargestBelow(new ParkingLot(truckCapacity,0));
        }

        if (target == null) {
            return "-1"; // No suitable parking lot found
        } else {
            Truck truck = new Truck(truckId,truckCapacity);
            target.addWaitingTruck(truck); // Add truck to the waiting section of the lot

            // Update AVL trees as needed
            if(target.getWaitingSection().size()==1){
                parkingLotWithWaiting.insert(target);
            }
            if(target.getCurrentTruckNumber()==1){
                notEmptyParkingLots.insert(target);
            }
            else {
                notEmptyParkingLots.updateSubtreeCount(target,1);
            }
            if(target.isFull()){
                notFullParkingLots.delete(target);
            }
            return Integer.toString(target.getCapacityConstraint());
        }
    }

    public String ready(int capacityConstraint) {


        ParkingLot lot = parkingLotsByCapacity.get(capacityConstraint);

        Truck readyTruck = null;

        if (lot != null) { //If there is an exact match
            readyTruck = lot.moveReadyTruck();  // Move truck to the ready section
            if(readyTruck!=null&&lot.getWaitingSection().isEmpty()){
                parkingLotWithWaiting.delete(lot); //parkingLots with empty waiting section are deleted.
            }
            if(lot.getReadySection().size()==1){
                parkingLotWithReady.insert(lot); //new parkingLots with ready trucks are added.
            }
        }
        if (readyTruck == null) {
            // If no truck was moved, find the smallest parking lot above the specified capacity
            lot = parkingLotWithWaiting.getSmallestAbove(new ParkingLot(capacityConstraint,0));
            if(lot!=null) {

                //If there exists a suitable lot, first truck in this parking lot moves to ready section.
                readyTruck = lot.moveReadyTruck();

                //Relevant AVL trees are updated.
                if (lot.getWaitingSection().isEmpty()){
                    parkingLotWithWaiting.delete(lot);
                }
                if(lot.getReadySection().size()==1){
                    parkingLotWithReady.insert(lot);
                }
            }
        }
        if (readyTruck == null) {
            return "-1";
        } else {

            return readyTruck.getId() + " " + lot.getCapacityConstraint();
        }
    }

    //Finds a parking lot with the largest capacity that is not full and below a given constraint.
    private ParkingLot findSuitableParkingLot(int capacityConstraint) {


        // Try to find the exact match for the capacity
        ParkingLot targetLot = parkingLotsByCapacity.get(capacityConstraint);
        // If no exact match, find the largest parking lot below the given capacity
        if (targetLot == null|| targetLot.isFull()) {
            targetLot = notFullParkingLots.getLargestBelow(new ParkingLot(capacityConstraint, 0));
        }

        return targetLot;
    }

    public String load(int capacityConstraint, int loadAmount) {
        StringBuilder result = new StringBuilder();
        // Suitable lot is searched.
        ParkingLot currentLot = parkingLotsByCapacity.get(capacityConstraint);
        if((currentLot == null) || (currentLot.getReadySection().isEmpty())) {
            currentLot = parkingLotWithReady.getSmallestAbove(new ParkingLot(capacityConstraint,0));
            if(currentLot==null){return "-1";}
        }
        // Iterate through lots to load all the amount.
        while (currentLot != null && loadAmount > 0) {

            MyLinkedList<Truck> readySection = currentLot.getReadySection();
            while (!readySection.isEmpty() && loadAmount > 0) {
                Truck truck = readySection.poll(); // Get the next truck in the ready section

                // Relevant AVL trees are updated.
                if(!currentLot.isEmpty()){
                    notEmptyParkingLots.updateSubtreeCount(currentLot,-1);
                }
                if(currentLot.getReadySection().isEmpty()){
                    if(currentLot.getCurrentTruckNumber()==0){
                        notEmptyParkingLots.delete(currentLot);
                    }

                    parkingLotWithReady.delete(currentLot);
                }
                // Load the truck based on its remaining capacity
                int remainingCapacity = truck.getRemainingCapacity();
                if (loadAmount >= remainingCapacity) {
                    truck.addLoad(remainingCapacity);
                    loadAmount = loadAmount - remainingCapacity;

                } else {
                    truck.addLoad(loadAmount);
                    loadAmount = 0;
                }

                if(!currentLot.isFull()){
                    notFullParkingLots.insert(currentLot);
                }
                // Find a suitable lot for the truck to relocate.
                int targetCapacity = truck.getRemainingCapacity();
                ParkingLot targetLot = findSuitableParkingLot(targetCapacity);

                if (targetLot != null) {
                    //AVL trees and SubtreeCounts are updated.
                    if(!targetLot.isEmpty()){
                        notEmptyParkingLots.updateSubtreeCount(targetLot,1);
                    }
                    targetLot.addWaitingTruck(truck); //Truck is added to the targetLot's waiting section.
                    if(targetLot.getWaitingSection().size()==1){
                        parkingLotWithWaiting.insert(targetLot);
                    }
                    if(targetLot.getCurrentTruckNumber()==1){
                        notEmptyParkingLots.insert(targetLot);
                    }
                    if(targetLot.isFull()){
                        notFullParkingLots.delete(targetLot);
                    }

                    result.append(truck.getId()).append(" ").append(targetLot.getCapacityConstraint());
                }
                else{
                    result.append(truck.getId()).append(" -1");
                }
                if(!readySection.isEmpty() && loadAmount > 0 && !result.isEmpty()){
                    result.append(" - ");
                }

            }
            if(currentLot.getReadySection().isEmpty()){
                parkingLotWithReady.delete(currentLot);
            }

            if (loadAmount > 0) {
                //If ready trucks of currentLoad is finished but there is a remainingLoad, it continues with smallestAbove lot
                currentLot = parkingLotWithReady.getSmallestAbove(currentLot);
                if(!result.isEmpty()&&currentLot!=null){
                    result.append(" - ");
                }
            }
            else{break;}

        }

        if (!result.isEmpty()) {
            return result.toString();
        }
        else {return "-1";}

    }

    //Counts the total number of trucks in parking lots with a capacity constraint above the given value.
    public String countTrucksAboveCapacity(int capacity){
        return Integer.toString(notEmptyParkingLots.getAllLarger(new ParkingLot(capacity,0)));

    }

}


