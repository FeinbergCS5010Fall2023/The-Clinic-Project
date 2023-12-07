package models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
/**
 * This class contains the information for the visit record of each client.
 */

public class VisitRecord {
  private String timeOfRegistration;
  private String chiefComplaint;
  private BigDecimal bodyTemp;
  
  /**
   * Constructs a new VisitRecord object to store visit information.
   *
   * The `VisitRecord` class is designed to represent a patient's visit to a medical
   * facility, capturing details such as the date and time of the visit, the chief complaint,
   * and the patient's body temperature.
   *
   * @param formattedDateTime A string representing the date and time of the visit in a
   *                         specific format (e.g., "yyyy-MM-dd HH:mm:ss").
   * @param chiefComplaint    A description of the patient's chief complaint for the visit.
   * @param bodyTemp          The patient's body temperature at the time of the visit,
   *                         represented as a `BigDecimal`.
   */
  
  public VisitRecord(String formattedDateTime, String chiefComplaint, BigDecimal bodyTemp) {
    super();
    this.timeOfRegistration = formattedDateTime;
    this.chiefComplaint = chiefComplaint;
    this.bodyTemp = bodyTemp;
  }

  /**
   * Retrieves the time of registration for a record.
   *
   * @return The time of registration as a string.
   */
  
  public String getTimeOfRegistration() {
    return timeOfRegistration;
  }

  /**
   * Retrieves the chief complaint associated with a record.
   *
   * @return The chief complaint as a string.
   */
  
  public String getChiefComplaint() {
    return chiefComplaint;
  }

  /**
   * Retrieves the body temperature recorded in a record.
   *
   * @return The body temperature as a BigDecimal, rounded to two decimal places.
   */
  
  public BigDecimal getBodyTemp() {

    return bodyTemp.setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Sets the time of registration for a record.
   *
   * @param timeOfRegistration The time of registration to be set.
   */
  
  public void setTimeOfRegistration(String timeOfRegistration) {
    this.timeOfRegistration = timeOfRegistration;
  }

  /**
   * Sets the chief complaint for a record.
   *
   * @param chiefComplaint The chief complaint to be set.
   */
  
  public void setChiefComplaint(String chiefComplaint) {
    this.chiefComplaint = chiefComplaint;
  }

  /**
   * Sets the body temperature for a record.
   *
   * @param bodyTemp The body temperature to be set as a BigDecimal.
   */
  
  public void setBodyTemp(BigDecimal bodyTemp) {
    this.bodyTemp = bodyTemp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(bodyTemp, chiefComplaint, timeOfRegistration);
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
    VisitRecord other = (VisitRecord) obj;
    return Objects.equals(bodyTemp, other.bodyTemp)
        && Objects.equals(chiefComplaint, other.chiefComplaint)
        && Objects.equals(timeOfRegistration, other.timeOfRegistration);
  }

  @Override
  public String toString() {
    // Display bodyTemp to one decimal place
    String formattedBodyTemp = String.format("%.1f", bodyTemp);

    return "VisitRecord:\n Registration: " + timeOfRegistration + ", Symptoms: " + chiefComplaint
        + ", Body Temperature (in Celsius): " + formattedBodyTemp + "\n";
  }

}
