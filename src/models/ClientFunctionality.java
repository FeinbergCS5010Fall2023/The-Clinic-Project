package models;

import java.math.BigDecimal;

/**
 * The purpose of this class is to store the room number, first name, last name, and birthday of
 * each client.
 */
public interface ClientFunctionality {

  /**
   * This updates the visit record of the client.
   * @param client is the client that will change the visit record.
   * @param visitRecord is the client's new visit record.
   */
  void updateClientVisitRecord(Client client, String visitRecord);

  /**
   * This updates the symptoms of the patient.
   * @param client is the client that will get the new symtpom.
   * @param complaint is the new sympoms for the client.
   */
  void updateClientChiefComplaint(Client client, String complaint);

  /**
   * This updates the temperature of the patient. 
   * @param client is the client who will change their temperature.
   * @param temperature is the new temp of the client.
   */
  void updateClientTemperature(Client client, BigDecimal temperature);

  /**
   * Checks if there are at least two records in the record history with a time difference
   * of less than 365 days between them, indicating two visits within a year.
   *
   * @return {@code true} if there are two records with a time difference less than 365 days,
   *         {@code false} otherwise. Returns {@code false} if there are less than two records.
   */
  boolean twoVisitInYear();

  /**
   * Checks if the time difference between the first and last records in the record history
   * is greater than or equal to 365 days.
   *
   * @return {@code true} if the time difference is greater than or equal to 365 days,
   *         {@code false} otherwise. Returns {@code false} if there are less than two records.
   */
  boolean isFirstRecord365DaysGreater();

}
