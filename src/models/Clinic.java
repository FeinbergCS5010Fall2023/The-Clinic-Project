package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * A single class who's purpose is to hold all the information about all the classes implemented.
 * This information includes The list of staff members, the list of client members, the list of
 * rooms, the list of waiting rooms, the key that defines the room id from the room number, and the
 * list that determines who each staff is assigned to.
 */

public class Clinic implements ClinicFunctionality {
  private List<Staff> staffList;
  private List<Client> clientList;
  private List<Room> roomList;
  private List<Integer> waitingRoomList;
  private List<Client> clinicArchives;
  private String name;
  private Map<Integer, int[]> roomKey;
  private Map<Staff, ArrayList<Client>> staffKey;

  /**
   * The constructor is used to store the name of the clinic.
   *
   * @param name contains the name of the clinic.
   */
  public Clinic(String name) {
    super();
    this.name = name;
    staffList = new ArrayList<Staff>();
    clientList = new ArrayList<Client>();
    roomList = new ArrayList<Room>();
    setWaitingRoomList(new ArrayList<Integer>());
    roomKey = new HashMap<Integer, int[]>();
    staffKey = new HashMap<Staff, ArrayList<Client>>();
    clinicArchives = new ArrayList<Client>();
  }

  /**
   * Retrieves the name of the clinic.
   *
   * @return The name of the clinic as a string.
   */

  public String getName() {
    return name;
  }

  /**
   * Retrieves the list of staff members in the clinic.
   *
   * @return The list of staff members.
   */

  public List<Staff> getClinicStaffs() {
    return staffList;
  }

  /**
   * Retrieves the list of client members in the clinic.
   *
   * @return The list of client members.
   */

  public List<Client> getClinicClients() {
    return clientList;
  }

  /**
   * Retrieves the list of rooms in the clinic.
   *
   * @return The list of rooms.
   */

  public List<Room> getClinicRooms() {
    return roomList;
  }

  /**
   * Retrieves the list of archived clients in the clinic.
   *
   * @return The list of archived clients.
   */

  public List<Client> getClinicArchives() {
    return clinicArchives;
  }

  /**
   * The purpose of this function is to read and load the data from the given file.
   *
   * @param file is the file path that contains the file to be read
   * @throws IllegalArgumentException if there is no file in the path
   * 
   */
  @Override
  public void readDataFromFile(File file) {
    int numRoom;
    int numStaff;
    int numClient;
    try (Scanner scanner = new Scanner(file)) {
      name = scanner.nextLine();
      // Add all rooms into the room list

      numRoom = Integer.parseInt(scanner.nextLine());
      int[][] idArray = new int[numRoom][4];
      for (int i = 0; i < numRoom; i++) {
        for (int j = 0; j < 4; j++) {
          idArray[i][j] = Integer.parseInt(scanner.next());
          // clinic.populateRoomKey(i+1, room);
        }
        Room room = addClinicRoom(new Room(idArray[i], scanner.next(), scanner.nextLine()));
        room.setStatus(true);
      }
      // Add all staff into the staff list int numStaff =
      numStaff = Integer.parseInt(scanner.nextLine());
      for (int i = 0; i < numStaff; i++) {
        addClinicStaff(new Staff(scanner.next(), scanner.next(), scanner.next()));
      }

      // Add all clients into the client list int numClient =
      numClient = Integer.parseInt(scanner.next());
      for (int i = 0; i < numClient; i++) {
        int roomNumber = Integer.parseInt(scanner.next());
        String firstName = scanner.next();
        String lastName = scanner.next();
        String birthDay = scanner.next();
        addClinicClient(new Client(roomNumber, firstName, lastName, birthDay));
      }
      // System.out.println("File was read");

    } catch (FileNotFoundException e) {
      System.out.println("File was not read");
      e.printStackTrace();
    }
  }

