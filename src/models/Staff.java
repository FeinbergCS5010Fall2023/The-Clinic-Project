package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The purpose of this class is to store the occupation, first namem and last name of inidvisual
 * staff members.
 */
public class Staff {
  private String occupation;
  private String firstName;
  private String lastName;
  private boolean clinicalStaffStatus = false;
  private List<Client> staffKey = new ArrayList<>();

  /**
   * constructor takes in the occupation, first name, and last name of the staff member.
   *
   * @param occupation is the assigned job of the staff member.
   * @param firstName is the first name of the staff member.
   * @param lastName is the last name of the staff member.
   */
  public Staff(String occupation, String firstName, String lastName) {
    super();
    this.occupation = occupation;
    if ("physician".equals(occupation)) {
      this.firstName = "Dr. " + firstName;
      this.lastName = lastName;
      clinicalStaffStatus = true;
    } else if ("nurse".equals(occupation)) {
      this.firstName = "Nurse " + firstName;
      this.lastName = lastName;
      clinicalStaffStatus = true;
    } else {
      this.firstName = firstName;
      this.lastName = lastName;
    }
  }

  /**
   * Retrieves the number of patients associated with the staff member.
   *
   * @return The number of patients assigned to the staff.
   */
  
  public int getNumPatients() {
    return staffKey.size();
  }

  /**
   * Retrieves the list of clients (patients) associated with the staff member.
   *
   * @return The list of clients assigned to the staff.
   */
  
  public List<Client> getStaffKey() {
    return staffKey;
  }

  /**
   * Sets the list of clients (patients) associated with the staff member.
   *
   * @param staffKey The list of clients to be set for the staff.
   */
  
  public void setStaffKey(ArrayList<Client> staffKey) {
    this.staffKey = staffKey;
  }

  /**
   * Retrieves the occupation of the staff member.
   *
   * @return The occupation as a string.
   */
  
  public String getOccupation() {
    return occupation;
  }

  /**
   * Retrieves the first name of the staff member.
   *
   * @return The first name as a string.
   */
  
  public String getFirstName() {
    return firstName;
  }

  /**
   * Retrieves the last name of the staff member.
   *
   * @return The last name as a string.
   */
  
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the occupation of the staff member.
   *
   * @param occupation The occupation to be set.
   */
  
  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  /**
   * Sets the first name of the staff member.
   *
   * @param firstName The first name to be set.
   */

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Sets the last name of the staff member.
   *
   * @param lastName The last name to be set.
   */
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Retrieves the clinical staff status of the staff member.
   *
   * @return True if the staff member is a clinical staff, false otherwise.
   */
  
  public boolean getClinicalStaffStatus() {
    return clinicalStaffStatus;
  }

  /**
   * Sets the clinical staff status of the staff member.
   *
   * @param clinicalStaffStatus The clinical staff status to be set.
   */
  
  public void setClinicalStaffStatus(boolean clinicalStaffStatus) {
    this.clinicalStaffStatus = clinicalStaffStatus;
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, occupation);
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
    Staff other = (Staff) obj;
    return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
        && Objects.equals(occupation, other.occupation);
  }

  @Override
  public String toString() {
    return firstName + " " + lastName;
  }

}
