import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

class Passengers {
    String name;
    String mobileNumber;
    String email;
    String city;
    int age;

    public Passengers(String name, String mobileNumber, String email, String city, int age) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.city = city;
        this.age = age;
    }

    public int generateCustomerId() {
        String customerIdString = mobileNumber.substring(Math.max(0, mobileNumber.length() - 7));
        return Integer.parseInt(customerIdString);
    }
}

class Bus {
    String busNumber;
    int totalSeats;
    String startPoint;
    String endPoint;
    String startTime;
    double fare;

    public Bus(String busNumber, int totalSeats, String startPoint, String endPoint, String startTime, double fare) {
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startTime = startTime;
        this.fare = fare;
    }

    public String generateBusId(String busNumber, String startPoint, String endPoint) {
        String busId = "";

        if (busNumber.length() >= 2) {
            busId += busNumber.substring(0, 2).toUpperCase();
        } else {
            busId += busNumber.toUpperCase();
        }

        if (startPoint.length() >= 2) {
            busId += startPoint.substring(0, 2).toUpperCase();
        } else {
            busId += startPoint.toUpperCase();
        }

        if (endPoint.length() >= 2) {
            busId += endPoint.substring(0, 2).toUpperCase();
        } else {
            busId += endPoint.toUpperCase();
        }

        return busId;
    }

}

class ReservationSystem {
    LinkedList<Passengers> customers;
    LinkedList<Bus> buses;
    Queue<Passengers> waitingQueue;
    int nextSeatNumber;

    public ReservationSystem() {
        customers = new LinkedList<>();
        buses = new LinkedList<>();
        waitingQueue = new LinkedList<>();
        nextSeatNumber = 1;

    }

    public void registerCustomer(String name, String mobileNumber, String email, String city, int age) {
        if (validateName(name) && validateMobileNumber(mobileNumber) && validateEmail(email)
                && validateCity(city) && validateAge(age)) {
            Passengers customer = new Passengers(name, mobileNumber, email, city, age);
            customers.add(customer);
            writeCustomerToFile(customer);
            System.out.println("Customer registered successfully!");
        } else {
            System.out.println("Invalid input. Customer not registered.");
        }
    }

    private boolean validateName(String name) {
        return name.matches("[a-zA-Z\\s]+");
    }

    private boolean validateMobileNumber(String mobileNumber) {
        return mobileNumber.matches("\\d{10}");
    }

    private boolean validateEmail(String email) {
        return email.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
    }

    private boolean validateCity(String city) {
        return city.matches("[a-zA-Z\\s]+");
    }

    private boolean validateAge(int age) {
        return age >= 18 && age <= 100;
    }

