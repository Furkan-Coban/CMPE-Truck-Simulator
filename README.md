# CMPE Truck Simulator 🚛

## 📌 Project Overview
The **CMPE Truck Simulator** is a fleet management simulation project for the **CmpE 250: Data Structures and Algorithms** course (Fall 2024). It handles truck assignments, parking lot organization, load distribution, and truck movements.

## ⚙️ Features
- **Fleet Management:** Assign trucks to parking lots based on capacity constraints.
- **Load Handling:** Distribute incoming loads efficiently.
- **Truck Movement:** Trucks relocate dynamically as per the load status.
- **Command-Based Execution:** Reads operations from an input file.

### Available Commands
| Command | Description |
|---------|------------|
| `create_parking_lot <capacity> <truck_limit>` | Creates a parking lot. |
| `add_truck <truck_id> <capacity>` | Adds a truck to the fleet. |
| `ready <capacity>` | Moves a truck from waiting to ready. |
| `load <capacity> <load_amount>` | Loads the earliest available trucks. |
| `delete_parking_lot <capacity>` | Removes a parking lot and its trucks. |
| `count <capacity>` | Counts trucks in lots larger than the given capacity. |

## 🚀 How to Run
1. **Compile the code:**
   ```sh
   javac *.java
2. Run the program with input/output files:
   ```sh
   java Main <input_file> <output_file>

🛠️ Technologies Used
  - Java
  - File I/O Handling
  - Data Structures (Lists, Trees, etc.)
  - Algorithm Optimization

📌 Author
  - Name: Furkan Çoban
  - Course: CmpE 250 - Boğaziçi University
  - Year: Fall 2024



    