  /**
   * Purpose of this function is to identify the waiting Room in the file. This is assuming there is
   * only one waiting room
   *
   * @return returns the room number that the waiting room id is.
   */
  @Override
  public int getWaitingRoomNumber() {
    // First I need to check to see where the waiting room is

    for (int i = 0; i < roomList.size(); i++) {

      // This is done by iterating through the list of rooms and checking
      // which room has a room type of waiting.
      if (roomList.get(i).getRoomType().contains("waiting")) {

        // Once the waiting room has been round, we need to figure out
        // The room number of that room with the given id
        for (Map.Entry<Integer, int[]> entry : getRoomKey().entrySet()) {
          if (roomList.get(i).getId() == entry.getValue()) {

            // We then return the room number, giving us the waiting room
            return entry.getKey();
          }
        }
      }
    }
    return 0;
  }

  /**
   * The purpose of this function is to register clients into the clinic.
   *
   * @param registration is the client who will be autoassigned to the waiting room.
   * @param record is the visit record that will be attached to the client once he's registered.
   * @return returns the client that was created with the registration information.
   */
  @Override
  public Client registerClient(Registration registration, VisitRecord record) {

    // Using the information from the registration class, we are able to assign
    // Registration to the waiting room first, then once they are registered as
    // "Clients", they are put in the waiting room and finally a client of the
    // clinic.
    Client newClient = new Client(getWaitingRoomNumber(), registration.getFirstName(),
        registration.getLastName(), registration.getBirthDateTime());

    // Once they are registered as a client of the clinic, they are added to the
    // client list, making it official

    // clinicArchives.add(newClient);

    // clientList.add(newClient);

    clientList.add(newClient);
    clinicArchives.add(newClient);
    return newClient;
  }

  /**
   * he purpose of this function is to add the client to the client list.
   *
   * @param client adds the client to the client list.
   * @return returns the client that was added to the client list.
   */
  @Override
  public Client addClinicClient(Client client) {

    clientList.add(client);
    return client;

  }

  /**
   * The purpose of this function is to make a key that contains the room ids as well as their
   * corresponding room numbers.
   *
   * @param i represents the room number
   * @param room is used to get the id of the room to populate the key
   */
  @Override
  public void populateRoomKey(int i, Room room) {
    getRoomKey().put(i, room.getId());
  }

  /**
   * This gets the room key.
   * 
   * @return the room key.
   */
  public Map<Integer, int[]> getRoomKey() {
    return roomKey;
  }

  /**
   * This gets the staff key.
   * 
   * @return the staff key.
   */
  public Map<Staff, ArrayList<Client>> getStaffKey() {
    return staffKey;
  }

  /**
   * The purpose of this function is to remove the client from the clinic.
   *
   * @param client is used to add the given client to the client list
   * @param staff is used to add the given staff member to the staff list.
   */
  @Override
  public void removeClinicClient(Client client, Staff staff) {

    // This first checks to see if the client is a registered client
    // And if the staff is a registered staff member. This was done with
    // Using the .contains function for each arrayList

    int[] arr = new int[4];
    if (clientList.contains(client) && staffList.contains(staff)) {
      for (Map.Entry<Integer, int[]> entry : getRoomKey().entrySet()) {
        if (client.getRoomNum() == entry.getKey()) {
          arr = entry.getValue();
        }
      }

      for (int i = 0; i < roomList.size(); i++) {
        if (Arrays.equals(roomList.get(i).getId(), arr)) {
          roomList.get(i).setStatus(false);
        }
      }
      removeAllStaffFromClient(client);
      clientList.remove(client);
      client.setActive(false);
      client.setRoomNum(getWaitingRoomNumber());

      // We then throw an IllegalArgumentException if the staff member is not
      // a valid staff member. Valid meaning she wasn't added to the staff list.
      // We can assume if she wasn't added to the staff list then she is not a
      // valid staff member who can discharge patients
    } else if (clientList.contains(client) && !staffList.contains(staff)) {
      throw new IllegalArgumentException("Removal Has Not Been Approved By Staff");

      // Lastly, we check if the client is a registered client. We can assume they are
      // A registered client if they are in the client list. If they are not, then we
      // Throw an IllegalArgumentException
    } else {
      throw new IllegalArgumentException("Client Error, Client Doesn't Exist");
    }
  }

