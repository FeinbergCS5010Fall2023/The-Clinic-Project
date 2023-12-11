package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map.Entry;
import models.Client;
import models.Clinic;
import models.Room;
import models.Staff;
import models.VisitRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class tests all the functionality in the Clinic class.
 */
class TestClinic {
  private Clinic clinic;
  
  /**
   * This ensures the clinic is established for each test case.
   */
  
  @BeforeEach
  void setUpBeforeClass() {
    File file = new File("clinic-2.txt");
    clinic = new Clinic("Test");
    clinic.readDataFromFile(file);
  }

  // tests that the get room list has what is needed
  @Test
  void testGetAllRooms() {
    assertEquals(5, clinic.getClinicRooms().size());
  }

  // tests that the get staff list has what is needed
  @Test
  void testGetAllStaff() {
    assertEquals(5, clinic.getClinicStaffs().size());
  }

  // tests that the get client list has what is needed
  @Test
  void testGetAllClients() {
    assertEquals(7, clinic.getClinicClients().size());
  }

  // tests that the get waiting room list has what is needed
  @Test
  void testWaitingRoomList() {
    int[] arrOne = new int[4];
    for (int i = 0; i < 4; i++) {
      arrOne[i] = i;
    }

    int[] arrTwo = new int[4];
    for (int i = 0; i < 4; i++) {
      arrOne[i] = 9;
    }

    Room testRoom = new Room(arrOne, "waiting", "First Waiting Room");
    clinic.addClinicRoom(testRoom);
    Room testRoom2 = new Room(arrTwo, "waiting", "Second Waiting Room");
    clinic.addClinicRoom(testRoom2);

    assertEquals(1, clinic.getWaitingRoomNumber());

  }

  @Test
  void testRegisterNewPatient() {
    Client shaq = new Client(1, "Shaquille", "O'Neil", "3/6/1972");
    clinic.addClinicClient(shaq);
    assertTrue(clinic.getClinicClients().contains(shaq));
  }

  @Test
  void testSendPatientHomeApproved() {
    Staff lebron = new Staff("physician", "Lebron", "James");
    Client anthony = new Client(1, "Anthony", "Davis", "3/11/1993");
    clinic.addClinicClient(anthony);
    assertTrue(clinic.getClinicClients().contains(anthony));

    clinic.addClinicStaff(lebron);
    assertTrue(clinic.getClinicStaffs().contains(lebron));

    clinic.removeClinicClient(anthony, lebron);
    assertFalse(clinic.getClinicClients().contains(anthony));
  }

