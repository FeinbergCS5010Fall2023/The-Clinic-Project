package models;

import java.io.File;
import java.util.ArrayList;

/**
 * A single class who's purpose is to hold all the information about all the classes implemented.
 * This information includes The list of staff members, the list of client members, the list of
 * rooms, the list of waiting rooms, the key that defines the room id from the room number, and the
 * list that determines who each staff is assigned to.
 */
public interface ClinicFunctionality {
  
  /**
   * The purpose of this function is to read and load the data from the given file.
   *
   * @param file is the file path that contains the file to be read
   * @throws IllegalArgumentException if there is no file in the path
   * 
   */
  void readDataFromFile(File file);

  /**
   * Purpose of this function is to identify the waiting Room in the file. This is assuming there is
   * only one waiting room
   *
   * @return returns the room number that the waiting room id is.
   */
  int getWaitingRoomNumber();

  /**
   * The purpose of this function is to register clients into the clinic.
   *
   * @param registration is the client who will be autoassigned to the waiting room.
   * @param record is the visit record that will be attached to the client once he's registered.
   * @return returns the client that was created with the registration information.
   */
  Client registerClient(Registration registration, VisitRecord record);

  /**
   * he purpose of this function is to add the client to the client list.
   *
   * @param client adds the client to the client list.
   * @return returns the client that was added to the client list.
   */
  Client addClinicClient(Client client);

  /**
   * The purpose of this function is to make a key that contains the room ids as well as their
   * corresponding room numbers.
   *
   * @param i represents the room number
   * @param room is used to get the id of the room to populate the key
   */
  void populateRoomKey(int i, Room room);

  /**
   * The purpose of this function is to remove the client from the clinic.
   *
   * @param client is used to add the given client to the client list
   * @param staff is used to add the given staff member to the staff list.
   */
  void removeClinicClient(Client client, Staff staff);

  /**
   * adds a staff to the staff list, "registering" them into the clinic.
   * 
   * @param staff is the staff member that is being registered into the clinic.
   */
  void addClinicStaff(Staff staff);

  /**
   * Execute a move in the position specified by the given row and column.
   *
   * @param staff is used to remove the given staff from the staff list.
   * 
   */
  void removeClinicStaff(Staff staff);

  /**
   * This adds to the master room list. Execute a move in the position specified by the given row
   * and column.
   *
   * @param room is used to add the given room to the room list.
   * @return the room after it has been added to the room list.
   */
  Room addClinicRoom(Room room);

  /**
   * The purpose of this function is to validate if the room is available.
   *
   * @param id is taken to find the status of that room with the given id
   * @return boolean true or false depending on if the room is available or not.
   */
  boolean validateRoom(int[] id);

  /**
   * The purpose of this function is to assign a client to a new room upon request.
   *
   * @param client is used to assign to the new room.
   * @param room is used to get the room assigned to the client.
   */
  void assignClientToNewRoom(Client client, Room room);

  /**
   * The purpose of this function is to assign a staff member to a client.
   *
   * @param staff is used get the staff from the staff key
   * @param client is used for the client to be assigned to the given staff
   * @return true if the staff was assigned to the client successfully, but false if not.
   */
  boolean assignStaffToClient(Staff staff, Client client);

  /**
   * This method is used to assign a client to a staff member.
   * 
   * @param staff will be assigned to a staff.
   * @param client will have a staff member assigned to them.
   */
  void assignClientToStaff(Staff staff, Client client);

  /**
   * This method removes the client from the staff member.
   * 
   * @param staff is the staff being removed.
   * @param client is the patient who is begin removed as well.
   */
  void removeClientFromStaff(Staff staff, Client client);

  /**
   * This function removes all the staff that the client is assigned to.
   * 
   * @param client is used to remove all his/her assigned staff members.
   */
  void removeAllStaffFromClient(Client client);

  /**
   * Checks if the staff member was removed from the clinic.
   * 
   * @param staff is the member who is getting removed.
   */
  void staffWasRemovedFromClinic(Staff staff);

  /**
   * This is a helped function that determines if a staff member is already assigned to a patient.
   * 
   * @param staff is the member that will be assigned to the patient.
   * @param client is the patient who might already be assigned to this staff member.
   * @return false if they are already assigned to each other and true if not.
   */
  boolean isStaffAlreadyWithPatient(Staff staff, Client client);

  /**
 * In this method, it iterates through the client list and determines which clients
 * have been to the clinic at least twice within the year. The functionality of this
 * method is mainly done in the client.twoVisitInYear method; however, the purpose of
 * this method is to perform that method to each patient and collect the names of all
 * patients who have been in the clinic at least twice within the year. That name is 
 * added to a string that is returned.
 * @return the list of patients who have been to the clinic at least twice within the year.
 */
  String isPatientOneYearIn();

  /**
 * In this method, it checks if the staff is in the staffKey that is located 
 * in the clinic class (this class). If that particular staff member is 
 * found in the staffKey, it checks if the client that is passed in is 
 * associated with that staff member. If they are, then that client is 
 * removed from that list. This has a return type void.
 * 
 * @param staff is the staff member who is currently assigned to the client.
 * @param client is the client that will be unassigned from the staff member
 */
  void removeStaffFromClient(Staff staff, Client client);
  
  /**
  * This sets the new waitingroom list.
  * @param waitingRoomList is the new waiting room list.
  */
  void setWaitingRoomList(ArrayList<Integer> waitingRoomList);

  /**
 * This method finds the patients who have not been in the clinic for more than a year. 
 * It iterates through the client list and checks if the last time of registration date
 * has exceeded a year. If it has, then the name of the patient will be added to the result
 * string. That result string will be returned and later in the game fed into an Appendable out.
 * @return the list of patients that have not visited the clinic in a year.
 */
  String getNoYearVisitList();

  /**
   * This method displays all the info of every room.
   * 
   * @return the string that was build with parsing through the clinic.
   */
  String displayAllInfo();

  /**
   * This method displays the information of the room.
   * 
   * @param room is the room that we will see the information of.
   * @return the string once the information has been parsed.
   */
  String displayRoomInfo(Room room);

}
