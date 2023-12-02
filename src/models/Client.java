package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The purpose of this class is to store the room number, first name, last name, and birthday of
 * each client.
 */

public class Client implements ClientFunctionality {
  private int roomNum;
  private String firstName;
  private String lastName;
  private String birthDateTime;
  private VisitRecord record;
  private List<VisitRecord> recordHistory;
  private boolean active = true;
  private List<Staff> assignedStaff;

  /**
   * The constructor takes in the room number, first name, last name and birthday of the client.
   * 
   *
   * @param roomNum is the assigned room number of the client
   * @param firstName is the first name of the client
   * @param lastName is the last name of the client
   * @param birthDateTime is the birthday of the client
   */

  public Client(int roomNum, String firstName, String lastName, String birthDateTime) {
    super();
    this.roomNum = roomNum;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDateTime = birthDateTime;
    this.recordHistory = new ArrayList<>();
    this.assignedStaff = new ArrayList<>();
  }
  
  /**
  * This is a getter function for the list of assigned staff.
  * @return the list of assigned staff.
  */
  
  public List<Staff> getAssignedStaff() {
    return assignedStaff;
  }

  /**
   * This is a setter for the assigned staff list.
   * @param assignedStaff is the new list of assigned staff.
   */
  
  public void setAssignedStaff(List<Staff> assignedStaff) {
    this.assignedStaff = assignedStaff;
  }

  /**
   * This is a getter for the room number of the patient.
   * @return the room number of the patient.
   */
  
  public int getRoomNum() {
    return roomNum;
  }
  
  /**
   * This is a getter for the first name of the patient.
   * @return the first name of the patient.
   */
  
  public String getFirstName() {
    return firstName;
  }

  /**
   * This is a getter for the last name of the patient.
   * @return the last name of the patient.
   */
  
  public String getLastName() {
    return lastName;
  }

  /**
   * This is a getter for the birthday of the patient.
   * @return the birthday of the patient.
   */
  
  public String getBirthDateTime() {
    return birthDateTime;
  }

  /**
   * This is a setter for the room Number of the patient.
   * @param roomNum is the new room Number they are assigned.
   */
  
  public void setRoomNum(int roomNum) {
    this.roomNum = roomNum;
  }

  /**
   * This is a setter for the visit record.
   * @param record is the new visit record of the patient.
   */
  
  public void setRecord(VisitRecord record) {
    this.record = record;
  }

  /**
   * This is the getter of the current visitRecord.
   * @return the current visitRecord of the patient.
   */
  
  public VisitRecord getRecord() {
    return record;
  }

  /**
   * This is a getter that returns the list of record history.
   * @return the list of records.
   */
  public List<VisitRecord> getRecordHistory() {
    return recordHistory;
  }


  /**
   * This updates the symptoms of the patient.
   * @param client is the client that will get the new symtpom.
   * @param complaint is the new sympoms for the client.
   */
  
  @Override
  public void updateClientChiefComplaint(Client client, String complaint) {
    client.getRecord().setChiefComplaint(complaint);

  }

  /**
   * This updates the temperature of the patient. 
   * @param client is the client who will change their temperature.
   * @param temperature is the new temp of the client.
   */
  
  @Override
  public void updateClientTemperature(Client client, BigDecimal temperature) {
    client.getRecord().setBodyTemp(temperature);

  }

  /**
   * This updates the visit record of the client.
   * @param client is the client that will change the visit record.
   * @param visitRecord is the client's new visit record.
   */
  
  @Override
  public void updateClientVisitRecord(Client client, String visitRecord) {
    client.getRecord().setTimeOfRegistration(visitRecord);

  }

  /**
   * This method checks if the patient is currently in the clinic.
   * @return if the client is currently in the clinic or not.
   */
  
  public boolean isActive() {
    return active;
  }

  /**
   * This is a setter for the patients clinic activity status.
   * @param active is the new status.
   */
  
  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Checks if the time difference between the first and last records in the record history
   * is greater than or equal to 365 days.
   *
   * @return {@code true} if the time difference is greater than or equal to 365 days,
   *         {@code false} otherwise. Returns {@code false} if there are less than two records.
   */
  
  @Override
  public boolean isFirstRecord365DaysGreater() {
    if (getRecordHistory().isEmpty() || getRecordHistory().size() < 2) {
      return false; // There must be at least two records to compare
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");

    // Get the LocalDateTime for the first and last records
    // LocalDateTime firstRecordTime =
    // LocalDateTime.parse(getRecordHistory().get(0).getTimeOfRegistration(), formatter);
    LocalDateTime lastRecordTime = LocalDateTime.parse(
        getRecordHistory().get(getRecordHistory().size() - 1).getTimeOfRegistration(), formatter);
    // Calculate the duration between the two timestamps in days
    long daysDifference = ChronoUnit.DAYS.between(lastRecordTime, LocalDateTime.now());
    long year = 365;
    return Math.abs(daysDifference) >= year;
  }
  
  /**
   * Checks if there are at least two records in the record history with a time difference
   * of less than 365 days between them, indicating two visits within a year.
   *
   * @return {@code true} if there are two records with a time difference less than 365 days,
   *         {@code false} otherwise. Returns {@code false} if there are less than two records.
   */
  
  @Override
  public boolean twoVisitInYear() {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    long year = 365;
    if (getRecordHistory().isEmpty() || getRecordHistory().size() < 2) {
      return false; // There must be at least two records to compare
    }
    // Get the LocalDateTime for the first and last records
    // LocalDateTime firstRecordTime =
    // LocalDateTime.parse(getRecordHistory().get(0).getTimeOfRegistration(), formatter);
    for (int i = 0; i < getRecordHistory().size() - 1; i++) {
      LocalDateTime lastRecordTime = LocalDateTime
          .parse(getRecordHistory().get(i).getTimeOfRegistration(), formatter);
      // Calculate the duration between the two timestamps in days
      long daysDifference = ChronoUnit.DAYS.between(lastRecordTime, LocalDateTime.now());
      if (Math.abs(daysDifference) < year) {
        System.out.println(lastRecordTime);
        System.out.println(LocalDateTime.now());
        System.out.println(daysDifference);

        return true;
      }
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(birthDateTime, firstName, lastName, record, roomNum);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Client other = (Client) obj;
    return Objects.equals(birthDateTime, other.birthDateTime)
        && Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
        && Objects.equals(record, other.record) && roomNum == other.roomNum;
  }

  @Override
  public String toString() {
    if (roomNum == 5) {
      if (record == null) {
        return "First Name: " + firstName + "\n" + "Last Name: " + lastName + "\n"
            + " -Date Of Birth: " + birthDateTime + "\n" + " -Room Number: " + roomNum + "\n"
            + " -No Current Record";
      }
      return "First Name: " + firstName + "\n" + "Last Name: " + lastName + "\n"
          + " -Date Of Birth: " + birthDateTime + "\n" + " -Room Number: " + roomNum + "\n" + " -"
          + record.getChiefComplaint();
    }
    if (record == null) {
      return "First Name: " + firstName + ", Last Name: " + lastName + ", Date Of Birth: "
          + birthDateTime + ", Room Number: " + roomNum + ", No Current Record";
    }
    return "First Name: " + firstName + ", Last Name: " + lastName + ", Date Of Birth: "
        + birthDateTime + ", Room Number: " + roomNum + ", " + record.getChiefComplaint();
  }

}
