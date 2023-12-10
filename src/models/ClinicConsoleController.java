
package models;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * A console-based controller for managing a clinic's operations.
 *
 * The `ClinicConsoleController` class implements the `ClinicController` interface to provide a
 * text-based user interface for managing the operations of a clinic. It allows users to interact
 * with the clinic's functionality through a console interface. This class handles tasks such as
 * scheduling appointments, displaying patient information, and managing room allocations.
 *
 * The specific operations and user interactions are defined in the `ClinicController` interface,
 * which this class implements.
 */
public class ClinicConsoleController extends JFrame implements ClinicController {
  private static JFrame frame;
  private static final long serialVersionUID = 1L;
  private final Appendable out;
  private final Scanner scan;
  

  /**
   * ClinicConsoleController to take in the input and output.
   *
   * @param in Input
   * @param out Output
   */
  public ClinicConsoleController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  /**
   * Displays information about all clients in the clinic, including details such as name, birth
   * date, and other relevant information.
   *
   * @param client The client for which information is to be displayed.
   * @param clinic The clinic object containing client information.
   * @return A string containing detailed information about all clients in the clinic.
   */
  @Override
  public String displayClientInfo(Client client, Clinic clinic) {
    String res = "";
    for (Client clients : clinic.getClinicClients()) {
      res += clients.toString() + "\n";
    }
    return res;
  }

  /**
   * This method generates and returns a detailed display of information about a specific room in
   * the clinic, including details about clients and staff members assigned to that room. The
   * information is obtained from the clinic's data and is formatted into a string for further use
   * or output.
   *
   * @param room The room object for which information is to be displayed.
   * @param clinic The clinic object containing client, staff, and room information.
   * @return A string containing detailed information about the specified room and its occupants.
   */
  @Override
  public String displayRoomInfo(Room room, Clinic clinic) {
    String res = "";
    int roomNum = 0;
    // Temporary list of clients and staff are made so private data isn't shared
    ArrayList<Client> clientsInTheRoomArrayList = new ArrayList<>();
    ArrayList<Staff> staffInTheRoom = new ArrayList<>();

    // clients assigned to that room
    // We iterate through the room key and check if there are any room id's that
    // match the id of the given room.
    for (Map.Entry<Integer, int[]> entry : clinic.getRoomKey().entrySet()) {
      // This gived us the room number
      if (Arrays.equals(room.getId(), entry.getValue())) {
        roomNum = entry.getKey();
      }
    }

    // We then use that room number to determine which client(s) are
    // in that room
    for (int i = 0; i < clinic.getClinicClients().size(); i++) {
      if (roomNum == 0) {
        break;
      }

      // If they found the matchging room number with client, they are added
      // to the temporary clients list.
      if (clinic.getClinicClients().get(i).getRoomNum() == roomNum) {
        clientsInTheRoomArrayList.add(clinic.getClinicClients().get(i));
      }
    }
    // If room is empty, produce "Empty" message
    if (clientsInTheRoomArrayList.size() == 0) {
      res += "Empty\n";
    } else {
      // A string, res, collects the data and converts it into a string so that it
      // can be written to the .txt file.
      for (int i = 0; i < clientsInTheRoomArrayList.size(); i++) {
        res += clientsInTheRoomArrayList.get(i) + "\n";
      }
    }

    // any staff that is assigned to that client
    // This staff key list is used to add clients to a staff member if they have been
    // Assigned to them. If they have, then they are added to the list of clients
    // that are assigned to them
    for (Map.Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      for (int i = 0; i < clientsInTheRoomArrayList.size(); i++) {
        if (entry.getValue().contains(clientsInTheRoomArrayList.get(i))) {
          staffInTheRoom.add(entry.getKey());
        }
      }
    }

    // All that information is then added to the res string that is returned at the
    // end of the function
    for (int i = 0; i < staffInTheRoom.size(); i++) {
      res += staffInTheRoom.get(i) + "\n";
    }
    res += "\n";
    return res;
  }

