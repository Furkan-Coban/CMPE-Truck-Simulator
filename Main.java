import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Main <input_file> <output_file>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];
        FleetManager fleetManager = new FleetManager();
        StringBuilder outputBuffer = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while (((line = reader.readLine()) != null)){
                String[] parts = line.split(" ");
                String command = parts[0];
                switch (command) {

                    case "create_parking_lot":
                        int capacityConstraint = Integer.parseInt(parts[1]);
                        int truckLimit = Integer.parseInt(parts[2]);
                        fleetManager.createParkingLot(capacityConstraint, truckLimit);
                        break;

                    case "delete_parking_lot":
                        capacityConstraint = Integer.parseInt(parts[1]);
                        fleetManager.deleteParkingLot(capacityConstraint);
                        break;

                    case "add_truck":
                        int truckId = Integer.parseInt(parts[1]);
                        int capacity = Integer.parseInt(parts[2]);
                        String addTruckResult = fleetManager.addTruck(truckId, capacity);
                        outputBuffer.append(addTruckResult).append("\n");
                        break;

                    case "ready":
                        capacityConstraint = Integer.parseInt(parts[1]);
                        String readyResult = fleetManager.ready(capacityConstraint);
                        outputBuffer.append(readyResult).append("\n");
                        break;

                    case "load":
                        capacityConstraint = Integer.parseInt(parts[1]);
                        int loadAmount = Integer.parseInt(parts[2]);
                        String loadResult = fleetManager.load(capacityConstraint, loadAmount);
                        outputBuffer.append(loadResult).append("\n");
                        break;

                    case "count":
                        capacityConstraint = Integer.parseInt(parts[1]);
                        String countResult = fleetManager.countTrucksAboveCapacity(capacityConstraint);
                        outputBuffer.append(countResult).append("\n");
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(outputBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