    private void writeCustomerToFile(Passengers customer) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("customers.txt", true))) {
            writer.println("<=====Customer Details:-=====>\n" + "Customer ID: " + customer.generateCustomerId()
                    + "\n" +
                    "Full Name: " + customer.name + ";\n" + "Mobile Number: " + customer.mobileNumber + ";\n"
                    + "Customer Email ID: " + customer.email + ";\n" + "Customer Address: " + customer.city
                    + ";\n" + "Customer Age: " + customer.age
                    + "\n__");
        } catch (IOException e) {
            System.out.println("Error writing customer details to file: " + e.getMessage());
        }
    }

    public void registerBus(String busNumber, int totalSeats, String startPoint, String endPoint,
            String startTime, double fare) {
        if (validateBusNumber(busNumber) && validateTotalSeats(totalSeats) &&
                validateCity(startPoint) && validateCity(endPoint) &&
                validateStartTime(startTime) && validateFare(fare)) {
            Bus bus = new Bus(busNumber, totalSeats, startPoint, endPoint, startTime, fare);
            buses.add(bus);
            writeBusToFile(bus);
            System.out.println("Bus registered successfully!");
        } else {
            System.out.println("Invalid input. Bus not registered.");
        }
    }

    private boolean validateBusNumber(String busNumber) {
        // Ensure bus number is not empty
        return !busNumber.isEmpty();
    }

    private boolean validateTotalSeats(int totalSeats) {
        // Ensure total seats is reasonable (let's say maximum 1000)
        return totalSeats > 0 && totalSeats <= 1000;
    }

    private boolean validatestart(String startPoint, String endPoint) {
        // Ensure both city names are not empty
        return !startPoint.isEmpty() && !endPoint.isEmpty();
    }

    private boolean validateStartTime(String startTime) {
        // Ensure start time follows the format HH:MM:SS
        return startTime.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
    }

    private boolean validateFare(double fare) {
        // Ensure fare is not negative and not excessively high (let's say maximum
        // 10000)
        return fare >= 0 && fare <= 100000;
    }

    public void searchBuses(String startPoint, String endPoint) {
        System.out.println("Available Buses:");
        for (Bus bus : buses) {
            if (bus.startPoint.equalsIgnoreCase(startPoint) && bus.endPoint.equalsIgnoreCase(endPoint)) {
                System.out.println("Bus Number: " + bus.busNumber + ", Starting Time: " + bus.startTime +
                        ", Fare: " + bus.fare + ", Available Seats: "
                        + (bus.totalSeats - getReservedSeats(bus)));
            }
        }
    }

    public void searchReservedSeats(String option, String value) {
        switch (option.toLowerCase()) {
            case "reserved date":
                System.out.println("Reserved Seats for Date: " + value);
                searchReservedSeatsByDate(value);
                break;
            case "customer id":
                System.out.println("Reserved Seats for Customer ID: " + value);
                searchReservedSeatsByCustomerId(value);
                break;
            case "bus id":
                System.out.println("Reserved Seats for Bus ID: " + value);
                searchReservedSeatsByBusId(value);
                break;
            default:
                System.out.println("Invalid search option.");
        }
    }

    private void searchReservedSeatsByDate(String date) {
        // Implement search by date logic here
        int reservedSeats = 0;
        for (Passengers customer : customers) {
            if (customer.name.equalsIgnoreCase(date)) {
                reservedSeats++;
            }
        }
        System.out.println("Total reserved seats for date " + date + ": " + reservedSeats);
    }

    private void searchReservedSeatsByCustomerId(String customerId) {
        // Implement search by customer ID logic here
        int reservedSeats = 0;
        for (Passengers customer : customers) {
            if (String.valueOf(customer.generateCustomerId()).equalsIgnoreCase(customerId)) {
                reservedSeats++;
            }
        }
        System.out.println("Total reserved seats for customer ID " + customerId + ": " + reservedSeats);
    }

    private void searchReservedSeatsByBusId(String busId) {
        // Implement search by bus ID logic here
        int reservedSeats = 0;
        for (Passengers customer : customers) {
            if (customer.name.equalsIgnoreCase(busId)) {
                reservedSeats++;
            }
        }
        System.out.println("Total reserved seats for bus ID " + busId + ": " + reservedSeats);
    }

    private int getReservedSeats(Bus bus) {
        int reservedSeats = 0;
        for (Passengers customer : customers) {
            if (customer.name.equalsIgnoreCase(bus.busNumber)) {
                reservedSeats++;
            }
        }
        return reservedSeats;
    }

    // Existing methods omitted for brevity...
    private void writeWaitingCustomerToFile(Passengers customer) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("waiting_customers.txt", true))) {
            writer.println("<=====Waiting Customer Details:-=====>\n" + "Customer ID: " + customer.generateCustomerId()
                    + "\n" +
                    "Full Name: " + customer.name + ";\n" + "Mobile Number: " + customer.mobileNumber + ";\n"
                    + "Customer Email ID: " + customer.email + ";\n" + "Customer Address: " + customer.city
                    + ";\n" + "Customer Age: " + customer.age
                    + "\n__");
        } catch (IOException e) {
            System.out.println("Error writing waiting customer details to file: " + e.getMessage());
        }
    }

    public void reserveSeat(String customerName, String busNumber, int seatCount) {
        Passengers customer = findCustomerByName(customerName);
        Bus bus = findBusByNumber(busNumber);

        if (customer != null && bus != null) {
            if (validateSeatAvailability(bus, seatCount)) {
                bus.totalSeats -= seatCount;
                customers.add(customer);

                System.out.println("Seat reserved successfully for " + customer.name + " on Bus " + bus.busNumber);
                notifyCustomer(customer, "Your seat has been reserved on Bus " + bus.busNumber);
                notifyPartnerPassenger(customer,
                        "Your partner " + customer.name + " has reserved a seat on Bus " + bus.busNumber);

                if (!waitingQueue.isEmpty()) {
                    Passengers nextCustomer = waitingQueue.poll();
                    customers.add(nextCustomer);
                    System.out.println("Next customer " + nextCustomer.name + " notified for Bus " + bus.busNumber);
                    notifyCustomer(nextCustomer, "You are next in line for Bus " + bus.busNumber);
                }
            } else {
                waitingQueue.add(customer);
                writeWaitingCustomerToFile(customer);
                System.out.println("No available seats. Added " + customer.name + " to the waiting queue.");
            }
        } else {
            System.out.println("Customer or Bus not found.");
        }
    }

    private void notifyPartnerPassenger(Passengers customer, String message) {
        System.out.println("Notification sent to partner passenger of " + customer.name + ": " + message);
    }

    private boolean validateSeatAvailability(Bus bus, int seatCount) {
        return bus.totalSeats >= seatCount && (bus.totalSeats - getReservedSeats(bus)) >= seatCount;
    }

    public void cancelReservation(String customerName, String busNumber) {
        Passengers customer = findCustomerByName(customerName);
        Bus bus = findBusByNumber(busNumber);
        if (customer != null && bus != null) {
            customers.remove(customer);
            System.out.println("Reservation cancelled for " + customer.name + " on Bus " + bus.busNumber);
            notifyCustomer(customer, "Your reservation on Bus " + bus.busNumber + " has been cancelled.");
            notifyNextCustomer(bus);
        } else {
            System.out.println("Customer or Bus not found.");
        }
    }

    public void deleteCustomerById(int customerId) {
        Passengers removedCustomer = null;
        for (Passengers customer : customers) {
            if (customer.generateCustomerId() == customerId) {
                removedCustomer = customer;
                customers.remove(customer);
                break;
            }
        }
        if (removedCustomer != null) {
            System.out.println("Customer with ID " + customerId + " deleted successfully.");
            notifyCustomer(removedCustomer, "Your account has been deleted.");
        } else {
            System.out.println("Customer with ID " + customerId + " not found.");
        }
    }

    public void deleteBusByNumber(String busNumber) {
        Bus removedBus = null;
        for (Bus bus : buses) {
            if (bus.busNumber.equalsIgnoreCase(busNumber)) {
                removedBus = bus;
                buses.remove(bus);
                break;
            }
        }
        if (removedBus != null) {
            System.out.println("Bus with number " + busNumber + " deleted successfully.");
            notifyBusOwner(removedBus, "Your bus has been deleted.");
        } else {
            System.out.println("Bus with number " + busNumber + " not found.");
        }
    }

    private Passengers findCustomerByName(String customerName) {
        for (Passengers customer : customers) {
            if (customer.name.equalsIgnoreCase(customerName)) {
                return customer;
            }
        }
        return null;
    }

    private Bus findBusByNumber(String busNumber) {
        for (Bus bus : buses) {
            if (bus.busNumber.equalsIgnoreCase(busNumber)) {
                return bus;
            }
        }
        return null;
    }

    private void notifyCustomer(Passengers customer, String message) {
        System.out.println("Notification sent to " + customer.name + ": " + message);
    }

    private void notifyBusOwner(Bus bus, String message) {
        // Assuming there's a field for bus owner in Bus class
        System.out.println("Notification sent to Bus Owner of bus " + bus.busNumber + ": " + message);
    }

    private void notifyNextCustomer(Bus bus) {
        if (!waitingQueue.isEmpty()) {
            Passengers nextCustomer = waitingQueue.poll();
            customers.add(nextCustomer);
            System.out.println("Next customer " + nextCustomer.name + " notified for Bus " + bus.busNumber);
            notifyCustomer(nextCustomer, "You are next in line for Bus " + bus.busNumber);
        }
    }

    public void updateCustomer(String name, String mobileNumber, String email, String city, int age) {
        if (validateName(name) && validateMobileNumber(mobileNumber) && validateEmail(email)
                && validateCity(city) && validateAge(age)) {
            for (Passengers customer : customers) {
                if (customer.name.equalsIgnoreCase(name)) {
                    customer.mobileNumber = mobileNumber;
                    customer.email = email;
                    customer.city = city;
                    customer.age = age;
                    System.out.println("Customer information updated successfully!");

                    updateCustomerInFile(customer);
                    return;
                }
            }
        } else {
            System.out.println("Invalid input. Customer information not updated.");
        }
    }

    private void updateCustomerInFile(Passengers updatedCustomer) {
        try {
            File inputFile = new File("customers.txt");
            File tempFile = new File("tempCustomers.txt");

            try (Scanner scanner = new Scanner(inputFile);
                    PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains(updatedCustomer.name)) {
                        writer.println("<=====Customer Details:-=====>\n" +
                                "Full Name: " + updatedCustomer.name + ";\n" + "Mobile Number: "
                                + updatedCustomer.mobileNumber + ";\n"
                                + "Customer Email ID: " + updatedCustomer.email + ";\n" + "Customer Address: "
                                + updatedCustomer.city
                                + ";\n" + "Customer Age: " + updatedCustomer.age
                                + "\n__");
                    } else {
                        writer.println(line);
                    }
                }
            }

            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                System.out.println("Error updating customer details in file.");
            }
        } catch (IOException e) {
            System.out.println("Error updating customer details in file: " + e.getMessage());
        }
    }

    public void updateBus(String busNumber, int totalSeats, String startPoint, String endPoint,
            String startTime,
            double fare) {
        if (validateBusNumber(busNumber) && validateTotalSeats(totalSeats) && validateCity(startPoint)
                && validateCity(endPoint) && validateStartTime(startTime) && validateFare(fare)) {
            for (Bus bus : buses) {
                if (bus.busNumber.equalsIgnoreCase(busNumber)) {
                    bus.totalSeats = totalSeats;
                    bus.startPoint = startPoint;
                    bus.endPoint = endPoint;
                    bus.startTime = startTime;
                    bus.fare = fare;
                    System.out.println("Bus information updated successfully!");

                    updateBusInFile(bus);
                    return;
                }
            }
        } else {
            System.out.println("Invalid input. Bus information not updated.");
        }
    }

    private void updateBusInFile(Bus updatedBus) {
        try {
            File inputFile = new File("buses.txt");
            File tempFile = new File("tempBuses.txt");

            try (Scanner scanner = new Scanner(inputFile);
                    PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains(updatedBus.busNumber)) {
                        writer.println(
                                updatedBus.busNumber + ";" + updatedBus.totalSeats + ";" + updatedBus.startPoint
                                        + ";" +
                                        updatedBus.endPoint + ";" + updatedBus.startTime + ";"
                                        + updatedBus.fare);
                    } else {
                        writer.println(line);
                    }
                }
            }

            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                System.out.println("Error updating bus details in file.");
            }
        } catch (IOException e) {
            System.out.println("Error updating bus details in file: " + e.getMessage());
        }
    }

    public void displayAllReservations() {
        List<String> customerReservations = readReservationsFromFile("customers.txt");
        List<String> busReservations = readReservationsFromFile("buses.txt");
        List<String> waitingCustomerReservations = readReservationsFromFile("waiting_customers.txt");

        System.out.println("All Reservations:");

        System.out.println("\nCustomer Reservations (Newest to Oldest):");
        displayReservationsInOrder(customerReservations);

        System.out.println("\nBus Reservations (Newest to Oldest):");
        displayReservationsInOrder(busReservations);

        System.out.println("\nWaiting Customer Reservations (Newest to Oldest):");
        displayReservationsInOrder(waitingCustomerReservations);
    }

    private List<String> readReservationsFromFile(String fileName) {
        List<String> reservations = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                reservations.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading reservations from file " + fileName + ": " + e.getMessage());
        }
        return reservations;
    }

    private void displayReservationsInOrder(List<String> reservations) {
        Collections.reverse(reservations); // Sort from newest to oldest
        for (String reservation : reservations) {
            System.out.println(reservation);
        }
    }

    private void writeBusToFile(Bus bus) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("buses.txt", true))) {
            writer.println("<----------Bus Details---------->\n BusNumber: " + bus.busNumber + "\n Total Seat:"
                    + bus.totalSeats + "\n Start Point:" + bus.startPoint + "\nEnd Point:" + bus.endPoint
                    + "\nStart Time:"
                    + bus.startTime + "\nFare:" + bus.fare + "\n__");
        } catch (IOException e) {
            System.out.println("Error writing bus details to file: " + e.getMessage());
        }
    }

    public void loadTicketsFromFile() {
        try (Scanner scanner = new Scanner(new File("buses.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line); // Here you can process each line as per your requirement
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public void saveTicketsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("tickets.txt"))) {
            for (Passengers customer : customers) {
                writer.println(
                        customer.name + ";" + customer.mobileNumber + ";" + customer.email + ";" + customer.city
                                + ";" + customer.age);
            }
            for (Bus bus : buses) {
                writer.println(
                        bus.busNumber + ";" + bus.totalSeats + ";" + bus.startPoint + ";" + bus.endPoint + ";"
                                + bus.startTime + ";" + bus.fare);
            }
        } catch (IOException e) {
            System.out.println("Error saving tickets to file: " + e.getMessage());
        }
    }

    // Existing methods omitted for brevity...
}