  @Test
  void testSendPatientHomeNotApproved() throws IllegalArgumentException {
    Client jerry = new Client(1, "Jerry", "West", "5/28/1938");
    Staff rondo = new Staff("physician", "Rajon", "Rondo");
    clinic.addClinicClient(jerry);
    assertTrue(clinic.getClinicClients().contains(jerry));

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> clinic.removeClinicClient(jerry, rondo), "Removal Has Not Been Approved By Staff");
    assertTrue(thrown.getMessage().contains("Removal Has Not Been Approved By Staff"));

  }

  @Test
  void testSendPatientHomeDoesNotExist() throws IllegalArgumentException {
    Staff rondo = new Staff("physician", "Rajon", "Rondo");
    clinic.addClinicStaff(rondo);
    assertTrue(clinic.getClinicStaffs().contains(rondo));

    Client jerry = new Client(1, "Jerry", "West", "5/28/1938");

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> clinic.removeClinicClient(jerry, rondo), "Client Error, Client Doesn't Exist");
    assertTrue(thrown.getMessage().contains("Client Error, Client Doesn't Exist"));

  }

  @Test
  void testDeactivateStaffMember() {
    Staff raymond = new Staff("physician", "Raymond", "Allen");
    clinic.addClinicStaff(raymond);
    assertTrue(clinic.getClinicStaffs().contains(raymond));

    clinic.removeClinicStaff(raymond);
    assertFalse(clinic.getClinicStaffs().contains(raymond));
  }

  @Test
  void testAssignNewClientToAvailableRoom() {
    Client dwight = new Client(1, "Dwight", "Howard", "12/8/1985");
    clinic.addClinicClient(dwight);
    assertTrue(clinic.getClinicClients().contains(dwight));

    int[] testArray = new int[4];
    for (int i = 0; i < testArray.length; i++) {
      testArray[i] = 11;
    }
    Room examRoom = new Room(testArray, "exam", "Exam_2");
    clinic.addClinicRoom(examRoom);
    assertTrue(clinic.getClinicRooms().contains(examRoom));

    clinic.assignClientToNewRoom(dwight, examRoom);
    assertEquals(6, dwight.getRoomNum());

  }

  @Test
  void testAssignNewClientToWaitingRoom() {
    Client steve = new Client(1, "Steve", "Nash", "2/7/1974");
    clinic.addClinicClient(steve);
    assertTrue(clinic.getClinicClients().contains(steve));

    int[] testArray = new int[4];
    for (int i = 0; i < testArray.length; i++) {
      testArray[i] = 3;
    }
  }

  @Test
  void testAssignNewClientToUnavailableRoom() {
    Client steve = new Client(1, "Steve", "Nash", "2/7/1974");
    clinic.addClinicClient(steve);

    assertTrue(clinic.getClinicClients().contains(steve));
    int[] testArray = new int[4];
    for (int i = 0; i < testArray.length; i++) {
      testArray[i] = 7;
    }
    Room office = new Room(testArray, "Office", "Fidelity Investments");
    clinic.addClinicRoom(office);
    assertTrue(clinic.getClinicRooms().contains(office));

    office.setStatus(true);
    for (Room rooms : clinic.getClinicRooms()) {
      if ("Triage".equals(rooms.getRoomName())) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> clinic.assignClientToNewRoom(steve, rooms), "Room is occupied");
        assertTrue(thrown.getMessage().contains("Room is occupied"));
      }
    }

  }

  @Test
  void testVisitRecordInformation() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    // Prepare the input for the game
    VisitRecord record = new VisitRecord(formattedDateTime, "Fever", new BigDecimal("100.34"));
    VisitRecord record2 = new VisitRecord(formattedDateTime, "Cold", new BigDecimal("102.34"));

    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record);
    phil.getRecordHistory().add(record);
    phil.getRecordHistory().add(record2);
    VisitRecord record3 = new VisitRecord(formattedDateTime, "Headache",
        new BigDecimal("98.45987"));
    phil.getRecordHistory().add(record3);
    clinic.addClinicClient(phil);
    assertEquals(3, phil.getRecordHistory().size());
  }

  @Test
  void testDisplayCorrectCelsiusPrecision() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    // Prepare the input for the game
    VisitRecord record = new VisitRecord(formattedDateTime, "Fever",
        new BigDecimal("100.345678493023"));

    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record);
    phil.getRecordHistory().add(record);

    assertEquals(new BigDecimal("100.35"), phil.getRecord().getBodyTemp());
  }

  @Test
  void testPhysicianTitle() {
    Staff drPhil = new Staff("physician", "Phil", "Askander");
    assertEquals("Dr. Phil", drPhil.getFirstName());
  }

  @Test
  void testNurseTitle() {
    Staff nursePhil = new Staff("nurse", "Phil", "Askander");
    assertEquals("Nurse Phil", nursePhil.getFirstName());

  }

  @Test
  void testAssignStaffToClient() {
    Staff eric = new Staff("physician", "Eric", "Spolstra");
    clinic.addClinicStaff(eric);
    assertTrue(clinic.getClinicStaffs().contains(eric));

    Client lebron = new Client(1, "Lebron", "James", "12/30/1984");
    Client chris = new Client(1, "Chris", "Bosh", "3/24/1984");
    Client dwayne = new Client(1, "Dwayne", "Wade", "1/17/1982");
    clinic.addClinicClient(lebron);
    clinic.addClinicClient(chris);
    clinic.addClinicClient(dwayne);
    assertTrue(clinic.getClinicClients().contains(lebron));
    assertTrue(clinic.getClinicClients().contains(chris));
    assertTrue(clinic.getClinicClients().contains(dwayne));

    ArrayList<Client> testArrayList = new ArrayList<>();
    testArrayList.add(lebron);
    testArrayList.add(chris);
    testArrayList.add(dwayne);

    clinic.assignStaffToClient(eric, lebron);
    clinic.assignStaffToClient(eric, chris);
    clinic.assignStaffToClient(eric, dwayne);
    assertTrue(clinic.getStaffKey().containsKey(eric));
    assertTrue(clinic.getStaffKey().containsValue(testArrayList));

  }

  @Test
  void testDisplayRoomInfoAndCreateFile() {
    Staff doc = new Staff("physician", "Doc", "Rivers");
    clinic.addClinicStaff(doc);
    assertTrue(clinic.getClinicStaffs().contains(doc));

    int[] celticsCoor = new int[4];
    celticsCoor[0] = 20; // Ray Allen
    celticsCoor[1] = 5; // Kevin Garnett
    celticsCoor[2] = 34; // Paul Pierce
    celticsCoor[3] = 9; // Rondo

    int[] knicksCoor = new int[4];
    knicksCoor[0] = 7; // Carmelo Anthony
    knicksCoor[1] = 10; // Walt Frazier
    knicksCoor[2] = 33; // Patrick Ewing
    knicksCoor[3] = 8; // J.R. Smith
    Room tdGarden = new Room(celticsCoor, "Gym", "TD Garden");
    Room msGarden = new Room(knicksCoor, "Gym", "Madison Square Garden");

    clinic.addClinicRoom(tdGarden);
    assertTrue(clinic.getClinicRooms().contains(tdGarden));
    clinic.addClinicRoom(msGarden);
    assertTrue(clinic.getClinicRooms().contains(msGarden));

    Client ray = new Client(1, "Ray", "Allen", "7/20/1975");
    Client kevin = new Client(1, "Kevin", "Garnet", "5/19/1976");
    Client paul = new Client(1, "Paul", "Pierce", "10/13/1977");
    Client rajon = new Client(1, "Rajon", "Rondo", "2/22/1986");
    clinic.addClinicClient(ray);
    clinic.addClinicClient(kevin);
    clinic.addClinicClient(paul);
    clinic.addClinicClient(rajon);
    assertTrue(clinic.getClinicClients().contains(ray));
    assertTrue(clinic.getClinicClients().contains(kevin));
    assertTrue(clinic.getClinicClients().contains(paul));
    assertTrue(clinic.getClinicClients().contains(rajon));

    ArrayList<Client> testArrayList = new ArrayList<>();
    testArrayList.add(ray);
    testArrayList.add(kevin);
    testArrayList.add(paul);
    testArrayList.add(rajon);

    clinic.assignStaffToClient(doc, ray);
    clinic.assignStaffToClient(doc, kevin);
    clinic.assignStaffToClient(doc, paul);
    clinic.assignStaffToClient(doc, rajon);
    assertTrue(clinic.getStaffKey().containsKey(doc));
    assertTrue(clinic.getStaffKey().containsValue(testArrayList));

    clinic.assignClientToNewRoom(ray, tdGarden);
    assertEquals(6, ray.getRoomNum());

    clinic.assignClientToNewRoom(kevin, msGarden);
    assertEquals(7, kevin.getRoomNum());

    String resultOutput = "";
    try {
      FileWriter myWriter = new FileWriter("results.txt");
      myWriter.close();
      resultOutput = "Successfully wrote to the file.";
    } catch (IOException e) {
      resultOutput = "An error occurred.";
      e.printStackTrace();
    }

    assertEquals("Successfully wrote to the file.", resultOutput);
  }
  
  @Test
  void testHasItBeenOneYear() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    // Prepare the input for the game
    VisitRecord record = new VisitRecord(formattedDateTime, "Fever", new BigDecimal("100.34"));
    VisitRecord record2 = new VisitRecord(formattedDateTime, "Cold", new BigDecimal("102.34"));
    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record);
    phil.getRecordHistory().add(record);
    phil.getRecordHistory().add(record2);
    VisitRecord record3 = new VisitRecord(formattedDateTime, "Headache",
        new BigDecimal("98.45987"));
    phil.getRecordHistory().add(record3);
    clinic.addClinicClient(phil);
    assertEquals(3, phil.getRecordHistory().size());
    LocalDateTime referenceDate = LocalDateTime.of(2025, 10, 31, 0, 0);
    String formattedFutureDate = referenceDate.format(formatter);
    VisitRecord record4 = new VisitRecord(formattedFutureDate, "Future Visit",
        new BigDecimal("98.6"));
    phil.getRecordHistory().add(record4);
    assertTrue(phil.isFirstRecord365DaysGreater());

  }

  @Test
  void testHasNotBeenOneYear() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    // Prepare the input for the game
    VisitRecord record = new VisitRecord(formattedDateTime, "Fever", new BigDecimal("100.34"));
    VisitRecord record2 = new VisitRecord(formattedDateTime, "Cold", new BigDecimal("102.34"));

    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record);
    phil.getRecordHistory().add(record);
    phil.getRecordHistory().add(record2);
    VisitRecord record3 = new VisitRecord(formattedDateTime, "Headache",
        new BigDecimal("98.45987"));
    phil.getRecordHistory().add(record3);
    clinic.addClinicClient(phil);
    assertEquals(3, phil.getRecordHistory().size());

    assertFalse(phil.isFirstRecord365DaysGreater());

  }

  @Test
  void testHasHadTwoOrMoreVisitsWithinTheYear() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    // Prepare the input for the game
    VisitRecord record = new VisitRecord(formattedDateTime, "Fever", new BigDecimal("100.34"));
    VisitRecord record2 = new VisitRecord(formattedDateTime, "Cold", new BigDecimal("102.34"));
    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record);
    phil.getRecordHistory().add(record);
    phil.getRecordHistory().add(record2);
    VisitRecord record3 = new VisitRecord(formattedDateTime, "Headache",
        new BigDecimal("98.45987"));
    phil.getRecordHistory().add(record3);
    clinic.addClinicClient(phil);
    assertEquals(3, phil.getRecordHistory().size());
    LocalDateTime referenceDate = LocalDateTime.of(2024, 10, 31, 0, 0);

    String formattedFutureDate = referenceDate.format(formatter);
    VisitRecord record4 = new VisitRecord(formattedFutureDate, "Future Visit",
        new BigDecimal("98.6"));
    phil.getRecordHistory().add(record4);
    assertTrue(phil.twoVisitInYear());
  }

  @Test
  void testDoesNotHaveTwoOrMoreVisitsWithinTheYear() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(formatter);

    // Create visit records for two different years
    LocalDateTime firstVisitDate = now.minusYears(1); // One year ago
    String formattedFirstVisitDate = firstVisitDate.format(formatter);

    VisitRecord record1 = new VisitRecord(formattedFirstVisitDate, "Fever",
        new BigDecimal("100.34"));
    VisitRecord record2 = new VisitRecord(formattedDateTime, "Cold", new BigDecimal("102.34"));

    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record1);
    phil.getRecordHistory().add(record1);
    phil.getRecordHistory().add(record2); // Two visits within the same year
    assertFalse(phil.twoVisitInYear());
  }

  @Test
  void testOnlyHasOneVisitWithinTheYear() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(formatter);

    // Create visit records for two different years
    String formattedFirstVisitDate = formattedDateTime;

    VisitRecord record1 = new VisitRecord(formattedFirstVisitDate, "Fever",
        new BigDecimal("100.34"));
    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record1);
    phil.getRecordHistory().add(record1);
    assertFalse(phil.twoVisitInYear());

  }

  @Test
  void testNoVisitRecordYearVisit() {
    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    assertFalse(phil.twoVisitInYear());

  }

  @Test
  void testIsPatientOneYearInClinic() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    // Prepare the input for the game
    VisitRecord record = new VisitRecord(formattedDateTime, "Fever", new BigDecimal("100.34"));
    VisitRecord record2 = new VisitRecord(formattedDateTime, "Cold", new BigDecimal("102.34"));
    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record);
    phil.getRecordHistory().add(record);
    phil.getRecordHistory().add(record2);
    VisitRecord record3 = new VisitRecord(formattedDateTime, "Headache",
        new BigDecimal("98.45987"));
    phil.getRecordHistory().add(record3);
    clinic.addClinicClient(phil);
    assertEquals(3, phil.getRecordHistory().size());
    assertFalse(phil.isFirstRecord365DaysGreater());
    LocalDateTime referenceDate = LocalDateTime.of(2025, 10, 31, 0, 0);
    String formattedFutureDate = referenceDate.format(formatter);
    VisitRecord record4 = new VisitRecord(formattedFutureDate, "Future Visit",
        new BigDecimal("98.6"));

    phil.getRecordHistory().add(record4);
    assertTrue(phil.isFirstRecord365DaysGreater());

    assertEquals("--------------------------------\n" + "Phil Askander\n"
        + "--------------------------------\n", clinic.isPatientOneYearIn());
  }

  @Test
  public void testDeactivateStaffInClinic() {
    Staff phil = new Staff("physician", "Phil", "Askander");
    clinic.getClinicStaffs().add(phil);
    assertTrue(clinic.getClinicStaffs().contains(phil));

    clinic.removeClinicStaff(phil);
    assertFalse(clinic.getClinicStaffs().contains(phil));
  }

  @Test
  public void testDeactivateStaffWithPatientInClinic() {
    Staff phil = new Staff("physician", "Phil", "Askander");
    clinic.getClinicStaffs().add(phil);
    assertTrue(clinic.getClinicStaffs().contains(phil));

    Client michael = new Client(1, "Michael", "Jordan", "01/06/2001");
    clinic.getClinicClients().add(michael);
    assertTrue(clinic.getClinicClients().contains(michael));

    clinic.assignStaffToClient(phil, michael);
    boolean isAssigned = false;
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if (entry.getKey() == phil && entry.getValue().contains(michael)) {
        isAssigned = true;
      }
    }
    assertTrue(isAssigned);
    isAssigned = false;
    clinic.removeClinicStaff(phil);
    assertFalse(clinic.getClinicStaffs().contains(phil));
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if (entry.getKey() == phil && entry.getValue().contains(michael)) {
        isAssigned = true;
      }
    }
    assertFalse(isAssigned);

  }

  @Test
  public void testDeactivateStaffDoesNotExist() {
    Staff phil = new Staff("physician", "Phil", "Askander");
    assertFalse(clinic.getClinicStaffs().contains(phil));

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> clinic.removeClinicStaff(phil), "Staff Doesn't Exist");
    assertTrue(thrown.getMessage().contains("Staff Doesn't Exist"));

    assertFalse(clinic.getClinicStaffs().contains(phil));
  }

  @Test
  public void testUnassignStaffFromPatient() {
    Staff phil = new Staff("physician", "Phil", "Askander");
    clinic.getClinicStaffs().add(phil);
    assertTrue(clinic.getClinicStaffs().contains(phil));

    Client michael = new Client(1, "Michael", "Jordan", "01/06/2001");
    clinic.getClinicClients().add(michael);
    assertTrue(clinic.getClinicClients().contains(michael));

    clinic.assignStaffToClient(phil, michael);
    boolean isAssigned = false;
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if (entry.getKey() == phil && entry.getValue().contains(michael)) {
        isAssigned = true;
      }
    }
    assertTrue(isAssigned);

    clinic.removeStaffFromClient(phil, michael);
    isAssigned = false;
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if (entry.getKey() == phil && entry.getValue().contains(michael)) {
        isAssigned = true;
      }
    }
    assertFalse(isAssigned);

  }

  @Test
  public void testUnassignedStaffFromPatientDoesNotExist() {
    Staff phil = new Staff("physician", "Phil", "Askander");
    clinic.getClinicStaffs().add(phil);
    assertTrue(clinic.getClinicStaffs().contains(phil));

    Client michael = new Client(1, "Michael", "Jordan", "01/06/2001");
    clinic.getClinicClients().add(michael);
    assertTrue(clinic.getClinicClients().contains(michael));

    boolean isAssigned = false;
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if (entry.getKey() == phil && entry.getValue().contains(michael)) {
        isAssigned = true;
      }
    }
    assertFalse(isAssigned);

    clinic.removeStaffFromClient(phil, michael);
    isAssigned = false;
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if (entry.getKey() == phil && entry.getValue().contains(michael)) {
        isAssigned = true;
      }
    }
    assertFalse(isAssigned);
  }

}
