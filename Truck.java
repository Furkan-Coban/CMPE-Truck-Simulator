
public class Truck {
    private int id;

    private int maxCapacity;

    private int currentLoad;

    private int remainingCapacity;

    public Truck(int id, int maxCapacity){
        this.id = id;
        this.maxCapacity = maxCapacity;
        currentLoad = 0;
        remainingCapacity = maxCapacity;
    }

    public int getId() {
        return id;
    }


    public int getRemainingCapacity(){
        return remainingCapacity;
    }
    public void setRemainingCapacity(int remainingCapacity){
        this.remainingCapacity = remainingCapacity;
    }


    public void addLoad(int load){
        currentLoad += load;
        if(this.isFull()){
            currentLoad = 0;
            setRemainingCapacity(maxCapacity); // If the truck is full, it will be unloaded and its remainingCapacity is again maxCapacity.
        }
        else{
            setRemainingCapacity(maxCapacity-currentLoad); // If the truck is not full, its remainingCapacity is updated to the new value.
        }
    }

    public boolean isFull() {
        return maxCapacity == currentLoad;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }


    @Override
    public String toString() {
        return "Truck ID: " + id + ", Max Capacity: " + maxCapacity + ", Current Load: " + currentLoad;
    }

}
