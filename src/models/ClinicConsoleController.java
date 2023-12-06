
package models;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
  private static final long serialVersionUID = 1L;
  private final Appendable out;
  private final Scanner scan;
  private JFrame frame;

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

  public void playGame(Clinic clinic, File input) {
    if (clinic == null) {
      throw new IllegalArgumentException("Clinic object cannot be null");
    }
    clinic.readDataFromFile(input);
//    while (!quitGame) {
//      displayGame(clinic);
//
//      try {
//        this.out.append(displayAllInfo(clinic));
//        this.out.append("Please choose what you want to do:\n");
//        this.out.append("1: Add Patient\n");
//        this.out.append("2: Add Staff\n");
//        this.out.append("3: Discharge Patient\n");
//        this.out.append("4: Assign Staff To Client\n");
//        this.out.append("5: Assign Patient To A New Room\n");
//        this.out.append("6: Add A New Room\n");
//        this.out.append("7: Show me All Data\n");
//        this.out.append("8: Show me Data About A Specific Room\n");
//        this.out.append("9: Show me the Medical History of a patient\n");
//        this.out.append("10: Show List of Staff members with their patients\n");
//        this.out.append("11: Remove Clinical Staff Member\n");
//        this.out.append("12: Unassign Clinical Staff Member From Patient\n");
//        this.out.append("13: Show the List Of Patients Who Have Not Visited For Over A Year\n");
//        this.out.append("14: Show the List of Patients Who Have Visit The Clinic At Least Twice"
//            + " in the last 365 days\n");
//        this.out.append("15: Show the List of All Staff Members\n");
//        this.out.append("16: Quit\n");
//        int userInput;
//        // userInput = scan.nextInt();
//        userInput = scan.nextInt();
//        if (userInput > 16 || userInput < 1) {
//          throw new IllegalArgumentException("That was not a valid option");
//        }
//        // Handle user input based on the selected option
//        switch (userInput) {
//          case 1:
//            handleAddPatient(clinic);
//            break;
//          case 2:
//            handleAddStaff(clinic);
//            break;
//          case 3:
//            handleRemovePatient(clinic);
//            break;
//          case 4:
//            handleAssignStaffToClient(clinic);
//            break;
//          case 5:
//            handleAssignPatientToRoom(clinic);
//            break;
//          case 6:
//            handleAddNewRoom(clinic);
//            break;
//          case 7:
//            handleDisplayAllInfo(clinic);
//            break;
//
//          case 8:
//            handleDisplayRoomInfo(clinic);
//            break;
//
//          case 9:
//            handleViewPatientRecordHistory(clinic);
//            break;
//
//          case 10:
//            handleViewStaff(clinic);
//            break;
//
//          case 11:
//            handleRemoveStaff(clinic);
//            break;
//
//          case 12:
//            handleUnassignStaffFromClient(clinic);
//            break;
//
//          case 13:
//            handleYearNoVisit(clinic);
//            break;
//
//          case 14:
//            handleTwoVisitYear(clinic);
//            break;
//
//          case 15:
//            handleViewAllStaffMembers(clinic);
//            break;
//
//          case 16:
//            quitGame = true;
//            this.out.append("Game over, thanks for playing");
//            break;
//
//          default:
//            continue;
//        }
//
//      } catch (NoSuchElementException | IOException | IllegalArgumentException ioe) {
//        throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
//      }
//    }

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

//    frame = new JFrame("Clinic Layout");
//    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//    JMenuBar menuBar = createMenuBar(clinic);
//    frame.setJMenuBar(menuBar);
//
//    JPanel contentPane = new JPanel();
//    contentPane.setLayout(new BorderLayout());
//
//    frame.setContentPane(contentPane);
//
//    frame.setSize(1000, 1000);
//    frame.setLocationRelativeTo(null);
//    frame.setVisible(true);
    showRoomMap(clinic);

  }

  private JMenuBar createMenuBar(Clinic clinic) {
    JMenuBar menuBar = new JMenuBar();

    JMenu aboutMenu = new JMenu("About");
    JMenuItem aboutItem = new JMenuItem("About Clinic");
    aboutItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showAboutDialog(clinic);
      }
    });

    JMenu roomMapMenu = new JMenu("Room Map");
    JMenuItem roomMapItem = new JMenuItem("Show Room Map");
    roomMapItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showRoomMap(clinic);
      }
    });

    JMenu SaveLivesMenu = new JMenu("Save Lives");
    JMenuItem SaveLivesItem1 = new JMenuItem("Register Patient");
    JMenuItem SaveLivesItem2 = new JMenuItem("Move Patient");
    JMenuItem SaveLivesItem3 = new JMenuItem("Assign Staff To Patient");
    JMenuItem SaveLivesItem4 = new JMenuItem("Display Patient Info");
    JMenuItem SaveLivesItem5 = new JMenuItem("Unassign Staff To Patient");
    JMenuItem SaveLivesItem6 = new JMenuItem("Discharge Patient");

    SaveLivesItem1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleAddPatient(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    SaveLivesItem2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleAssignPatientToRoom(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    SaveLivesItem3.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleAssignStaffToClient(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    SaveLivesItem4.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleViewPatientRecordHistory(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    SaveLivesItem5.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleUnassignStaffFromClient(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });

    SaveLivesItem6.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleRemovePatient(clinic);
        } catch (IOException ioe) {
          throw new IllegalStateException("Error: " + ioe.getMessage(), ioe);
        }
      }
    });
    
    aboutMenu.add(aboutItem);
    roomMapMenu.add(roomMapItem);
    SaveLivesMenu.add(SaveLivesItem1);
    SaveLivesMenu.add(SaveLivesItem2);
    SaveLivesMenu.add(SaveLivesItem3);
    SaveLivesMenu.add(SaveLivesItem4);
    SaveLivesMenu.add(SaveLivesItem5);
    SaveLivesMenu.add(SaveLivesItem6);
    menuBar.add(aboutMenu);
    menuBar.add(roomMapMenu);
    menuBar.add(SaveLivesMenu);
    return menuBar;
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
      JFrame frame = new JFrame(clinic.getName());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Create menu bar and add it to the frame
      JMenuBar menuBar = createMenuBar(clinic);
      frame.setJMenuBar(menuBar);

      RectanglePanel rectanglePanel = new RectanglePanel(clinic);
      rectanglePanel.setPreferredSize(new Dimension(2000, 4000)); // Adjust the size as needed

      JScrollPane scrollPane = new JScrollPane(rectanglePanel);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      frame.add(scrollPane);
      frame.setSize(1000, 1000);
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
    try {
      boolean firstCheck = false;
      Client patient = null;
      String dischargeFirstName = "";
      String dischargeLastName = "";
      String disChargeStaffFirstName = "";
      String disChargeStaffLastName = "";
      int count = 0;

      while (!firstCheck) {
        this.out.append("Please enter the First Name of the patient's First Name: ");
        dischargeFirstName = getValidNameInput();

        this.out.append("Please enter the Last Name of the patient's Last Name: ");
        dischargeLastName = getValidNameInput();

        for (Client client : clinic.getClinicClients()) {
          if (client.getFirstName().contains(dischargeFirstName)
              && client.getLastName().contains(dischargeLastName)) {
            patient = client;
            firstCheck = true;
            break;
          }
        }

        if (!firstCheck) {
          this.out.append("Patient doesn't exist. Do you want to try again? (yes/no)");
          String tryAgain = scan.next();
          if (!"yes".equalsIgnoreCase(tryAgain)) {
            return; // Return without removing a patient
          }
        }
      }

      this.out
          .append("Below is the list of staff members assigned to " + dischargeFirstName + ":\n");

      for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
        for (int i = 0; i < entry.getValue().size(); i++) {
          if (entry.getValue().contains(patient)) {
            count++;
            this.out
                .append(entry.getKey().getFirstName() + " " + entry.getKey().getLastName() + "\n");
          }
        }
      }

      if (count == 0) {
        this.out.append("This patient does not have any staff members assigned to them."
            + "\nWould you like to try another patient? (yes/no)");
        String tryAgain = getValidNameInput();
        if (!"yes".equalsIgnoreCase(tryAgain)) {
          return; // Return without removing a patient
        } else {
          handleUnassignStaffFromClient(clinic);
        }
      }

      boolean secondCheck = false;

      while (!secondCheck) {
        this.out.append("Please enter the First Name of the staff member:");
        disChargeStaffFirstName = getValidNameInput();

        this.out.append("Please enter the Last Name of the Staff who approved this:");
        disChargeStaffLastName = getValidNameInput();

        for (Staff staff : clinic.getClinicStaffs()) {
          if (staff.getFirstName().contains(disChargeStaffFirstName)
              && staff.getLastName().contains(disChargeStaffLastName)) {
            secondCheck = true;
            clinic.removeStaffFromClient(staff, patient);
          }
        }

        if (!secondCheck) {
          this.out.append("Staff doesn't exist or is not approved to discharge patients."
              + " Do you want to try again? (yes/no)");
          String tryAgain = getValidNameInput();
          if (!"yes".equalsIgnoreCase(tryAgain)) {
            return; // Return without removing a patient
          }
        }
      }
      this.out.append(disChargeStaffFirstName + " has been unassigned to " + dischargeFirstName
          + ".\n would you like to remove another clinical staff member from a patient? (yes/no)");
      String tryAgain = getValidNameInput();
      if (!"yes".equalsIgnoreCase(tryAgain)) {
        return; // Return without removing a patient
      } else {
        handleUnassignStaffFromClient(clinic);
      }
    } catch (IllegalArgumentException e) {
      this.out.append("Error: " + e.getMessage() + ", please try again.");
    }
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
    Scanner scan = new Scanner(System.in);

    boolean checkAnotherPatient = true;

    while (checkAnotherPatient) {
      try {
        boolean firstCheck = false;
        Client patient = null;
        String firstName = "";
        String lastName = "";

        while (!firstCheck) {
          this.out.append("Please enter the First Name of the patient:\n");
          firstName = getValidNameInput();

          this.out.append("Please enter the Last Name of the patient:\n");
          lastName = getValidNameInput();

          for (Client client : clinic.getClinicClients()) {
            if (client.getFirstName().contains(firstName)
                && client.getLastName().contains(lastName)) {
              patient = client;
              firstCheck = true;
              break;
            }
          }

          if (!firstCheck) {
            this.out.append("Patient doesn't exist. Do you want to try again? (yes/no)");
            String tryAgain = getValidNameInput();
            if (!"yes".equalsIgnoreCase(tryAgain)) {
              return; // Return without removing a patient
            }
          }
        }

        for (int i = 0; i < patient.getRecordHistory().size(); i++) {
          this.out.append(patient.getRecordHistory().get(i).toString());
        }
        if (patient.getRecord() == null) {
          this.out.append(patient.getFirstName() + ' ' + patient.getLastName()
              + " does not have a medical record with us.\n");
        }
        boolean isOut = false;
        while (!isOut) {
          this.out.append("Do you want to check another patient's clinical record? (yes/no)");
          String checkAnother = getValidNameInput();
          if ("yes".equalsIgnoreCase(checkAnother)) {
            handleViewPatientRecordHistory(clinic);
          } else if ("no".equalsIgnoreCase(checkAnother)) {
            return;
          } else {
            this.out.append("That was not a valid response!\n");
          }
        }
        scan.close();
      } catch (NullPointerException e) {
        this.out.append("Error: " + e.getMessage() + ". Please try again.\n");
      } catch (IllegalArgumentException e) {
        this.out.append("Error: " + e.getMessage() + ". Please try again.\n");
      }
    }

  }

  /**
   * method handles adding a patient to the clinic.
   * 
   * @param clinic is where this takes place.
   */

  public void handleAddPatient(Clinic clinic) throws IOException {

    //    boolean registerAnotherPatient = true;
//
//    while (registerAnotherPatient) {
//      try {
//        this.out.append("Please enter the First Name of the client:");
//        String firstName = getValidNameInput();
//        this.out.append("Please enter the Last Name of the client:");
//        String lastName = getValidNameInput();
//        this.out
//            .append("Please enter the Date Of Birth of the Client in this format (MM/DD/YYYY):");
//        String birthday = getValidDateInput();
//
//        this.out.append("What symptoms does the patient have?");
//        String symptoms = getValidNameInput();
//
//        BigDecimal temp = null;
//        try {
//          this.out.append("And can you tell me what their body temperature is in celsius?\n");
//          temp = getValidBodyTemperature();
//        } catch (InputMismatchException e) {
//          this.out
//              .append("Error: Invalid input for body temperature. Please enter a valid number.\n");
//          // Consume the invalid input
//          // Add a recursive call to prompt the user to try again
//          temp = scan.nextBigDecimal();
//        }
//        boolean isValid = false;
//        for (int i = 0; i < clinic.getClinicClients().size(); i++) {
//          if (clinic.getClinicClients().get(i).getFirstName().equals(firstName)
//              && clinic.getClinicClients().get(i).getLastName().equals(lastName)) {
//            this.out.append("This patient is already in the clinic.");
//            isValid = true;
//          }
//        }
//
//        if (!isValid) {
//          LocalDateTime now = LocalDateTime.now();
//          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
//          String formattedDateTime = now.format(formatter);
//          VisitRecord record = new VisitRecord(formattedDateTime, symptoms, temp);
//
//          Client client = registerClientWithVisitRecord(firstName, lastName, birthday, record,
//              clinic);
//          client.setRecord(record);
//          client.getRecordHistory().add(record);
//        }
//
//        // this.out.append("Welcome to the clinic " + firstName + " " + lastName + "\nYou have been
//        // placed in the Waiting Room!\n");
//
//        this.out.append("Do you want to register another patient? (yes/no)\n");
//        // Consume the newline
//        String registerAnother = getValidNameInput();
//        if (!"yes".equalsIgnoreCase(registerAnother)) {
//          registerAnotherPatient = false;
//        }
//
//      } catch (IllegalArgumentException | IllegalStateException e) {
//        this.out.append("Error: " + e.getMessage() + ", please try again.\n");
//      }
//    }
    SwingUtilities.invokeLater(() -> {
      RegisterPatient registerPatientFrame = new RegisterPatient(clinic);

  });
    showRoomMap(clinic);


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
    showRoomMap(clinic);
//    try {
//      boolean firstCheck = false;
//      Client patient = null;
//      String dischargeFirstName = "";
//      String dischargeLastName = "";
//      String disChargeStaffFirstName = "";
//      String disChargeStaffLastName = "";
//
//      while (!firstCheck) {
//        this.out.append("Please enter the First Name of the patient you wish to discharge:");
//        dischargeFirstName = getValidNameInput();
//
//        this.out.append("Please enter the Last Name of the patient you wish to discharge:");
//        dischargeLastName = getValidNameInput();
//
//        for (Client client : clinic.getClinicClients()) {
//          if (client.getFirstName().contains(dischargeFirstName)
//              && client.getLastName().contains(dischargeLastName)) {
//            patient = client;
//            firstCheck = true;
//            break;
//          }
//        }
//
//        if (!firstCheck) {
//          this.out.append("Patient doesn't exist. Do you want to try again? (yes/no)");
//          String tryAgain = scan.next();
//          if (!"yes".equalsIgnoreCase(tryAgain)) {
//            return; // Return without removing a patient
//          }
//        }
//      }
//
//      boolean secondCheck = false;
//      Staff staffApproval = null;
//
//      while (!secondCheck) {
//        this.out.append("Please enter the First Name of the Staff who approved this:");
//        disChargeStaffFirstName = getValidNameInput();
//
//        this.out.append("Please enter the Last Name of the Staff who approved this:");
//        disChargeStaffLastName = getValidNameInput();
//
//        for (Staff staff : clinic.getClinicStaffs()) {
//          if (staff.getFirstName().contains(disChargeStaffFirstName)
//              && staff.getLastName().contains(disChargeStaffLastName)) {
//            if (staff.getFirstName().contains("Dr.")) {
//              staffApproval = staff;
//              secondCheck = true;
//              break;
//            }
//          }
//        }
//
//        if (!secondCheck) {
//          this.out.append("Staff doesn't exist or is not approved to discharge patients."
//              + " Do you want to try again? (yes/no)");
//          String tryAgain = getValidNameInput();
//          if (!"yes".equalsIgnoreCase(tryAgain)) {
//            return; // Return without removing a patient
//          }
//        }
//      }
//
//      sendPatientHome(patient, staffApproval, clinic);
//      this.out.append(
//          dischargeFirstName + ' ' + dischargeLastName + " has been discharged, approved by: "
//              + disChargeStaffFirstName + ' ' + disChargeStaffLastName + "\n");
//
//    } catch (IllegalArgumentException e) {
//      this.out.append("Error: " + e.getMessage() + ", please try again.");
//    }
  }

  /**
   * Method assigns staff to patient.
   * 
   * @param clinic is where the staff is being assigned.
   */

  public void handleAssignStaffToClient(Clinic clinic) throws IOException {
    try {
      boolean firstCheck = false;
      Client patient = null;
      String firstName = "";
      String lastName = "";

      while (!firstCheck) {
        this.out.append("Please enter the First Name of the patient:\n");
        firstName = getValidNameInput();

        this.out.append("Please enter the Last Name of the patient:\n");
        lastName = getValidNameInput();

        for (Client client : clinic.getClinicClients()) {
          if (client.getFirstName().contains(firstName)
              && client.getLastName().contains(lastName)) {
            patient = client;
            firstCheck = true;
            break;
          }
        }

        if (!firstCheck) {
          this.out.append("Patient doesn't exist. Do you want to try again? (yes/no)");
          String tryAgain = getValidNameInput();
          if ("no".equalsIgnoreCase(tryAgain)) {
            return; // Return without removing a patient
          }
        }
      }

      boolean secondCheck = false;
      Staff staffApproval = null;
      String staffFirstName = "";
      String staffLastName = "";

      while (!secondCheck) {
        this.out.append(
            "Please enter the First Name of the Staff you want assigned to " + firstName + "\n");
        staffFirstName = getValidNameInput();

        this.out.append(
            "Please enter the Last Name of the Staff you want assigned to " + firstName + "\n");
        staffLastName = getValidNameInput();

        for (Staff staff : clinic.getClinicStaffs()) {
          if (staff.getFirstName().contains(staffFirstName)
              && staff.getLastName().contains(staffLastName)) {
            staffApproval = staff;
            secondCheck = true;
            break;
          }
        }

        if (!secondCheck) {
          this.out.append("Staff doesn't exist. Do you want to try again? (yes/no)");
          String tryAgain = scan.next();
          if (!"yes".equalsIgnoreCase(tryAgain)) {
            return;
          }
        }
      }

      if (assignStaffToPatient(clinic, staffApproval, patient)) {
        this.out.append(staffFirstName + " has been assigned to " + firstName + "\n");
      }

      boolean check = false;
      while (check == false) {
        this.out.append("Do you want to assign staff to another patient? (yes/no)\n");
        String assignAnotherResponse = scan.next();

        if ("yes".equalsIgnoreCase(assignAnotherResponse)) {

          // If the user wants to assign staff to another patient, call the method again recursively
          handleAssignStaffToClient(clinic);
        }
        if ("no".equalsIgnoreCase(assignAnotherResponse)) {

          check = true;
        }
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage() + ", please try again.");
    }
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

  private BigDecimal getValidBodyTemperature() throws IOException {
    BigDecimal temp = null;
    try {
      temp = scan.nextBigDecimal();
    } catch (InputMismatchException e) {
      this.out.append("Error: Invalid input for body temperature. Please enter a valid number.\n");
      scan.nextLine(); // Consume the invalid input
      temp = getValidBodyTemperature(); // Recursive call to prompt user again
    }
    return temp;
  }

  private String getValidDateInput() throws IOException {
    String input;
    while (true) {
      input = scan.next();
      if (input.matches("\\d{2}/\\d{2}/\\d{4}")) {
        return input;
      } else {
        this.out.append("Invalid input. Please enter a valid date in MM/dd/yyyy format.");
      }
    }
  }
}