  /**
   * Registers a client in the clinic with a provided visit record or retrieves an existing client
   * from the clinic archives with the same first name and last name, welcoming them back and
   * placing them in the waiting room if found. If the client is not found, a new registration is
   * created, and the client is added to the clinic's client list.
   *
   * @param firstName The first name of the client.
   * @param lastName The last name of the client.
   * @param birthDay The birth date of the client.
   * @param record The visit record to be associated with the client.
   * @param clinic The clinic object where the client will be registered or retrieved.
   * @return The registered or retrieved client with the provided visit record.
   * @throws IOException If an I/O error occurs while interacting with the input/output stream.
   */
  @Override
  public Client registerClientWithVisitRecord(String firstName, String lastName, String birthDay,
      VisitRecord record, Clinic clinic) throws IOException {
    for (int i = 0; i < clinic.getClinicArchives().size(); i++) {
      if (clinic.getClinicArchives().get(i).getFirstName().contains(firstName)
          && clinic.getClinicArchives().get(i).getLastName().contains(lastName)) {
        this.out.append("Welcome Back! " + firstName);
        this.out.append("\nYou have been placed in the Waiting Room!");
        clinic.getClinicArchives().get(i).setActive(true);
        // clinic.getClinicArchives().get(i).getRecordHistory().add(record);
        clinic.getClinicClients().add(clinic.getClinicArchives().get(i));

        return clinic.getClinicArchives().get(i);
      }
    }
    Registration registration = new Registration(firstName, lastName, birthDay);
    Client client = clinic.registerClient(registration, record);
    client.setRecord(record);
    this.out.append("Welcome to the clinic " + firstName + " " + lastName
        + " \nYou have been placed in the Waiting Room!\n");
    return client;

  }

  /**
   * This method registers an existing client in the clinic along with their visit record.
   *
   * @param client The existing client to be registered.
   * @param clinic The clinic object where the client and visit record will be associated.
   * @param record The visit record to be associated with the client.
   * @return The registered client with the provided visit record.
   */
  @Override
  public Client registerExistingClientWithVisitRecord(Client client, Clinic clinic,
      VisitRecord record) {
    client.setRecord(record);
    return null;
  }

  /**
   * This method registers a new staff member in the clinic by adding them to the list of clinic
   * staff.
   *
   * @param staff The staff member to be registered.
   * @param clinic The clinic object where the staff member will be added.
   * @return The registered staff member.
   */
  @Override
  public Staff registerNewStaff(Staff staff, Clinic clinic) {

    clinic.addClinicStaff(staff);
    return staff;
  }

  /**
   * This method generates and returns a comprehensive display of information about every room in
   * the clinic, including details about the individuals present in each room. The information is
   * collected from the clinic's data and is formatted into a string for further use or output.
   *
   * @param clinic The clinic object containing room and occupancy information.
   * @return A string containing detailed information about every room and its occupants.
   */
  @Override
  public String displayAllInfo(Clinic clinic) {
    String res = "";
    // Every room and who's in each room.
    for (Map.Entry<Integer, int[]> entry : clinic.getRoomKey().entrySet()) {
      res += "Room " + Integer.toString(entry.getKey()) + "\n";
      for (int i = 0; i < clinic.getClinicRooms().size(); i++) {
        if (entry.getKey() - 1 == i) {
          int[] temp = new int[4];
          temp = entry.getValue();
          for (int j = 0; j < clinic.getClinicRooms().size(); j++) {
            if (Arrays.equals(clinic.getClinicRooms().get(j).getId(), temp)) {
              res += displayRoomInfo(clinic.getClinicRooms().get(j), clinic);
            }
          }
        }
      }
    }
    return res;
  }

  /**
   * This method removes the patient from the clinic.
   * 
   * @param client is the patient that is being removed.
   * @param staff is the staff member that approved of this.
   * @param clinic is the clinic that they are located in.
   */

  public void sendPatientHome(Client client, Staff staff, Clinic clinic) {
    clinic.removeClinicClient(client, staff);
  }

  /**
   * This method removes the patient from the clinic.
   * 
   * @param client is the patient that is being moved.
   * @param room is the room that they are being reassigned to.
   * @param clinic is the clinic that they are located in.
   */
  @Override
  public void assignPatientToNewRoom(Room room, Client client, Clinic clinic) {
    clinic.assignClientToNewRoom(client, room);
  }

  /**
   * Method assigns staff to patient.
   * 
   * @param clinic is the clinic that they are located in.
   * @param staff is the staff that is being assigned to the patient.
   * @param client is the patient that the staff is being assigned to.
   * @return true if it was successful, but false if they are already assigned to each other.
   */