  /**
   * adds a staff to the staff list, "registering" them into the clinic.
   * 
   * @param staff is the staff member that is being registered into the clinic.
   */
  @Override
  public void addClinicStaff(Staff staff) {
    if (staff.getClinicalStaffStatus() == true) {
      staffList.add(staff);

    }
    ArrayList<Client> tempList = new ArrayList<>();
    // Client temp = new Client(0, "", "", "");
    // tempList.add(temp);
    staffKey.put(staff, tempList);
    // removeStaffFromClient(staff, temp);
    // staffKey.remove(temp);
  }

  /**
   * Execute a move in the position specified by the given row and column.
   *
   * @param staff is used to remove the given staff from the staff list.
   * 
   */
  @Override
  public void removeClinicStaff(Staff staff) {

    // We first check if the staff member was a registered staff member. if they are
    // Then we just remove that staff member object from the arrayList
    if (staffList.contains(staff)) {
      staffWasRemovedFromClinic(staff);
      staffList.remove(staff);

      // If that staff member isn't a registered staff member, then we throw an
      // IllegalArgumentException
    } else {
      throw new IllegalArgumentException("Staff Error, Staff Doesn't Exist");
    }
  }

  /**
   * This adds to the master room list. Execute a move in the position specified by the given row
   * and column.
   *
   * @param room is used to add the given room to the room list.
   * @return the room after it has been added to the room list.
   */
  @Override
  public Room addClinicRoom(Room room) {

    // This adds the room to the room list, and then populates the
    // room key. The reason why we need to populate the room key is
    // so we can check back on it if we have the room number
    // but we need to find the id of and vice versa.
    roomList.add(room);
    populateRoomKey(getRoomKey().size() + 1, room);

    // this determines if the room that was added is a waiting room;
    getWaitingRoomNumber();
    return room;
  }

  /**
   * The purpose of this function is to validate if the room is available.
   *
   * @param id is taken to find the status of that room with the given id
   * @return boolean true or false depending on if the room is available or not.
   */
  @Override
  public boolean validateRoom(int[] id) {
    // If the ID's match and the status of the room is true,
    // then it's available for use
    for (int i = 0; i < roomList.size(); i++) {
      if (Arrays.equals(roomList.get(i).getId(), id)) {
        return roomList.get(i).getStatus();
      }
    }
    return false;
  } //

  /**
   * The purpose of this function is to assign a client to a new room upon request.
   *
   * @param client is used to assign to the new room.
   * @param room is used to get the room assigned to the client.
   */
  @Override
  public void assignClientToNewRoom(Client client, Room room) {
    int[] currentRoomId = new int[4];
    // Check if the new room exists
    if (!roomList.contains(room)) {
      throw new IllegalArgumentException("Room Does Not Exist");
    }
    // Check the status of new room to ensure it's available
    if (validateRoom(room.getId()) && !room.getRoomName().contains("Waiting")) {
      throw new IllegalArgumentException("Room is occupied");
    }
    // change status of old room from unavailable to available;
    if (client.getRoomNum() != getWaitingRoomNumber()) {
      for (Map.Entry<Integer, int[]> entry : getRoomKey().entrySet()) {
        if (client.getRoomNum() == entry.getKey()) {
          currentRoomId = entry.getValue();
        }
      }
      for (int i = 0; i < roomList.size(); i++) {
        if (Arrays.equals(roomList.get(i).getId(), currentRoomId)) {
          roomList.get(i).setStatus(false);
        }
      }
    }

    // set client room to new room and set status of new room to occupied
    for (Map.Entry<Integer, int[]> entry : getRoomKey().entrySet()) {
      if (Arrays.equals(room.getId(), entry.getValue())) {
        client.setRoomNum(entry.getKey());
        System.out.println(entry.getKey());
      }
      room.setStatus(true);
    }

  }

