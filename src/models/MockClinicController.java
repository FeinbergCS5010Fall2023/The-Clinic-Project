package models;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 * This is the mock class that has been used for testing.
 */
public class MockClinicController extends JFrame implements ClinicController {
  private static final long serialVersionUID = 1L;
  private final Appendable out;
  private final Scanner scan;

  /**
   * The constructor takes in two parameters.
   * 
   * @param in that is type Readable.
   * @param out that is type Appendable.
   */
  public MockClinicController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    this.scan = new Scanner(in);
  }

  /**
   * This method defines the input and output parameters for the mock.
   * 
   * @param in is a type Readable.
   * @param out is a type Appendable.
   */
  public MockClinicController(InputStream in, Appendable out) {
    this(new InputStreamReader(in), out);
  }

  /**
   * Constructs a new instance of the MockClinicController with default input and output streams.
   * This constructor initializes the MockClinicController using the standard input and output
   * streams (System.in and System.out).
   */
  public MockClinicController() {
    this(System.in, System.out);
  }

  @Override
  public String displayClientInfo(Client client, Clinic clinic) {
    return "Mock Client Info: " + client.toString();
  }

  @Override
  public Client registerExistingClientWithVisitRecord(Client client, Clinic clinic,
      VisitRecord record) {
    return client;
  }

  @Override
  public Staff registerNewStaff(Staff staff, Clinic clinic) {
    return staff;
  }

  @Override
  public String displayAllInfo(Clinic clinic) {
    return "Mock All Info";
  }

  @Override
  public void sendPatientHome(Client client, Staff staff, Clinic clinic) {
    System.out.println("Mock: Sending patient home - " + client.toString());
    clinic.removeClinicClient(client, staff);
  }

  @Override
  public void assignPatientToNewRoom(Room room, Client client, Clinic clinic) {
    System.out.println("Mock: Assigning patient to a new room - " + client.toString());
    clinic.assignClientToNewRoom(client, room);
  }

  @Override
  public boolean assignStaffToPatient(Clinic clinic, Staff staff, Client client) {
    System.out.println(
        "Mock: Assigning staff to patient - " + staff.toString() + " to " + client.toString());
    if (!clinic.assignStaffToClient(staff, client)) {
      System.out.println(staff.getFirstName() + " " + staff.getLastName()
          + " is already assigned to " + client.getFirstName() + " " + client.getLastName());
      return false;
    }
    return true;
  }

  @Override
  public Client registerClientWithVisitRecord(String firstName, String lastName, String birthDay,
      VisitRecord record, Clinic clinic) throws IOException {
    Client mockClient = new Client(1, firstName, lastName, birthDay); // Create a mock client
    mockClient.setRecord(record); // Set the mock client's visit record
    out.append("Mock: Welcome to the clinic " + firstName + " " + lastName + "\n");
    out.append("Mock: You have been placed in the Waiting Room!\n");

    return mockClient;
  }

  @Override
  public void handleAddNewRoom(Clinic clinic) throws IOException {
    try {
      String roomName = "MockRoom";
      String roomType = "MockType";
      String staffFirstName = "MockStaffFirstName";
      String staffLastName = "MockStaffLastName";
      boolean tryAgain = false;

      int[] roomId = new int[4];

      out.append("Mock: Please enter the Name of the new room:\n");
      out.append(roomName + "\n");

      out.append("Mock: Please enter the type room this is going to be:\n");
      out.append(roomType + "\n");

      for (int i = 0; i < 4; i++) {
        roomId[i] = clinic.getClinicRooms().size();
      }

      boolean secondCheck = false;
      while (!secondCheck) {
        out.append("Mock: Please enter the First Name of the Staff who approved this:");
        out.append(staffFirstName + "\n");

        out.append("Mock: Please enter the Last Name of the Staff who approved this:");
        out.append(staffLastName + "\n");

        for (Staff staff : clinic.getClinicStaffs()) {
          if (staff.getFirstName().contains(staffFirstName)
              && staff.getLastName().contains(staffLastName)) {
            secondCheck = true;
            break;
          }
        }

        if (!secondCheck) {
          out.append(
              "Mock: Staff doesn't exist or is not approved to build new rooms into the clinic."
                  + " Do you want to try again? (yes/no)");
          out.append(tryAgain ? "yes\n" : "no\n");
          if (!tryAgain) {
            return; // Return without adding a room
          }
        }
      }

      Room room = new Room(roomId, roomType, roomName);
      clinic.addClinicRoom(room);

    } catch (IllegalArgumentException e) {
      out.append("Mock: Error: " + e.getMessage() + ", please try again.");
    }
  }

  /**
   * Simulates the viewing of a patient's record history through the console.
   *
   * This method displays a list of patient names from the given clinic, allows the user to select a
   * patient, retrieves and displays the selected patient's information, including first name, last
   * name, date of birth, and visit records. The user is prompted to enter the number corresponding
   * to the patient they want to view.
   *
   * @param clinic The clinic instance containing patient information.
   * @throws IOException If an I/O error occurs while reading user input.
   */
  public void handleViewPatientRecordHistory(Clinic clinic) throws IOException {
    System.out.println("Mock: Simulating the view of patient record history through console");

    // Mock data for patient names
    List<String> patientNames = getClinicClientNames(clinic); // Pass clinic to the method

    // Display patient names for selection
    System.out.println("Mock: Please select the patient:");

    for (int i = 0; i < patientNames.size(); i++) {
      System.out.println((i + 1) + ". " + patientNames.get(i));
    }

    // Read user input for patient selection
    System.out.print("Mock: Enter the number corresponding to the patient: ");
    int selectedPatientIndex;

    try {
      selectedPatientIndex = Integer.parseInt(scan.next());
    } catch (NumberFormatException e) {
      System.out.println("Mock: Invalid input. Exiting...");
      return;
    }

    // Validate user input
    if (selectedPatientIndex < 1 || selectedPatientIndex > patientNames.size()) {
      System.out.println("Mock: Invalid selection. Exiting...");
      return;
    }

    // Find the selected patient
    Client patient = findClientByName(clinic, patientNames.get(selectedPatientIndex - 1));

    if (patient == null) {
      System.out.println("Mock: Patient doesn't exist. Please try again.");
      return;
    }

    // Display patient information
    System.out.println("Mock: Patient Information:");
    System.out.println("First Name: " + patient.getFirstName());
    System.out.println("Last Name: " + patient.getLastName());
    System.out.println("Date of Birth: " + patient.getBirthDateTime());
    System.out.println(getVisitRecord(patient));

    System.out.println("Mock: End of Patient Information.");
  }

  private List<String> getClinicClientNames(Clinic clinic) {
    List<String> names = new ArrayList<>();
    for (Client client : clinic.getClinicClients()) {
      names.add(client.getFirstName() + " " + client.getLastName());
    }
    return names;
  }

  private Client findClientByName(Clinic clinic, String fullName) {
    for (Client client : clinic.getClinicClients()) {
      String name = client.getFirstName() + " " + client.getLastName();
      if (name.equals(fullName)) {
        return client;
      }
    }
    return null;
  }

  /**
   * Finds a client in the given clinic based on the provided first name.
   *
   * This method iterates through the list of clients in the clinic, concatenates the first name and
   * last name of each client, and checks if it contains the specified first name. If a match is
   * found, the corresponding client is returned; otherwise, null is returned.
   *
   * @param firstName The first name to search for.
   * @param clinic The clinic instance containing the list of clients.
   * @return The Client instance with a matching first name, or null if no match is found.
   */
  private Client findClientByName(String firstName, Clinic clinic) {
    for (Client client : clinic.getClinicClients()) {
      String name = client.getFirstName() + " " + client.getLastName();
      if (name.contains(firstName)) {
        return client;
      }
    }
    return null;
  }

  private String getVisitRecord(Client patient) {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < patient.getRecordHistory().size(); i++) {
      res.append(patient.getRecordHistory().get(i).toString());
    }
    if (patient.getRecord() == null) {
      res.append(patient.getFirstName()).append(' ').append(patient.getLastName())
          .append(" does not have a medical record with us.");
    }
    return res.toString();
  }

  @Override
  public void handleAddPatient(Clinic clinic) throws IOException {
    System.out.println("Mock: Simulating the addition of a new patient through console");

    String patientFirstName = "MockPatientFirstName";
    String patientLastName = "MockPatientLastName";
    String patientBirthDay = "2000-01-01";

    try {
      System.out.println("Mock: Please enter the First Name of the patient: " + patientFirstName);
      System.out.println("Mock: Please enter the Last Name of the patient: " + patientLastName);
      System.out.println(
          "Mock: Please enter the Birth Day of the patient (YYYY-MM-DD): " + patientBirthDay);

      Registration registration = new Registration(patientFirstName, patientLastName,
          patientBirthDay);
      Client patient = clinic.registerClient(registration, null); // Assuming null for VisitRecord

      System.out.println("Mock: Successfully added the following patient: " + patient);

    } catch (IllegalArgumentException e) {
      System.out.println("Mock: Error: " + e.getMessage() + ", please try again.");
    }
  }

  @Override
  public void handleAddStaff(Clinic clinic) throws IOException {
    System.out.println("Mock: Simulating the addition of a new staff member through console");

    // Mock user input
    String staffFirstName = "MockFirstName";
    String staffLastName = "MockLastName";
    String staffOccupation = "MockOccupation";

    try {
      System.out
          .println("Mock: Please enter the First Name of the Staff member: " + staffFirstName);
      System.out.println("Mock: Please enter the Last Name of the Staff member: " + staffLastName);
      System.out
          .println("Mock: Please enter the occupation of the Staff member: " + staffOccupation);

      Staff staff = new Staff(staffOccupation, staffFirstName, staffLastName);

      if (clinic.getClinicStaffs().contains(staff)) {
        System.out.println("Mock: This staff member already exists");
        return;
      }

      registerNewStaff(staff, clinic);

      System.out.println("Mock: Successfully added the following staff member: " + staff);

    } catch (IllegalArgumentException e) {
      System.out.println("Mock: Error: " + e.getMessage() + ", please try again.");
    }
  }

  @Override
  public void handleRemovePatient(Clinic clinic) throws IOException {
    System.out.println("Mock: Simulating the removal of a patient from the clinic");

    String dischargeFirstName = "MockPatientFirstName";
    String patientLastName = "MockPatientLastName";
    String patientBirthDay = "2000-01-01";

    String dischargeStaffFirstName = "Dr. MockStaffFirstName";
    String staffLastName = "MockLastName";
    String staffOccupation = "physician";
    try {
      Staff staff = new Staff(staffOccupation, dischargeStaffFirstName, staffLastName);
      Registration registration = new Registration(dischargeFirstName, patientLastName,
          patientBirthDay);
      Client patient = clinic.registerClient(registration, null);

      clinic.addClinicStaff(staff);
      clinic.addClinicClient(patient);
      System.out.println("Mock: Please select the patient to discharge: " + dischargeFirstName);

      // Simulate finding the selected patient
      patient = findClientByName(dischargeFirstName, clinic);

      if (patient == null) {
        System.out.println("Mock: Patient doesn't exist. Please try again.");
        return;
      }

      System.out
          .println("Mock: Please select the staff who approved this: " + dischargeStaffFirstName);

      Staff staffApproval = findStaffByName(dischargeStaffFirstName, clinic);
      sendPatientHome(patient, staffApproval, clinic);
      System.out.println("Mock: " + patient.getFirstName() + " has been discharged, approved by: "
          + dischargeStaffFirstName);

    } catch (IllegalArgumentException e) {
      System.out.println("Mock: Error: " + e.getMessage() + ", please try again.");
    }
  }

  private Staff findStaffByName(String firstName, Clinic clinic) {
    for (Staff staff : clinic.getClinicStaffs()) {
      String name = staff.getFirstName() + " " + staff.getLastName();
      if (name.contains(firstName)) {
        return staff;
      }
    }
    return null;
  }

  /**
   * Finds a staff member in the clinic based on the provided first and last name.
   *
   * Iterates through the list of clinic staff, checks if both first and last names match the
   * provided names. If a match is found, returns the corresponding staff member; otherwise, returns
   * null.
   *
   * @param firstName First name of the staff to search for.
   * @param lastName Last name of the staff to search for.
   * @param clinic Clinic instance containing the list of staff.
   * @return The Staff instance with matching first and last names, or null if no match is found.
   */
  private Staff findStaffByName(String firstName, String lastName, Clinic clinic) {
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains(firstName) && staff.getLastName().contains(lastName)) {
        return staff;
      }
    }
    return null;
  }

  @Override
  public void handleAssignPatientToRoom(Clinic clinic) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void handleDisplayAllInfo(Clinic clinic) throws IOException {
    try {
      this.out.append(displayAllInfo(clinic));
    } catch (IOException e) {
      this.out.append("whatever");
    }
  }

  /**
   * Simulates the process of unassigning staff from a patient through a graphical user interface
   * (GUI). This method takes the clinic instance, patient's first name, patient's last name,
   * staff's first name, and staff's last name as parameters. It then finds the corresponding
   * patient and staff using the provided names, unassigns the staff from the patient, and prints a
   * success message.
   *
   * @param clinic The clinic instance containing patient and staff information.
   * @param patientFirstName The first name of the patient.
   * @param patientLastName The last name of the patient.
   * @param staffFirstName The first name of the staff to be unassigned.
   * @param staffLastName The last name of the staff to be unassigned.
   * @throws IOException If an I/O error occurs during the unassignment process.
   */
  public void handleUnassignStaffFromClient(Clinic clinic, String patientFirstName,
      String patientLastName, String staffFirstName, String staffLastName) throws IOException {
    System.out.println("Mock: Simulating unassigning staff from a patient through GUI.");
    System.out.println("Mock: Unassigning staff from patient...");

    unassignStaffFromPatient(findClientByName(patientFirstName, clinic),
        findStaffByName(staffFirstName, clinic), clinic);
    System.out.println("Mock: Successfully unassigned staff from the patient.");
  }

  @Override
  public void handleUnassignStaffFromClient(Clinic clinic) throws IOException {
    return;
  }

  private void unassignStaffFromPatient(Client patient, Staff staff, Clinic clinic) {
    clinic.removeStaffFromClient(staff, patient);
    System.out.println("Able to remove him\n");

  }

  @Override
  public void displayGame(Clinic clinic) {
    return;
  }

  @Override
  public void handleYearNoVisit(Clinic clinic) throws IOException {
    System.out
        .println("Mock: Simulating the display of patients with no visits for more than 365 days");

    String list = clinic.getNoYearVisitList();

    try {
      if (list.length() == 0) {
        System.out.println("Mock: There are no patients that haven't visited the clinic for"
            + " more than 365 days from today.");
      } else {
        System.out.println("Mock: " + list);
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Mock: Error: " + e.getMessage() + ", please try again.");
    }
  }

  @Override
  public void handleViewAllStaffMembers(Clinic clinic) throws IOException {
    System.out.println("Mock: Simulating the view of all staff members through console");
    System.out.println(
        "Mock: --------------------------------------------------------------------------");
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      System.out.println(
          "Mock: Staff: " + entry.getKey().getFirstName() + " " + entry.getKey().getLastName());
      System.out.println("Mock: Clients assigned:");
      for (Client c : entry.getValue()) {
        System.out.println("Mock: " + c.getLastName() + ", " + c.getFirstName());
      }
      System.out.println(
          "Mock: Total number of assigned patients ever: " + entry.getKey().getNumPatients());
    }
    System.out.println(
        "Mock: --------------------------------------------------------------------------");
  }

  /**
   * Handles the removal of a staff member from the clinic.
   *
   * Takes clinic, discharge staff's first and last name as parameters. Finds the discharge staff,
   * removes them from the clinic, and prints a success message.
   *
   * @param clinic Clinic instance containing staff information.
   * @param disChargeStaffFirstName First name of the staff to be removed.
   * @param disChargeStaffLastName Last name of the staff to be removed.
   * @throws IOException If an I/O error occurs during the removal process.
   */
  public void handleRemoveStaff(Clinic clinic, String disChargeStaffFirstName,
      String disChargeStaffLastName) throws IOException {
    try {
      Staff dischargeStaff = findStaffByName(disChargeStaffFirstName, disChargeStaffLastName,
          clinic);

      if (dischargeStaff == null) {
        System.out.println("Staff doesn't exist.");
        return; // Return without removing a staff member
      }

      clinic.removeClinicStaff(dischargeStaff);
      System.out.println(dischargeStaff.getFirstName() + " " + dischargeStaff.getLastName()
          + " has been removed from the clinic.\n");
    } catch (NullPointerException e) {
      System.out.println("Error: " + e.getMessage() + ". Please try again.\n");
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage() + ". Please try again.\n");
    }
  }

  @Override
  public void handleRemoveStaff(Clinic clinic) throws IOException {

    return;
  }

  @Override
  public String displayRoomInfo(Room room, Clinic clinic) {
    StringBuilder result = new StringBuilder();
    int roomNum = findRoomNumber(room, clinic);

    List<Client> clientsInRoom = getClientsInRoom(roomNum, clinic);

    if (clientsInRoom.isEmpty()) {
      result.append("Mock: Empty\n");
    } else {
      for (Client client : clientsInRoom) {
        result.append("Mock: ").append(client).append("\n");
      }
    }

    List<Staff> staffInRoom = getStaffInRoom(clientsInRoom, clinic);

    for (Staff staff : staffInRoom) {
      result.append("Mock: ").append(staff).append("\n");
    }

    result.append("Mock: \n");
    return result.toString();
  }

  private int findRoomNumber(Room room, Clinic clinic) {
    for (Map.Entry<Integer, int[]> entry : clinic.getRoomKey().entrySet()) {
      if (Arrays.equals(room.getId(), entry.getValue())) {
        return entry.getKey();
      }
    }
    return 0;
  }

  private List<Client> getClientsInRoom(int roomNum, Clinic clinic) {
    List<Client> clientsInRoomArrayList = new ArrayList<>();

    for (Client client : clinic.getClinicClients()) {
      if (roomNum == client.getRoomNum()) {
        clientsInRoomArrayList.add(client);
      }
    }
    return clientsInRoomArrayList;
  }

  private List<Staff> getStaffInRoom(List<Client> clients, Clinic clinic) {
    List<Staff> staffInRoom = new ArrayList<>();

    for (Map.Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      for (Client client : clients) {
        if (entry.getValue().contains(client)) {
          staffInRoom.add(entry.getKey());
        }
      }
    }
    return staffInRoom;
  }

  @Override
  public void handleAssignStaffToClient(Clinic clinic) throws IOException {
    System.out.println("Mock: Simulating the assignment of staff to a patient through console");
    String patientFirstName = "MockPatientFirstName";
    String patientLastName = "MockPatientLastName";
    String dischargeStaffFirstName = "Dr. MockStaffFirstName";
    String staffLastName = "MockLastName";
    String staffOccupation = "physician";
    try {
      Staff staff = new Staff(staffOccupation, dischargeStaffFirstName, staffLastName);
      Registration registration = new Registration(patientFirstName, patientLastName, "2000-01-01");
      Client patient = clinic.registerClient(registration, null);

      clinic.addClinicStaff(staff);
      clinic.addClinicClient(patient);

      System.out.println("Mock: Please select the patient to assign staff: " + patientFirstName);
      patient = findClientByName(patientFirstName, clinic);

      if (patient == null) {
        System.out.println("Mock: Patient doesn't exist. Please try again.");
        return;
      }

      System.out.println("Mock: Please select the staff who will be assigned to the patient: "
          + dischargeStaffFirstName);

      Staff staffApproval = findStaffByName(dischargeStaffFirstName, clinic);
      if (!assignStaffToPatient(clinic, staffApproval, patient)) {
        System.out.println("Mock: " + staffApproval.getFirstName() + " "
            + staffApproval.getLastName() + " is already assigned to " + patient.getFirstName()
            + " " + patient.getLastName());
      } else {
        System.out.println("Mock: " + patient.getFirstName() + " " + patient.getLastName()
            + " has been assigned to " + staffApproval.getFirstName() + " "
            + staffApproval.getLastName());
      }

    } catch (IllegalArgumentException e) {
      System.out.println("Mock: Error: " + e.getMessage() + ", please try again.");
    }
  }

  @Override
  public void handleDisplayRoomInfo(Clinic clinic) throws IOException {
    boolean displayAnotherRoom = true;

    while (displayAnotherRoom) {
      boolean roomFound = false;
      Room roomInfo = null;
      String roomName = null;

      out.append("Here are the rooms in the clinic:\n");
      for (Room room : clinic.getClinicRooms()) {
        out.append(room.getRoomName() + "\n");
      }

      while (!roomFound) {
        out.append("Please enter the name of the room you would like to see:\n");
        roomName = getValidNameInput();

        for (Room room : clinic.getClinicRooms()) {
          if (room.getRoomName().contains(roomName)) {
            roomInfo = room;
            roomFound = true;
            break;
          }
        }

        if (!roomFound) {
          out.append("Room not found. Would you like to try again? (yes/no)\n");
          String tryAgain = scan.nextLine();

          if (!"yes".equalsIgnoreCase(tryAgain)) {
            return;
          }
        }
      }

      out.append("Room Information:\n");
      out.append(roomName + "\n" + displayRoomInfo(roomInfo, clinic) + "\n");

      out.append("Do you want to see another room? (yes/no)\n");
      String seeAnotherRoom = getValidNameInput();
      displayAnotherRoom = "yes".equalsIgnoreCase(seeAnotherRoom);
    }
  }

  @Override
  public void handleViewStaff(Clinic clinic) throws IOException {
    System.out
        .println("--------------------------------------------------------------------------");
    int count = 0;
    int numStaff = 0;

    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      numStaff++;
      if (entry.getValue().size() != 0) {
        System.out.println("Staff: " + entry.getKey().getFirstName() + " "
            + entry.getKey().getLastName() + "\nClients assigned: ");
        for (Client c : entry.getValue()) {
          System.out.println("\n " + c.getLastName() + ", " + c.getFirstName());
        }
        System.out.println(
            "\nTotal number of assigned patients ever: " + entry.getKey().getNumPatients() + "\n");
      } else {
        count++;
      }
    }
    if (count == numStaff) {
      System.out.println("There are no Staff members assigned to any patients at the moment.");
    }
    System.out
        .println("--------------------------------------------------------------------------");
  }

  @Override
  public void playGame(Clinic clinic, File file) {

    return;
  }

  private String getValidNameInput() throws IOException {
    String input;
    while (true) {
      input = scan.next();
      if (input.matches("[a-zA-Z]+")) {
        return input;
      } else {
        this.out.append("Invalid input. Please enter a valid name.");
      }
    }
  }

  /**
   * Simulates the action of clearing the map of a clinic.
   * This method clears the list of clients, client record history,
   * staff members, and rooms in the provided clinic.
   *
   * @param clinic The clinic to perform the simulated clear map action on.
   */
  public void simulateClearMapAction(Clinic clinic) {
    System.out.println("Mock: Simulating the clearMap action");
    clinic.getClinicClients().clear();
    for (Client client : clinic.getClinicClients()) {
      client.getRecordHistory().clear();
    }
    clinic.getClinicStaffs().clear();
    clinic.getClinicRooms().clear();

    System.out.println("Mock: Clinic map cleared successfully");
  }

}
