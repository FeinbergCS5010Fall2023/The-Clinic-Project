package models;

import java.io.File;
import java.io.IOException;

/**
 * This inferface declares all the functions for the ClinicConsoleController.
 */

public interface ClinicController {

  /**
   * Displays information about all clients in the clinic, including details such as name, birth
   * date, and other relevant information.
   *
   * @param client The client for which information is to be displayed
   * @param clinic The clinic object containing client information.
   * @return A string containing detailed information about all clients in the clinic.
   */
  String displayClientInfo(Client client, Clinic clinic);

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
  Client registerClientWithVisitRecord(String firstName, String lastName, String birthDay,
      VisitRecord record, Clinic clinic) throws IOException;

  /**
   * This method registers an existing client in the clinic along with their visit record.
   *
   * @param client The existing client to be registered.
   * @param clinic The clinic object where the client and visit record will be associated.
   * @param record The visit record to be associated with the client.
   * @return The registered client with the provided visit record.
   */
  Client registerExistingClientWithVisitRecord(Client client, Clinic clinic, VisitRecord record);

  /**
   * This method registers a new staff member in the clinic by adding them to the list of clinic
   * staff.
   *
   * @param staff The staff member to be registered.
   * @param clinic The clinic object where the staff member will be added.
   * @return The registered staff member.
   */
  Staff registerNewStaff(Staff staff, Clinic clinic);

  /**
   * This method generates and returns a comprehensive display of information about every room in
   * the clinic, including details about the individuals present in each room. The information is
   * collected from the clinic's data and is formatted into a string for further use or output.
   * @param clinic The clinic object containing room and occupancy information.
   * @return A string containing detailed information about every room and its occupants.
   */
  String displayAllInfo(Clinic clinic);

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
  String displayRoomInfo(Room room, Clinic clinic);

  /**
   * This method removes the patient from the clinic.
   * 
   * @param client is the patient that is being moved..
   * @param room is the new room that they are going to.
   * @param clinic is the clinic that they are located in.
   */
  void assignPatientToNewRoom(Room room, Client client, Clinic clinic);

  /**
   * This method sends the patient home.
   * @param client is the client that is being sent to the new room.
   * @param staff approves the move.
   * @param clinic is where this is all taking place.
   */
  void sendPatientHome(Client client, Staff staff, Clinic clinic);

  /**
   * Method assigns staff to patient.
   * 
   * @param clinic is the clinic that they are located in.
   * @param staff is the staff that is being assigned to the patient.
   * @param client is the patient that the staff is being assigned to.
   * @return true if it was successful, but false if they are already assigned to each other.
   */
  boolean assignStaffToPatient(Clinic clinic, Staff staff, Client client) throws IOException;

  /**
   * This method plays the game.
   * 
   * @param clinic is the clinic that the game is taking place in.
   * @param file is the file that is being read for the game.
   */
  void playGame(Clinic clinic, File file);

  /**
   * This method handles adding a new room.
   * 
   * @param clinic is the where this is taking place.
   */
  void handleAddNewRoom(Clinic clinic) throws IOException;

  /**
   * This method handles viewing the patient record history.
   * 
   * @param clinic is where this is taking place.
   */
  void handleViewPatientRecordHistory(Clinic clinic) throws IOException;

  /**
   * method handles adding a patient to the clinic.
   * 
   * @param clinic is where this takes place.
   */
  void handleAddPatient(Clinic clinic) throws IOException;

  /**
   * Method handels adding a staff to the clinic.
   * 
   * @param clinic is where the staff is being added.
   */
  void handleAddStaff(Clinic clinic) throws IOException;

  /**
   * Method removes patient from the clinic object.
   * 
   * @param clinic is where the patient will be removed from.
   * @throws IOException if something isn't inputted correctly.
   */
  void handleRemovePatient(Clinic clinic) throws IOException;

  /**
   * Method assigns staff to patient.
   * 
   * @param clinic is where the staff is being assigned.
   */
  void handleAssignStaffToClient(Clinic clinic) throws IOException;

  /**
   * Method handles asssigning patients to new rooms.
   * 
   * @param clinic is where the rooms are located.
   */
  void handleAssignPatientToRoom(Clinic clinic) throws IOException;

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
  void handleDisplayAllInfo(Clinic clinic) throws IOException;

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
  void handleDisplayRoomInfo(Clinic clinic) throws IOException;

  /**
   * This method handles the view of staff information in the clinic. It displays a summary of each
   * staff member, including their name, assigned clients, and the total number of assigned patients
   * ever.
   *
   * @param clinic The clinic object containing the staff information.
   * @throws IOException If an I/O error occurs while writing to the output stream.
   */
  void handleViewStaff(Clinic clinic) throws IOException;

  /**
   * This method handles the removal of a staff member from the clinic. It prompts the user to enter
   * the first name and last name of the staff member they want to remove. If the staff member is
   * found, they are removed from the clinic. The user has the option to remove another staff
   * member.
   *
   * @param clinic The clinic object from which the staff member is to be removed.
   * @throws IOException If an I/O error occurs while interacting with the input/output stream.
   */
  void handleRemoveStaff(Clinic clinic) throws IOException;

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
  void handleUnassignStaffFromClient(Clinic clinic) throws IOException;

  /**
   * This method generates a visual representation of the clinic layout, including room rectangles
   * and associated information. The resulting image is saved as a PNG file named
   * "clinic_representation.png". The layout includes the clinic name and individual room details.
   * The image is created using a BufferedImage with specified dimensions and graphics operations.
   *
   * @param clinic The clinic object containing information about rooms and layout details.
   */
  void displayGame(Clinic clinic);

  /**
   * This method handles the retrieval and display of a list of patients who have not visited the
   * clinic for more than 365 days. The list is obtained from the clinic's information, and the
   * result is printed to the output stream. If there are no such patients, a corresponding message
   * is displayed.
   *
   * @param clinic The clinic object containing patient information and visit history.
   * @throws IOException If an I/O error occurs while interacting with the input/output stream.
   */
  void handleYearNoVisit(Clinic clinic) throws IOException;

  /**
   * This method handles the display of information for all staff members in the clinic, including
   * their names, assigned clients, and the total number of patients assigned. The information is
   * obtained from the clinic's data and is printed to the output stream in a formatted manner.
   *
   * @param clinic The clinic object containing staff and client information.
   * @throws IOException If an I/O error occurs while interacting with the input/output stream.
   */
  void handleViewAllStaffMembers(Clinic clinic) throws IOException;

}