  /**
   * The purpose of this function is to assign a staff member to a client.
   *
   * @param staff is used get the staff from the staff key
   * @param client is used for the client to be assigned to the given staff
   * @return true if the staff was assigned to the client successfully, but false if not.
   */
  @Override
  public boolean assignStaffToClient(Staff staff, Client client) {
    ArrayList<Client> tempList = new ArrayList<>();

    // Check if the staff already exists in the map
    if (staffKey.containsKey(staff)) {
      if (!isStaffAlreadyWithPatient(staff, client)) {
        return false;
      }
      // Staff exists, retrieve the existing list of clients and add the new client
      tempList = staffKey.get(staff);
      tempList.add(client);
      staffKey.put(staff, tempList);
    } else {
      // Staff doesn't exist, create a new list and associate it with the staff
      tempList.add(client);
      staffKey.put(staff, tempList);

    }
    boolean check = false;
    for (Client clients : staff.getStaffKey()) {
      if (clients.getFirstName().contains(client.getFirstName())
          && clients.getLastName().contains(client.getLastName())) {
        check = true;
      }
    }
    if (!check) {
      staff.getStaffKey().add(client);
    }
    return true;
  }

  /**
   * This method is used to assign a client to a staff member.
   * 
   * @param staff will be assigned to a staff.
   * @param client will have a staff member assigned to them.
   */
  @Override
  public void assignClientToStaff(Staff staff, Client client) {
    // check if staff exists
    if (!staffList.contains(staff)) {
      throw new IllegalArgumentException("Staff Error, Staff Doesn't Exist");
    } else if (client.getAssignedStaff().contains(staff)) {
      throw new IllegalArgumentException("Staff Error, Staff is already assigned to patient");
    } else {
      client.getAssignedStaff().add(staff);
      boolean check = false;
      for (Client clients : staff.getStaffKey()) {
        if (clients.getFirstName().contains(client.getFirstName())
            && clients.getLastName().contains(client.getLastName())) {
          check = true;
        }
      }
      if (!check) {
        staff.getStaffKey().add(client);
      }
    }
  }

  /**
   * This method removes the client from the staff member.
   * 
   * @param staff is the staff being removed.
   * @param client is the patient who is begin removed as well.
   */
  @Override
  public void removeClientFromStaff(Staff staff, Client client) {
    if (!staffList.contains(staff)) {
      throw new IllegalArgumentException("Staff Error, Staff Doesn't Exist");
    } else if (!client.getAssignedStaff().contains(staff)) {
      throw new IllegalArgumentException("Staff was not assigned to this patient");
    } else {
      client.getAssignedStaff().remove(staff);
    }
  }

  /**
   * This function removes all the staff that the client is assigned to.
   * 
   * @param client is used to remove all his/her assigned staff members.
   */
  @Override
  public void removeAllStaffFromClient(Client client) {
    for (Staff staff : client.getAssignedStaff()) {
      client.getAssignedStaff().remove(staff);
    }
    for (Entry<Staff, ArrayList<Client>> entry : getStaffKey().entrySet()) {
      if (entry.getValue().contains(client)) {
        entry.getValue().remove(client);
      }
    }
  }

  /**
   * Checks if the staff member was removed from the clinic.
   * 
   * @param staff is the member who is getting removed.
   */
  @Override
  public void staffWasRemovedFromClinic(Staff staff) {
    staffKey.remove(staff);
  }