  public boolean assignStaffToPatient(Clinic clinic, Staff staff, Client client)
      throws IOException {

    if (!clinic.assignStaffToClient(staff, client)) {
      this.out.append(staff.getFirstName() + " " + staff.getLastName() + " is already assigned to "
          + client.getFirstName() + " " + client.getLastName() + "\n");
      return false;
    }
    return true;
  }

  /**
   * This method plays the game.
   * 
   * @param clinic is the clinic that the game is taking place in.
   * @param input is the file that is being read for the game.
   */

  public void playNewGame(Clinic clinic, File input) {
    if (clinic == null) {
      throw new IllegalArgumentException("Clinic object cannot be null");
    }
    clinic.readDataFromFile(input);
    displayGame(clinic);
  }

  /**
   * This method is the functionality of the game that reads the file for the data.
   * 
   * @param clinic is the clinic that it's going to be used.
   * @param input is file that it's going to read.
   */

  public void playGame(Clinic clinic, File input) {
    if (clinic == null) {
      throw new IllegalArgumentException("Clinic object cannot be null");
    }
    clinic.readDataFromFile(input);
  }

  /**
   * This method handles the display of information for all staff members in the clinic, including
   * their names, assigned clients, and the total number of patients assigned. The information is
   * obtained from the clinic's data and is printed to the output stream in a formatted manner.
   *
   * @param clinic The clinic object containing staff and client information.
   * @throws IOException If an I/O error occurs while interacting with the input/output stream.
   */
  @Override
  public void handleViewAllStaffMembers(Clinic clinic) throws IOException {
    try {
      this.out
          .append("--------------------------------------------------------------------------\n");
      for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
        this.out.append("Staff: " + entry.getKey().getFirstName() + " "
            + entry.getKey().getLastName() + "\nClients assigned: ");
        for (Client c : entry.getValue()) {
          this.out.append("\n " + c.getLastName() + ", " + c.getFirstName());
        }
        this.out.append(
            "\nTotal number of assigned patients ever: " + entry.getKey().getNumPatients() + "\n");

      }
      this.out
          .append("--------------------------------------------------------------------------\n");
    } catch (IllegalArgumentException e) {
      this.out.append("Error: " + e.getMessage() + ", please try again.");
    }

  }

  /**
   * This method handles the retrieval and display of a list of patients who have not visited the
   * clinic for more than 365 days. The list is obtained from the clinic's information, and the
   * result is printed to the output stream. If there are no such patients, a corresponding message
   * is displayed.
   *
   * @param clinic The clinic object containing patient information and visit history.
   * @throws IOException If an I/O error occurs while interacting with the input/output stream.
   */
  @Override
  public void handleYearNoVisit(Clinic clinic) throws IOException {
    String list = clinic.getNoYearVisitList();
    try {
      if (list.length() == 0) {
        this.out.append("There are no patients that haven't visited the clinic for more than "
            + "365 days from today.\n");
      } else {
        this.out.append(list);
      }
    } catch (IllegalArgumentException e) {
      this.out.append("Error: " + e.getMessage() + ", please try again.");
    }
  }

  /**
   * This method generates a visual representation of the clinic layout, including room rectangles
   * and associated information. The resulting image is saved as a PNG file named
   * "clinic_representation.png". The layout includes the clinic name and individual room details.
   * The image is created using a BufferedImage with specified dimensions and graphics operations.
   *
   * @param clinic The clinic object containing information about rooms and layout details.
   */
  @Override
  public void displayGame(Clinic clinic) {
    showRoomMap(clinic);

  }

  private JMenuBar createMenuBar(Clinic clinic) {

    JMenuItem aboutItem = new JMenuItem("About Clinic");
    aboutItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showAboutDialog(clinic);
      }
    });

    JMenuItem roomMapItem = new JMenuItem("Show Room Map");
    roomMapItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showRoomMap(clinic);
      }
    });

    JMenuItem clearMap = new JMenuItem("Clear Room Map");
    clearMap.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        clearRoom(clinic);
      }

    });
    
    JMenuItem loadFileItem = new JMenuItem("Load Clinic File");
    loadFileItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loadClinicFile();
      }
    });

    JMenuItem saveLivesItem1 = new JMenuItem("Register Patient");
    // JMenuItem SaveLivesItem2 = new JMenuItem("Move Patient");
    JMenuItem saveLivesItem3 = new JMenuItem("Assign Staff To Patient");
    JMenuItem saveLivesItem4 = new JMenuItem("Display Patient Info");

    saveLivesItem1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleAddPatient(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    /*
     * SaveLivesItem2.addActionListener(new ActionListener() {
     * 
     * @Override public void actionPerformed(ActionEvent e) { try {
     * handleAssignPatientToRoom(clinic); } catch (IOException ioe) { throw new
     * IllegalStateException("Error: " + ioe.getMessage(), ioe); } } });
     */
    saveLivesItem3.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleAssignStaffToClient(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    saveLivesItem4.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleViewPatientRecordHistory(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    JMenuItem saveLivesItem5 = new JMenuItem("Unassign Staff To Patient");
    saveLivesItem5.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleUnassignStaffFromClient(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    JMenuItem saveLivesItem6 = new JMenuItem("Discharge Patient");
    saveLivesItem6.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleRemovePatient(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    JMenu roomMapMenu = new JMenu("Room Map");
    JMenu aboutMenu = new JMenu("About");
    aboutMenu.add(aboutItem);
    roomMapMenu.add(roomMapItem);
    roomMapMenu.add(clearMap);
    roomMapMenu.add(loadFileItem);
    JMenu saveLivesMenu = new JMenu("Save Lives");
    saveLivesMenu.add(saveLivesItem1);
    saveLivesMenu.add(saveLivesItem3);
    saveLivesMenu.add(saveLivesItem4);
    saveLivesMenu.add(saveLivesItem5);
    saveLivesMenu.add(saveLivesItem6);
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(aboutMenu);
    menuBar.add(roomMapMenu);
    menuBar.add(saveLivesMenu);
    return menuBar;
  }

  private void clearRoom(Clinic clinic) {

    // Remove clinical patients and Remove visit records from everybody
    clinic.getClinicClients().clear();
    for (Client client : clinic.getClinicClients()) {
      client.getRecordHistory().clear();
    }

    // Remove clinical staff members
    clinic.getClinicStaffs().clear();

    // Remove clinical rooms
    clinic.getClinicRooms().clear();

  }
  
  private static void loadClinicFile() {
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showOpenDialog(frame);

    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      Clinic clinic = new Clinic("Cybernetic Implant Clinic"); 
      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;
      new ClinicConsoleController(input, output).playNewGame(clinic, selectedFile);
    }

  }

  private void showAboutDialog(Clinic clinic) {
    JOptionPane.showMessageDialog(frame,
        "Clinic Name: " + clinic.getName() + "\nNumber of patients: "
            + clinic.getClinicClients().size() + "\nNumber of Clinical Staff Members: "
            + clinic.getClinicStaffs().size() + "\nNumber of Rooms: "
            + clinic.getClinicRooms().size(),
        "About Clinic", JOptionPane.INFORMATION_MESSAGE);

  }

  private void showRoomMap(Clinic clinic) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Scrollable Rectangle Panel Example");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Create menu bar and add it to the frame
      JMenuBar menuBar = createMenuBar(clinic);
      frame.setJMenuBar(menuBar);

      RectanglePanel rectanglePanel = new RectanglePanel(clinic);
      rectanglePanel.setPreferredSize(new Dimension(1500, 3000)); // Adjust the size as needed

      JScrollPane scrollPane = new JScrollPane(rectanglePanel);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      frame.add(scrollPane);
      frame.setSize(5000, 5000);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);

      scrollPane.revalidate();
      scrollPane.repaint();
    });
  }

  /**
   * This method handles the unassignment of a staff member from a client in the clinic. It prompts
   * the user to enter the first name and last name of the client and then displays a list of staff
   * members assigned to that client. The user is then prompted to enter the first name and last
   * name of the staff member to be unassigned from the client. The unassignment is performed if the
   * staff member is found and is approved to discharge patients.
   *
   * @param clinic The clinic object containing client and staff information.
   * @throws IOException If an I/O error occurs while interacting with the input/output stream.
   */
  @Override
  public void handleUnassignStaffFromClient(Clinic clinic) throws IOException {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new UnassignStaffToPatient(clinic);
      }
    });

  }

  /**
   * This method handles the removal of a staff member from the clinic. It prompts the user to enter
   * the first name and last name of the staff member they want to remove. If the staff member is
   * found, they are removed from the clinic. The user has the option to remove another staff
   * member.
   *
   * @param clinic The clinic object from which the staff member is to be removed.
   * @throws IOException If an I/O error occurs while interacting with the input/output stream.
   */
  @Override
  public void handleRemoveStaff(Clinic clinic) throws IOException {
    try {
      Staff dischargeStaff = null;
      String disChargeStaffFirstName = "";
      String disChargeStaffLastName = "";
      while (dischargeStaff == null) {
        this.out.append("Please enter the First Name of the Staff Member you want to remove:");
        disChargeStaffFirstName = getValidNameInput();

        this.out.append("Please enter the Last Name of the Staff Member you want to remove:");
        disChargeStaffLastName = getValidNameInput();

        for (Staff staff : clinic.getClinicStaffs()) {
          if (staff.getFirstName().contains(disChargeStaffFirstName)
              && staff.getLastName().contains(disChargeStaffLastName)) {
            dischargeStaff = staff;
            break;
          }
        }

        if (dischargeStaff == null) {
          this.out.append("Staff doesn't exist." + " Do you want to try again? (yes/no)");
          String tryAgain = scan.next();
          if (!("yes".equalsIgnoreCase(tryAgain))) {
            return; // Return without removing a patient
          }
        }

        clinic.removeClinicStaff(dischargeStaff);
        this.out.append(dischargeStaff.getFirstName() + " has been removed from the clinic."
            + "\n Would you like to remove another clinical staff member?\n");
        String tryAgain = scan.next();
        if (!("yes".equalsIgnoreCase(tryAgain))) {
          return; // Return without removing a patient
        }

      }
    } catch (NullPointerException e) {
      this.out.append("Error: " + e.getMessage() + ". Please try again.\n");
    } catch (IllegalArgumentException e) {
      this.out.append("Error: " + e.getMessage() + ". Please try again.\n");
    }

  }

  /**
   * This method handles the view of staff information in the clinic. It displays a summary of each
   * staff member, including their name, assigned clients, and the total number of assigned patients
   * ever.
   *
   * @param clinic The clinic object containing the staff information.
   * @throws IOException If an I/O error occurs while writing to the output stream.
   */
  @Override
  public void handleViewStaff(Clinic clinic) throws IOException {
    this.out.append("--------------------------------------------------------------------------\n");
    int count = 0;
    int numStaff = 0;
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      numStaff++;
      if (entry.getValue().size() != 0) {
        this.out.append("Staff: " + entry.getKey().getFirstName() + " "
            + entry.getKey().getLastName() + "\nClients assigned: ");
        for (Client c : entry.getValue()) {
          this.out.append("\n " + c.getLastName() + ", " + c.getFirstName());
        }
        this.out.append(
            "\nTotal number of assigned patients ever: " + entry.getKey().getNumPatients() + "\n");
      } else {
        count++;
      }
    }
    if (count == numStaff) {
      this.out.append("There are no Staff members assigned to any patients at the moment.\n");
    }
    this.out.append("--------------------------------------------------------------------------\n");
  }

  /**
   * This method handles adding a new room.
   * 
   * @param clinic is where this is taking place.
   */

  @Override
  public void handleAddNewRoom(Clinic clinic) throws IOException {
    try {
      String roomName = "";

      String disChargeStaffFirstName = "";
      String disChargeStaffLastName = "";

      int[] roomId = new int[4];

      this.out.append("Please enter the Name of the new room:\n");
      roomName = getValidNameInput();
      String roomType = "";
      this.out.append("Please enter the type room this is going to be:\n");
      roomType = getValidNameInput();

      for (int i = 0; i < 4; i++) {
        roomId[i] = clinic.getClinicRooms().size();
      }
      boolean secondCheck = false;
      while (!secondCheck) {
        this.out.append("Please enter the First Name of the Staff who approved this:");
        disChargeStaffFirstName = getValidNameInput();

        this.out.append("Please enter the Last Name of the Staff who approved this:");
        disChargeStaffLastName = getValidNameInput();

        for (Staff staff : clinic.getClinicStaffs()) {
          if (staff.getFirstName().contains(disChargeStaffFirstName)
              && staff.getLastName().contains(disChargeStaffLastName)) {
            secondCheck = true;
            break;
          }
        }

        if (!secondCheck) {
          this.out
              .append("Staff doesn't exist or is not approved to build new rooms into the clinic."
                  + " Do you want to try again? (yes/no)");
          String tryAgain = scan.next();
          if (!("yes".equalsIgnoreCase(tryAgain))) {
            return; // Return without removing a patient
          }
        }

      }
      Room room = new Room(roomId, roomType, roomName);
      clinic.addClinicRoom(room);

    } catch (IllegalArgumentException e) {
      this.out.append("Error: " + e.getMessage() + ", please try again.");
    }

  }

  /**
   * This method handles viewing the patient record history.
   * 
   * @param clinic is where this is taking place.
   */

  public void handleViewPatientRecordHistory(Clinic clinic) throws IOException {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new ViewPatientInformation(clinic);
      }
    });
  }

  /**
   * method handles adding a patient to the clinic.
   * 
   * @param clinic is where this takes place.
   */

  public void handleAddPatient(Clinic clinic) throws IOException {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new RegisterPatient(clinic);
      }
    });
  }

  /**
   * Method handels adding a staff to the clinic.
   * 
   * @param clinic is where the staff is being added.
   */

  public void handleAddStaff(Clinic clinic) throws IOException {
    try {
      this.out.append("Please enter the First Name of the Staff member:");
      String staffFirstName = getValidNameInput();

      this.out.append("Please enter the Last Name of the Staff member:");
      String staffLastName = getValidNameInput();

      String staffOccupation = "";
      boolean validOccupation = false;

      while (!validOccupation) {
        this.out.append("Please enter the occupation of the Staff member\n");
        this.out.append("Note that they can only be a physician, nurse, or reception:");
        staffOccupation = scan.next();

        if ("nurse".equalsIgnoreCase(staffOccupation)
            || "physician".equalsIgnoreCase(staffOccupation)
            || "reception".equalsIgnoreCase(staffOccupation)) {
          validOccupation = true;
        }

      }

      // Create a new staff member
      Staff staff = new Staff(staffOccupation, staffFirstName, staffLastName);
      if (clinic.getClinicStaffs().contains(staff)) {
        this.out.append("This staff member already exists");
        return;
      }
      // Register the new staff member with the clinic
      registerNewStaff(staff, clinic);

      this.out.append("Here's the status of the staff member you just added:" + staff + "\n");

    } catch (IllegalArgumentException e) {
      this.out.append("Error: " + e.getMessage() + ", please try again.");
    }
  }

  /**
   * Method removes patient from the clinic object.
   * 
   * @param clinic is where the patient will be removed from.
   * @throws IOException if something isn't inputted correctly.
   */

  public void handleRemovePatient(Clinic clinic) throws IOException {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new OptionWindow(clinic);
      }
    });

  }

  /**
   * Method assigns staff to patient.
   * 
   * @param clinic is where the staff is being assigned.
   */

  public void handleAssignStaffToClient(Clinic clinic) throws IOException {

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new AssignStaffToPatient(clinic);
      }
    });
  }

  /**
   * Method handles asssigning patients to new rooms.
   * 
   * @param clinic is where the rooms are located.
   */

  public void handleAssignPatientToRoom(Clinic clinic) throws IOException {
    boolean tryAgain = true;

    while (tryAgain) {
      try {
        Client patient = null;
        String firstName = "";

        this.out.append("Please enter the First Name of the patient:\n");
        firstName = getValidNameInput();
        this.out.append("Please enter the Last Name of the patient:\n");
        String lastName = "";
        lastName = getValidNameInput();
        boolean patientFound = false;
        for (Client client : clinic.getClinicClients()) {
          if (client.getFirstName().contains(firstName)
              && client.getLastName().contains(lastName)) {
            patient = client;
            patientFound = true;
            break;
          }
        }

        if (!patientFound) {
          this.out.append("Patient doesn't exist. Do you want to try again? (yes/no)\n");
          String tryAgainResponse = getValidNameInput();
          if (!"yes".equalsIgnoreCase(tryAgainResponse)) {
            tryAgain = false;
            continue; // Skip to the end of the loop and quit
          }
          // If user wants to try again, continue the loop
          continue;
        }

        boolean roomFound = false;
        Room newRoom = null;
        this.out.append("Here are the rooms in the clinic:\n");
        for (Room room : clinic.getClinicRooms()) {
          this.out.append(room.getRoomName() + "\n");
        }

        this.out.append(
            "Please enter the Name of the Room you want " + firstName + " to be reassigned to:\n");
        String roomName = getValidNameInput();
        for (Room room : clinic.getClinicRooms()) {
          if (room.getRoomName().contains(roomName)) {
            newRoom = room;
            roomFound = true;
            break;
          }
        }

        if (!roomFound) {
          this.out.append(roomName + " doesn't exist. Do you want to try again? (yes/no)\n");
          String tryAgainResponse = getValidNameInput();
          if (!"yes".equalsIgnoreCase(tryAgainResponse)) {
            tryAgain = false;
            continue; // Skip to the end of the loop and quit
          }
          // If user wants to try again, continue the loop
          continue;
        }

        clinic.assignClientToNewRoom(patient, newRoom);

        this.out.append(
            firstName + ' ' + lastName + " has successfully been reassigned to the room.\n");

        // Ask if user wants to try again
        this.out.append("Do you want to assign another patient to a room? (yes/no)\n");
        String tryAgainResponse = getValidNameInput();
        if (!"yes".equalsIgnoreCase(tryAgainResponse)) {
          tryAgain = false;
        }
      } catch (IllegalArgumentException e) {
        this.out.append("Error: " + e.getMessage() + ", please choose a vacant room.\n");
        this.out.append("Do you want to try again? (yes/no)\n");
        String tryAgainResponse = getValidNameInput();
        if (!"yes".equalsIgnoreCase(tryAgainResponse)) {
          tryAgain = false;
        }
      }
    }
  }

  /**
   * Handles the display of room information for a given Clinic.
   *
   * This method takes a Clinic object as input and is responsible for displaying information about
   * the rooms in the clinic. The information could include details about room availability, room
   * numbers, and any other relevant data related to the rooms. The specific implementation of how
   * the room information is displayed may vary depending on the application.
   *
   * @param clinic The Clinic object for which room information should be displayed.
   * @throws IOException If an I/O error occurs while handling the room information.
   */

  public void handleDisplayAllInfo(Clinic clinic) throws IOException {
    try {
      this.out.append(displayAllInfo(clinic));
    } catch (IOException e) {
      this.out.append("whatever");
    }
  }

  /**
   * Handles the display of room information for a given Clinic.
   *
   * This method takes a Clinic object as input and is responsible for displaying information about
   * the rooms in the clinic. The information could include details about room availability, room
   * numbers, and any other relevant data related to the rooms. The specific implementation of how
   * the room information is displayed may vary depending on the application.
   *
   * @param clinic The Clinic object for which room information should be displayed.
   * @throws IOException If an I/O error occurs while handling the room information.
   */
  public void handleDisplayRoomInfo(Clinic clinic) throws IOException {
    boolean displayAnotherRoom = true;
    // scan.nextLine(); // Consume the newline character after user input

    while (displayAnotherRoom) {
      boolean roomFound = false;
      Room roomInfo = null;
      String roomName = null;

      this.out.append("Here are the rooms in the clinic:\n");
      for (Room room : clinic.getClinicRooms()) {
        this.out.append(room.getRoomName() + "\n");
      }

      while (!roomFound) {
        this.out.append("Please enter the name of the room you would like to see:\n");
        roomName = getValidNameInput(); // Use nextLine() to read the whole line

        for (Room room : clinic.getClinicRooms()) {
          if (room.getRoomName().contains(roomName)) {
            roomInfo = room;
            roomFound = true;
            break; // Found the room, no need to continue the loop
          }
        }

        if (!roomFound) {
          this.out.append("Room not found. Would you like to try again? (yes/no)\n");
          String tryAgain = scan.nextLine(); // Use nextLine() to read the whole line

          if (!"yes".equalsIgnoreCase(tryAgain)) {
            // User doesn't want to try again, exit the loop
            return;
          }
        }
      }

      // Display room information
      this.out.append("Room Information:\n");
      this.out.append(roomName + "\n" + displayRoomInfo(roomInfo, clinic) + "\n");
      // Display other room information as needed

      this.out.append("Do you want to see another room? (yes/no)\n");
      String seeAnotherRoom = getValidNameInput(); // Use nextLine() to read the whole line
      displayAnotherRoom = "yes".equalsIgnoreCase(seeAnotherRoom);
    }
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

}
