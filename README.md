Hostel Electricity Meter Register

Project Description

The Hostel Electricity Meter Register is a Java-based mini project used to maintain electricity meter readings for hostel rooms. It calculates electricity consumption and bills based on the meter readings and stores the records in a file.

Objectives

- Record electricity meter readings for hostel rooms.
- Calculate electricity consumption in units.
- Calculate electricity bills automatically.
- Search for a room's electricity record.
- Store and retrieve records using file handling.

Features

- Add new hostel room readings.
- Update monthly readings for existing rooms.
- Display all electricity records.
- Search records by room number.
- Calculate electricity bills at ₹5 per unit.
- Validate user inputs.
- Handle missing files and invalid records.
- Save records permanently in a text file.

Technologies Used

- Programming Language: Java
- Concepts: Object-Oriented Programming (OOP)
- Data Structure: ArrayList
- File Handling: Text file storage
- Exception Handling: Input and file error handling

Software Requirements

- Java JDK
- Visual Studio Code or any Java IDE
- Terminal or Command Prompt

How to Run

Step 1: Save the source code as:

"HostelElectricityMeter.java"

Step 2: Open the terminal in the project folder.

Step 3: Compile the program:

javac HostelElectricityMeter.java

Step 4: Run the program:

java HostelElectricityMeter

Sample Calculation

Description| Value
Room Number| 101
Student Name| Kumar
Previous Reading| 1250
Current Reading| 1325
Units Consumed| 75
Rate per Unit| ₹5
Total Bill| ₹375

Formula:

Units Consumed = Current Reading − Previous Reading

Electricity Bill = Units Consumed × Rate per Unit

Data Storage

The program saves electricity records in "meter_readings.txt". Saved records can be loaded when the program runs again.

Project Structure

hostel-electricity-meter-register/
├── HostelElectricityMeter.java
├── meter_readings.txt
└── README.md

"meter_readings.txt" is created automatically when the program saves its first record.

Applications

- Hostel electricity record maintenance
- Room-wise electricity consumption tracking
- Monthly electricity bill calculation
- Basic electricity management systems

Conclusion

This project demonstrates Java programming, object-oriented concepts, collections, file handling, input validation, and exception handling through a practical hostel electricity management application.