  /**
   * This is a helped function that determines if a staff member is already assigned to a patient.
   * 
   * @param staff is the member that will be assigned to the patient.
   * @param client is the patient who might already be assigned to this staff member.
   * @return false if they are already assigned to each other and true if not.
   */
  @Override
  public boolean isStaffAlreadyWithPatient(Staff staff, Client client) {
    for (Entry<Staff, ArrayList<Client>> entry : getStaffKey().entrySet()) {
      if (entry.getKey() == staff) {
        if (entry.getValue().contains(client)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * This method displays the information of the room.
   * 
   * @param room is the room that we will see the information of.
   * @return the string once the information has been parsed.
   */
  @Override
  public String displayRoomInfo(Room room) {
    String res = "";
    int roomNum = 0;
    ArrayList<Client> clientsInTheRoomArrayList = new ArrayList<>();
    ArrayList<Staff> staffInTheRoom = new ArrayList<>();

    for (Map.Entry<Integer, int[]> entry : getRoomKey().entrySet()) {
      if (Arrays.equals(room.getId(), entry.getValue())) {
        roomNum = entry.getKey();
      }
    }

    if (roomNum != 0) {
      for (Client client : getClinicClients()) {
        if (client.getRoomNum() == roomNum) {
          clientsInTheRoomArrayList.add(client);
        }
      }

      for (Client client : clientsInTheRoomArrayList) {
        if (client.getRecord() != null) {
          String removeThis = client.getRecord().toString();
          String resultantString = client.toString().replace(removeThis,
              " Chief Complaints:" + client.getRecord().getChiefComplaint());
          res += resultantString + "\n";
        } else {
          res += client + "\n";
        }
      }

      for (Map.Entry<Staff, ArrayList<Client>> entry : getStaffKey().entrySet()) {
        for (Client client : clientsInTheRoomArrayList) {
          if (entry.getValue().contains(client)) {
            staffInTheRoom.add(entry.getKey());
          }
        }
      }

      for (Staff staff : staffInTheRoom) {
        res += staff + "\n";
      }
    }

    return res;
  }

  /**
   * This method displays all the info of every room.
   * 
   * @return the string that was build with parsing through the clinic.
   */
  @Override
  public String displayAllInfo() {
    String res = "";

    for (Map.Entry<Integer, int[]> entry : getRoomKey().entrySet()) {
      res += "Room " + Integer.toString(entry.getKey()) + "\n";
      for (Room room : getClinicRooms()) {
        if (entry.getValue() == room.getId()) {
          res += displayRoomInfo(room);
        }
      }
    }

    return res;
  }

  /**
   * This gets the list of waiting rooms.
   * 
   * @return the list of waiting rooms.
   */
  public List<Integer> getWaitingRoomList() {
    return waitingRoomList;
  }

  @Override
  public void setWaitingRoomList(ArrayList<Integer> waitingRoomList) {
    this.waitingRoomList = waitingRoomList;
  }

  /**
   * In this method, it checks if the staff is in the staffKey that is located in the clinic class
   * (this class). If that particular staff member is found in the staffKey, it checks if the client
   * that is passed in is associated with that staff member. If they are, then that client is
   * removed from that list. This has a return type void.
   * 
   * @param staff is the staff member who is currently assigned to the client.
   * @param client is the client that will be unassigned from the staff member
   */

  @Override
  public void removeStaffFromClient(Staff staff, Client client) {
    if (getStaffKey().containsKey(staff)) {
      ArrayList<Client> clients = getStaffKey().get(staff);
      if (clients.contains(client)) {
        clients.remove(client);

      }

    }
  }

  /**
   * In this method, it iterates through the client list and determines which clients have been to
   * the clinic at least twice within the year. The functionality of this method is mainly done in
   * the client.twoVisitInYear method; however, the purpose of this method is to perform that method
   * to each patient and collect the names of all patients who have been in the clinic at least
   * twice within the year. That name is added to a string that is returned.
   * 
   * @return the list of patients who have been to the clinic at least twice within the year.
   */

  @Override
  public String isPatientOneYearIn() {
    String list = "";
    for (Client client : clientList) {

      if (client.twoVisitInYear()) {
        list += client.getFirstName() + " " + client.getLastName() + "\n";
      }
    }

    return "--------------------------------\n" + list + "--------------------------------\n";
  }

  /**
   * This method finds the patients who have not been in the clinic for more than a year. It
   * iterates through the client list and checks if the last time of registration date has exceeded
   * a year. If it has, then the name of the patient will be added to the result string. That result
   * string will be returned and later in the game fed into an Appendable out.
   * 
   * @return the list of patients that have not visited the clinic in a year.
   */

  @Override
  public String getNoYearVisitList() {
    String res = "";
    for (Client client : clientList) {
      if (client.isFirstRecord365DaysGreater()) {
        res += client.getFirstName() + " " + client.getLastName() + "\n";
      }
    }
    if (res.length() == 0) {
      return res;
    }
    String nonEmptyString = "--------------------------------\nHere is the list of patients:\n"
        + res + "--------------------------------\n";
    return nonEmptyString;
  }
}
