package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * The purpose of the driver class is to demonstrate the functionality of the project.
 */

public class DriverClass {
  private static Clinic clinic;
  
  /**
   * The main function displays all the information for the functionality of the project.
   *
   * @param args takes the in line command argument for the file to read data from.
   */
  public static void main(String[] args) throws FileNotFoundException {

    File file = new File("clinic-2.txt");
    clinic = new Clinic("Test");
    clinic.readDataFromFile(file);

    Registration michael = new Registration("Michael", "Jordan", "2/17/1963");
    Registration scotty = new Registration("Scott", "Pippen", "9/25/1965");
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    VisitRecord record1 = new VisitRecord(formattedDateTime, "Fever", new BigDecimal("99"));
    VisitRecord record2 = new VisitRecord(formattedDateTime, "Cold", new BigDecimal("88"));
    Client registeredMichael = clinic.registerClient(michael, record1);
    Client registeredScotty = clinic.registerClient(scotty, record2);

    if (clinic.getClinicClients().contains(registeredMichael)) {
      System.out.println(registeredMichael);
    }
    if (clinic.getClinicClients().contains(registeredScotty)) {
      System.out.println(registeredScotty);
    }

    Staff phil = new Staff("physician", "Phil", "Jackson");
    Staff pat = new Staff("physician", "Patrick", "Riley");
    clinic.addClinicStaff(phil);
    clinic.addClinicStaff(pat);
    if (clinic.getClinicStaffs().contains(phil) && clinic.getClinicStaffs().contains(pat)) {
      System.out.println(phil);
      System.out.println(pat);
    }

    clinic.removeClinicClient(registeredScotty, phil);
    if (!clinic.getClinicClients().contains(registeredScotty)) {
      System.out.println("Scottie has been removed from the clinic");
    }

    clinic.removeClinicStaff(pat);
    if (!clinic.getClinicStaffs().contains(pat)) {
      System.out.println("Pat is no longer a staff member at this clinic");
    }
    int[] idOne = new int[4];
    idOne[0] = 24;
    idOne[1] = 32;
    idOne[2] = 34;
    idOne[3] = 33;

    int[] idTwo = new int[4];
    idTwo[0] = 1;
    idTwo[1] = 2;
    idTwo[2] = 3;
    idTwo[3] = 4;
    Room cryptoArena =  new Room(idTwo, "Gym", "Crypto Arena");
    Room staplesCenter = new Room(idOne, "Gym", "Staples Center");
    clinic.addClinicRoom(cryptoArena);
    clinic.addClinicRoom(staplesCenter);
    System.out.println("Rooms added:\n" + cryptoArena + ", " + staplesCenter);

    clinic.assignClientToNewRoom(registeredScotty, cryptoArena);
    if (registeredScotty.getRoomNum() == 6) {
      System.out.println("Scotty has been moved to the Crypto Arena as requested");
    }
    clinic.assignClientToNewRoom(registeredScotty, staplesCenter);
    if (registeredScotty.getRoomNum() == 7) {
      System.out.println("Scotty has been moved to the Staples Center as requested");
    }

    clinic.assignClientToNewRoom(registeredMichael, cryptoArena);
    if (registeredMichael.getRoomNum() == 6) {
      System.out.println("Michael has been moved to the CryptoArena as requested");
    }

    clinic.assignStaffToClient(phil, registeredMichael);
    clinic.assignStaffToClient(phil, registeredScotty);

    try {
      FileWriter myWriter = new FileWriter("results.txt");
      myWriter.write(clinic.displayAllInfo());
      myWriter.close();
      System.out.println("This worked");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("This didn't worked");
    }

  }
}
