import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class HostelRoom {
    private int roomNo;
    private String studentName;
    private double previousReading;
    private double currentReading;

    HostelRoom(int roomNo, String studentName,
               double previousReading, double currentReading) {
        this.roomNo = roomNo;
        this.studentName = studentName;
        this.previousReading = previousReading;
        this.currentReading = currentReading;
    }

    int getRoomNo() {
        return roomNo;
    }

    double getCurrentReading() {
        return currentReading;
    }

    void updateReading(double newReading) {
        previousReading = currentReading;
        currentReading = newReading;
    }

    double getUnits() {
        return currentReading - previousReading;
    }

    double getBill() {
        return getUnits() * 5.0;
    }

    void display() {
        System.out.println("----------------------------------");
        System.out.println("Room Number      : " + roomNo);
        System.out.println("Student Name     : " + studentName);
        System.out.println("Previous Reading : " + previousReading);
        System.out.println("Current Reading  : " + currentReading);
        System.out.println("Units Consumed   : " + getUnits());
        System.out.printf("Electricity Bill : Rs. %.2f%n", getBill());
    }

    // Save one room record to a text file
    String toFileRecord() {
        String encodedName = Base64.getEncoder().encodeToString(
            studentName.getBytes(StandardCharsets.UTF_8)
        );

        return roomNo + "|" + encodedName + "|"
            + previousReading + "|" + currentReading;
    }

    // Create a room record from a saved line
    static HostelRoom fromFileRecord(String line) {
        String[] data = line.split("\\|", -1);

        if (data.length != 4) {
            throw new IllegalArgumentException("Invalid record format");
        }

        int roomNo = Integer.parseInt(data[0]);
        String name = new String(
            Base64.getDecoder().decode(data[1]),
            StandardCharsets.UTF_8
        );
        double previous = Double.parseDouble(data[2]);
        double current = Double.parseDouble(data[3]);

        if (roomNo <= 0 || name.trim().isEmpty()
                || !Double.isFinite(previous)
                || !Double.isFinite(current)
                || previous < 0 || current < previous) {
            throw new IllegalArgumentException("Invalid record values");
        }

        return new HostelRoom(roomNo, name, previous, current);
    }
}

public class HostelElectricityMeter {

    private static final double RATE_PER_UNIT = 5.0;
    private static final Path FILE_PATH =
        Paths.get("meter_readings.txt");

    private static final ArrayList<HostelRoom> rooms =
        new ArrayList<>();

    private static final Scanner sc = new Scanner(System.in);

    // Read an integer safely
    static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    // Read a valid decimal number
    static double readDouble(String message) {
        while (true) {
            System.out.print(message);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());

                if (!Double.isFinite(value)) {
                    System.out.println("Enter a finite number.");
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Find a room by room number
    static HostelRoom findRoom(int roomNo) {
        for (HostelRoom room : rooms) {
            if (room.getRoomNo() == roomNo) {
                return room;
            }
        }
        return null;
    }

    // Load saved readings when the program starts
    static void loadFromFile() {
        if (!Files.exists(FILE_PATH)) {
            System.out.println(
                "No previous data file found. Starting a new register."
            );
            return;
        }

        int loaded = 0;
        int skipped = 0;

        try (BufferedReader br = Files.newBufferedReader(
                FILE_PATH, StandardCharsets.UTF_8)) {

            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    HostelRoom room = HostelRoom.fromFileRecord(line);

                    if (findRoom(room.getRoomNo()) != null) {
                        throw new IllegalArgumentException(
                            "Duplicate room number in file"
                        );
                    }

                    rooms.add(room);
                    loaded++;

                } catch (IllegalArgumentException e) {
                    skipped++;
                    System.out.println(
                        "Warning: Invalid record skipped."
                    );
                }
            }

            System.out.println("Records loaded: " + loaded);

            if (skipped > 0) {
                System.out.println(
                    "Warning: " + skipped
                    + " corrupted record(s) were skipped."
                );
            }

        } catch (IOException e) {
            System.out.println(
                "Error reading the file: " + e.getMessage()
            );
        }
    }

    // Save all current readings to the file
    static void saveToFile() {
        try (BufferedWriter bw = Files.newBufferedWriter(
                FILE_PATH, StandardCharsets.UTF_8)) {

            for (HostelRoom room : rooms) {
                bw.write(room.toFileRecord());
                bw.newLine();
            }

            System.out.println("Readings saved successfully.");

        } catch (IOException e) {
            System.out.println(
                "Error saving file: " + e.getMessage()
            );
        }
    }

    // Add a new room or update an existing room's monthly reading
    static void addReading() {
        int roomNo = readInt("Enter Room Number: ");

        if (roomNo <= 0) {
            System.out.println("Room number must be positive.");
            return;
        }

        HostelRoom existing = findRoom(roomNo);

        if (existing != null) {
            System.out.println("Room already registered.");
            System.out.println(
                "Previous reading: " + existing.getCurrentReading()
            );

            double current = readDouble("Enter Current Reading: ");

            if (current < existing.getCurrentReading()) {
                System.out.println(
                    "Invalid reading! Current reading cannot be "
                    + "lower than the previous month's reading."
                );
                return;
            }

            existing.updateReading(current);
            existing.display();
            saveToFile();
            return;
        }

        System.out.print("Enter Student Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return;
        }

        double previous = readDouble("Enter Previous Reading: ");
        double current = readDouble("Enter Current Reading: ");

        if (previous < 0 || current < previous) {
            System.out.println("Invalid meter readings!");
            return;
        }

        HostelRoom room = new HostelRoom(
            roomNo, name, previous, current
        );

        rooms.add(room);
        room.display();
        saveToFile();
    }

    // Display every room record
    static void displayAllRecords() {
        if (rooms.isEmpty()) {
            System.out.println("No records available.");
            return;
        }

        for (HostelRoom room : rooms) {
            room.display();
        }
    }

    // Search for one room
    static void searchRoom() {
        int roomNo = readInt("Enter Room Number to Search: ");

        HostelRoom room = findRoom(roomNo);

        if (room != null) {
            room.display();
        } else {
            System.out.println("Room not found!");
        }
    }

    public static void main(String[] args) {
        loadFromFile();

        int choice;

        do {
            System.out.println(
                "\n=== HOSTEL ELECTRICITY METER REGISTER ==="
            );
            System.out.println("1. Add / Update Monthly Reading");
            System.out.println("2. Display All Records");
            System.out.println("3. Search Room");
            System.out.println("4. Exit");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addReading();
                    break;

                case 2:
                    displayAllRecords();
                    break;

                case 3:
                    searchRoom();
                    break;

                case 4:
                    saveToFile();
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);

        sc.close();
    }
}