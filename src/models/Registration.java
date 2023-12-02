package models;

import java.util.Objects;
/**
 * The purpose of this class is to store the first name, last name, and birthday of an incoming
 * registration client. This occurs before the client class. This is used so that once the 
 * registration is converted into a client, they are assigned to the waiting room.
 */

public class Registration {
  private int roomNum;
  private String firstName;
  private String lastName;
  private String birthDateTime;
  private Clinic clinic = new Clinic("TestClinic");
  
  /**
   * Constructor takes in the first name, last name, and birthday of the new registration.
   *
   * @param firstName is the first name of the registration
   * @param lastName is the last name of the registration
   * @param birthDateTime is the birthday of the registration 
   */
  public Registration(String firstName, String lastName, String birthDateTime) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDateTime = birthDateTime;
  }

  /**
   * Generates a waiting room number for the current instance based on the provided clinic.
   *
   * @param clinic The clinic instance used to obtain the waiting room number.
   */
  
  public void waitingRoomNumberGenerator(Clinic clinic) {
    this.roomNum = clinic.getWaitingRoomNumber();
  }

  /**
   * Retrieves the waiting room number associated with the current instance.
   *
   * @return The waiting room number.
   */
  
  public int getRoomNum() {
    waitingRoomNumberGenerator(clinic);
    return roomNum;
  }

  /**
   * Retrieves the first name of the individual.
   *
   * @return The first name as a string.
   */

  public String getFirstName() {
    return firstName;
  }

  /**
   * Retrieves the last name of the individual.
   *
   * @return The last name as a string.
   */

  public String getLastName() {
    return lastName;
  }

  /**
   * Retrieves the birth date and time of the individual.
   *
   * @return The birth date and time as a string.
   */

  public String getBirthDateTime() {
    return birthDateTime;
  }

  /**
   * Sets the waiting room number for the current instance.
   *
   * @param roomNum The waiting room number to be set.
   */

  public void setRoomNum(int roomNum) {
    this.roomNum = roomNum;
  }

  /**
   * Sets the first name of the individual.
   *
   * @param firstName The first name to be set.
   */

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Sets the last name of the individual.
   *
   * @param lastName The last name to be set.
   */

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Sets the birth date and time of the individual.
   *
   * @param birthDateTime The birth date and time to be set.
   */

  public void setBirthDateTime(String birthDateTime) {
    this.birthDateTime = birthDateTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(birthDateTime, firstName, lastName, roomNum);
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
    Registration other = (Registration) obj;
    return Objects.equals(birthDateTime, other.birthDateTime)
      && Objects.equals(firstName, other.firstName)
      && Objects.equals(lastName, other.lastName) && roomNum == other.roomNum;
  }


  @Override
  public String toString() {
    return "Registration [roomNum=" + roomNum + ", firstName=" + firstName + ", lastName="
      + lastName + ", birthDateTime=" + birthDateTime + "]";
  }

}