class Main {
    public static void main(String[] args) {
        ReservationSystem reservationSystem = new ReservationSystem();
        Scanner scanner = new Scanner(System.in);
        int choice;
        System.out.println("++++++++++++++Bus Reservation++++++++++++++\n");
        do {
            System.out.println("_____Select option_____");
            System.out.println("1. Register Customer");
            System.out.println("2. Register Bus");
            System.out.println("3. Search Buses");
            System.out.println("4. Reserve Seat");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Display All Reservations");
            System.out.println("7. Search Reserved Seats");
            System.out.println("8. Exit");
            System.out.println("9. Update Customer");
            System.out.println("10. Update Bus");
            System.out.println("11. Delete Customer");
            System.out.println("12. Delete Bus");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter mobile number: ");
                    String mobileNumber = scanner.nextLine(); // Changed the type to String
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter city: ");
                    String city = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    reservationSystem.registerCustomer(name, mobileNumber, email, city, age);
                    break;
                case 2:
                    System.out.print("Enter bus number: ");
                    String busNumber = scanner.nextLine();
                    System.out.print("Enter total seats: ");
                    int totalSeats = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter starting point: ");
                    String startPoint = scanner.nextLine();
                    System.out.print("Enter ending point: ");
                    String endPoint = scanner.nextLine();
                    System.out.print("Enter starting time: ");
                    String startTime = scanner.nextLine();
                    System.out.print("Enter fare: ");
                    double fare = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    reservationSystem.registerBus(busNumber, totalSeats, startPoint, endPoint, startTime, fare);
                    break;
                case 3:
                    System.out.print("Enter starting point to search: ");
                    startPoint = scanner.nextLine();
                    System.out.print("Enter ending point to search: ");
                    endPoint = scanner.nextLine();
                    reservationSystem.searchBuses(startPoint, endPoint);
                    break;
                case 4:
                    System.out.print("Enter customer name to reserve seat: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter bus number to reserve seat: ");
                    String busNum = scanner.nextLine();
                    System.out.print("Enter number of seats to reserve: ");
                    int seatCount = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    reservationSystem.reserveSeat(customerName, busNum, seatCount);
                    break;
                case 5:
                    System.out.print("Enter customer name to cancel reservation: ");
                    String cancelCustomerName = scanner.nextLine();
                    System.out.print("Enter bus number to cancel reservation: ");
                    String cancelBusNumber = scanner.nextLine();
                    reservationSystem.cancelReservation(cancelCustomerName, cancelBusNumber);
                    break;
                case 6:
                    reservationSystem.displayAllReservations();
                    break;
                case 7:
                    System.out.print("Enter search option (Reserved Date, Customer ID, Bus ID): ");
                    String option = scanner.nextLine();
                    System.out.print("Enter value: ");
                    String value = scanner.nextLine();
                    reservationSystem.searchReservedSeats(option, value);
                    break;
                // Existing cases omitted for brevity...

                case 8:
                    reservationSystem.saveTicketsToFile();
                    System.out.println("Exiting...");
                    break;
                case 9:
                    System.out.print("Enter customer name to update: ");
                    name = scanner.nextLine();
                    System.out.print("Enter new mobile number: ");
                    mobileNumber = scanner.nextLine(); // Changed the type to String
                    System.out.print("Enter new email: ");
                    email = scanner.nextLine();
                    System.out.print("Enter new city: ");
                    city = scanner.nextLine();
                    System.out.print("Enter new age: ");
                    age = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    reservationSystem.updateCustomer(name, mobileNumber, email, city, age);
                    break;
                case 10:
                    System.out.print("Enter bus number to update: ");
                    busNumber = scanner.nextLine();
                    System.out.print("Enter new total seats: ");
                    totalSeats = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new starting point: ");
                    startPoint = scanner.nextLine();
                    System.out.print("Enter new ending point: ");
                    endPoint = scanner.nextLine();
                    System.out.print("Enter new starting time: ");
                    startTime = scanner.nextLine();
                    System.out.print("Enter new fare: ");
                    fare = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    reservationSystem.updateBus(busNumber, totalSeats, startPoint, endPoint, startTime, fare);
                    break;
                case 11:
                    System.out.print("Enter customer ID to delete: ");
                    int customerId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    reservationSystem.deleteCustomerById(customerId);
                    break;
                case 12:
                    System.out.print("Enter bus number to delete: ");
                    busNumber = scanner.nextLine();
                    reservationSystem.deleteBusByNumber(busNumber);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                    break;
                // Existing cases omitted for brevity...
            }
        } while (choice != 8);
        scanner.close();
    }
}
