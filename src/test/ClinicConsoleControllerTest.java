package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map.Entry;
import models.Client;
import models.Clinic;
import models.ClinicConsoleController;
import models.ClinicController;
import models.Registration;
import models.Room;
import models.Staff;
import models.VisitRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClinicConsoleControllerTest {

  private Clinic clinic;
  private ClinicConsoleController controller;

  /**
   * This class tests the functionality of all the methods in the clinicController.
   */

  @BeforeEach
  public void setUp() {
    clinic = new Clinic("Test Clinic");
    controller = new ClinicConsoleController(new StringReader(""), new StringWriter());
  }

  @Test
  public void testAddNewPatient() {
    // ClinicFunctionality clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "1\n" + "Phil\n" + "Askander\n" + "01/06/2001\n" + "Fever\n" + "99\n"
        + "no\n 16"; // Enter "no" to not register another patient; // Choose option 16 (Quit)

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);

    for (Client patient : clinic.getClinicClients()) {
      if ("Phil".equals(patient.getFirstName()) && "Askander".equals(patient.getLastName())) {
        assertTrue(clinic.getClinicClients().contains(patient));
      }
    }

  }

  @Test
  public void testAddNewPatientInvalidParameter() {
    // ClinicFunctionality clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "1\n" + "Phil\n" + "Askander\n" + "October\n" + "01/06/2001\n" + "Fever\n"
        + "99\n" + "no\n 16\n"; // Enter "no" to not register another patient; // Choose option 16

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);

    assertTrue(gameLog.toString().contains("Please enter a valid date in MM/dd/yyyy format."));
  }

  @Test
  public void testAddNewStaff() {
    // ClinicFunctionality clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "2\n" + "Phil\n" + "Askander\n" + "physician\n" + "16\n";

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);

    for (Staff staffs : clinic.getClinicStaffs()) {
      if ("Phil".equals(staffs.getFirstName()) && "Askander".equals(staffs.getLastName())) {
        assertTrue(clinic.getClinicStaffs().contains(staffs));
      }
    }

  }

  @Test
  public void testAddNewStaffThatExists() {
    // ClinicFunctionality clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "2\n" + "Phil\n" + "Askander\n" + "physician\n" + "2\n" + "Phil\n" + "Askander\n"
        + "physician\n" + "16\n";

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);

    assertTrue(gameLog.toString().contains("This staff member already exists"));

  }

  @Test
  public void testInValidStaffOccupation() {
    // t clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "2\n" + "Phil\n" + "Askander\n" + "manager\n + principle\n" + "physician\n"
        + "16\n"; // Enter the
    // Room
    // Number

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    assertTrue(gameLog.toString().contains("Dr. Phil Askander"));

  }

  @Test
  public void testRemovePatientApproved() {
    // t clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "3\n" + "Fatima\n" + "Follicle\n" + "Amy\n + Anguish\n" + "16\n";

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    for (Client patients : clinic.getClinicClients()) {
      if (patients.getFirstName().contains("Fatima")
          && patients.getLastName().contains("Follicle")) {
        assertTrue(!patients.isActive());
        assertTrue(!(clinic.getClinicClients().contains(patients)));
      }
    }

  }

  @Test
  public void testRemovePatientUnapproved() {
    // t clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "3\n" + "Fatima\n" + "Follicle\n" + "Phil\n + Askander\n" + "no\n" + "16\n";
    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    for (Client patients : clinic.getClinicClients()) {
      if (patients.getFirstName().contains("Fatima")
          && patients.getLastName().contains("Follicle")) {
        assertTrue(patients.isActive());
        assertTrue(clinic.getClinicClients().contains(patients));
        assertTrue(patients.getAssignedStaff().size() == 0);

      }
    }
  }

  @Test
  public void testAssignStaffToClient() {
    // t clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n + Anguish\n" + "no\n" + "16\n";

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);

    assertTrue(gameLog.toString()
        .contains("First Name: Fatima\n" + "Last Name: Follicle\n" + " -Date Of Birth: 6/6/1986\n"
            + " -Room Number: 5\n" + " -No Current Record\n" + "Dr. Amy Anguish"));
  }

  @Test
  public void testAssignInvalidStaff() {
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Phil\n" + "Askander\n" + "no\n" + "16\n";
    // the
    // Room
    // Number

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if ("Fatima".equals(entry.getKey().getFirstName())
          && "Follicle".equals(entry.getKey().getLastName())) {
        assertEquals(0, entry.getValue().size());
      }

    }
  }

  @Test
  public void testPatientDoesNotExist() {

    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "4\n" + "Phil\n" + "Askander\n" + "no\n" + "16\n"; // Enter
    // the Room Number

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    assertTrue(gameLog.toString().contains("Patient doesn't exist. Do you want to try again?"));
  }

  @Test
  public void testDuplicateStaffToPatient() {
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "4\n"
        + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "16\n"; // Enter
    // the Room Number

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    assertTrue(
        gameLog.toString().contains("Dr. Amy Anguish is already assigned to Fatima Follicle"));
  }

  @Test
  public void testAssignPatientToNewRoom() {
    // ClinicFunctionality clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");
    int[] id = new int[4];
    for (int i = 0; i < 3; i++) {
      id[i] = i;
    }

    // Room room = new Room(id, "Gym", "LifeTime");
    // Prepare the input for the game
    String newRoom = "6\n" + "LifeTime\n" + "Gym\n" + "Amy\n" + "Anguish\n";
    String input = newRoom + "1\n" + "Phil\n" + "Askander\n" + "01/06/2001\n" + "headache\n"
        + "99\n" + "no\n" + "5\n" + "Phil\n" + "Askander\n" + "LifeTime\n" + "no\n" + "16\n";
    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);

    // Assert the expected output
    for (Client patient : clinic.getClinicClients()) {
      if ("Phil".equals(patient.getFirstName()) && "Askander".equals(patient.getLastName())) {

        assertEquals(6, patient.getRoomNum());
      }
    }
  }

  @Test
  public void testAssignPatientToOccupiedRoom() {

    // ClinicFunctionality clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");
    int[] id = new int[4];
    for (int i = 0; i < 3; i++) {
      id[i] = i;
    }

    Room room = new Room(id, "Gym", "LifeTime");
    clinic.addClinicRoom(room);
    // Prepare the input for the game
    String input = "5\n" + "Fatima\n" + "Follicle\n" + "Triage\n" + "no\n" + "16\n";

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);

  }

  @Test
  public void testAssignPatientToAvailableRoom() {
    File file = new File("clinic-2.txt");

    int[] id = new int[4];
    for (int i = 0; i < 3; i++) {
      id[i] = i;
    }

    String newRoom = "6\n" + "LifeTime\n" + "Gym\n" + "Amy\n" + "Anguish\n";
    String input = newRoom + "5\n" + "Fatima\n" + "Follicle\n" + "LifeTime\n" + "no\n" + "16\n";

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    // Client fatima = null;
    // Client aandi = null;
    assertTrue(gameLog.toString().contains("Empty"));
    for (Client patients : clinic.getClinicClients()) {
      if ("Fatima".equals(patients.getFirstName()) && "Follicle".equals(patients.getLastName())) {
        assertEquals(6, patients.getRoomNum());
      }
    }
  }

  @Test
  public void testRemovePatientFromRoomAndAddAnotherToSameRoom() {
    // ClinicFunctionality clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");
    String newRoom = "6\n" + "LifeTime\n" + "Gym\n" + "Amy\n" + "Anguish\n";

    String input = newRoom + "5\n" + "Fatima\n" + "Follicle\n" + "LifeTime\n" + "no\n" + "5\n"
        + "Aandi\n" + "Acute\n" + "Surgical\n" + "no\n" + "16\n";

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    // Client fatima = null;
    // Client aandi = null;
    for (Client patients : clinic.getClinicClients()) {
      if ("Fatima".equals(patients.getFirstName()) && "Follicle".equals(patients.getLastName())) {

        assertEquals(6, patients.getRoomNum());
      }
      if ("Aandi".equals(patients.getFirstName()) && "Acute".equals(patients.getLastName())) {

        assertEquals(5, patients.getRoomNum());
      }
    }

    // Assert the expected output
  }

  @Test
  public void testDisplayClientInfo() {
    // Create test data
    Registration register1 = new Registration("John", "Doe", "01/06/2001");
    VisitRecord record1 = new VisitRecord("1/1/2023", "Fever", new BigDecimal("99.23"));

    Client client1 = clinic.registerClient(register1, record1);

    String expected = "First Name: John, Last Name: Doe, Date Of Birth: "
        + "01/06/2001, Room Number: 0, No Current Record\n";
    String result = controller.displayClientInfo(client1, clinic);
    // result += controller.displayClientInfo(client2, clinic);

    assertEquals(expected, result);
  }

  @Test
  public void testDisplayRoomInfo() {
    // t clinic = new Clinic("Test"); // Assuming you have a Clinic instance
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "8\n" + "Triage\n" + "no\n" + "16\n"; // Enter the Room Number

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);

    // Assert the expected output
    assertTrue(gameLog.toString().contains("Triage"));
  }

  @Test
  public void testViewPatientRecordHistoryValid() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    // Prepare the input for the game
    VisitRecord record = new VisitRecord(formattedDateTime, "Fever", new BigDecimal("160.34"));
    VisitRecord record2 = new VisitRecord(formattedDateTime, "Cold", new BigDecimal("162.34"));

    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record);
    phil.getRecordHistory().add(record);
    phil.getRecordHistory().add(record2);
    VisitRecord record3 = new VisitRecord(formattedDateTime, "Headache",
        new BigDecimal("98.45987"));
    phil.getRecordHistory().add(record3);
    clinic.addClinicClient(phil);
    // the Room Number
    String input = "9\n" + "Phil\n" + "Askander\n" + "no\n" + "16\n"; // Enter

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertEquals(3, phil.getRecordHistory().size());

  }

  @Test
  public void testViewPatientWithOutRecordHistory() {
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "9\n" + "Fatima\n" + "Follicle\n" + "no\n" + "16\n"; // Enter
    // the Room Number

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    assertTrue(
        gameLog.toString().contains("Fatima Follicle does not have a medical record with us."));

  }

  @Test
  public void testViewPatientHistoryWithPatientDoesNotExist() {
    File file = new File("clinic-2.txt");

    // Prepare the input for the game
    String input = "9\n" + "Phil\n" + "Askander\n" + "no\n" + "16\n"; // Enter
    // the Room Number

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    c.playGame(clinic, file);
    boolean isValid = false;
    for (Client clients : clinic.getClinicClients()) {
      if (clients.getFirstName().contains("Phil") && clients.getLastName().contains("Askander")) {
        isValid = true;
      }
    }
    assertEquals(false, isValid);
    assertTrue(
        gameLog.toString().contains("Patient doesn't exist. Do you want to try again? (yes/no)"));

  }

  @Test
  public void testViewChiefComplaintInRoom() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    // Prepare the input for the game
    VisitRecord record = new VisitRecord(formattedDateTime, "Fever", new BigDecimal("160.34"));

    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record);
    phil.getRecordHistory().add(record);

    clinic.addClinicClient(phil);
    // the Room Number
    String input = "8\n" + "Triage\n" + "no\n" + "16\n"; // Enter

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    // Start the game
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertTrue(gameLog.toString().contains(
        "First Name: Phil, Last Name: Askander, Date Of Birth: 01/06/2001, Room Number: 1, Fever"));
  }

  @Test
  public void testViewAllPatientsWhoHaveBeenInForMorThanOneYear() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
    String formattedDateTime = now.format(formatter);
    // Prepare the input for the game
    VisitRecord record = new VisitRecord(formattedDateTime, "Fever", new BigDecimal("160.34"));
    VisitRecord record2 = new VisitRecord(formattedDateTime, "Cold", new BigDecimal("162.34"));
    Client phil = new Client(1, "Phil", "Askander", "01/06/2001");
    phil.setRecord(record);
    phil.getRecordHistory().add(record);
    phil.getRecordHistory().add(record2);
    VisitRecord record3 = new VisitRecord(formattedDateTime, "Headache",
        new BigDecimal("98.45987"));
    phil.getRecordHistory().add(record3);
    clinic.addClinicClient(phil);
    assertEquals(3, phil.getRecordHistory().size());
    LocalDateTime referenceDate = LocalDateTime.of(2025, 1, 31, 0, 0);
    String formattedFutureDate = referenceDate.format(formatter);
    VisitRecord record4 = new VisitRecord(formattedFutureDate, "Future Visit",
        new BigDecimal("98.6"));
    phil.getRecordHistory().add(record4);
    assertTrue(phil.isFirstRecord365DaysGreater());

    String input = "13\n" + "16\n"; // Enter

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertTrue(gameLog.toString().contains("Phil Askander"));
  }

  @Test
  public void testViewAllPatientsWhoHaveNotBeenInForMorThanOneYear() {
    String input = "13\n" + "16\n"; // Enter
    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertFalse(gameLog.toString().contains("----------------\nPhil Askander\n----------------\n"));

  }

  @Test
  public void testDisplayOfStaffMembers() {

    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "10\n"
        + "16\n";

    StringBuilder gameLog = new StringBuilder();
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertTrue(gameLog.toString().contains("Dr. Amy Anguish"));

  }

  @Test
  public void testDisplayOfStaffMembersWithNoPatients() {
    StringBuilder gameLog = new StringBuilder();
    String input = "10\n" + "16\n"; // Enter

    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertFalse(gameLog.toString().contains("Dr. Amy Anguish"));
    assertFalse(gameLog.toString().contains("Dr. Benny Bruise"));
    assertFalse(gameLog.toString().contains("Nurse Camila Crisis"));
    assertFalse(gameLog.toString().contains("Nurse Denise Danger"));
    assertFalse(gameLog.toString().contains("Nurse Evan Emergency"));
    assertFalse(gameLog.toString().contains("Frank Febrile"));

  }

  @Test
  public void testDeactivateStaff() {
    Staff phil = new Staff("physician", "Phil", "Askander");
    clinic.getClinicStaffs().add(phil);
    assertTrue(clinic.getClinicStaffs().contains(phil));

    StringBuilder gameLog = new StringBuilder();
    String input = "11\n" + "Phil\n" + "Askander\n" + "no\n" + "16\n"; // Enter

    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);

    assertFalse(clinic.getClinicStaffs().contains(phil));

  }

  @Test
  public void testDeactivateStaffDoesNotExist() {
    StringBuilder gameLog = new StringBuilder();
    String input = "11\n" + "Phil\n" + "Askander\n" + "no\n" + "16\n"; // Enter

    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertEquals(5, clinic.getClinicStaffs().size());

  }

  @Test
  public void testDisplayAllStaff() {
    StringBuilder gameLog = new StringBuilder();
    String input = "15\n" + "16\n"; // Enter

    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertTrue(gameLog.toString().contains("Dr. Amy Anguish"));
    assertTrue(gameLog.toString().contains("Dr. Benny Bruise"));
    assertTrue(gameLog.toString().contains("Nurse Camila Crisis"));
    assertTrue(gameLog.toString().contains("Nurse Denise Danger"));
    assertTrue(gameLog.toString().contains("Nurse Evan Emergency"));
    assertTrue(gameLog.toString().contains("Frank Febrile"));
  }

  @Test
  public void testDisplayAllStaffAfterRemovalOfAmyAnguish() {
    StringBuilder gameLog = new StringBuilder();
    String input = "11\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n" + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertFalse(gameLog.toString().contains("Dr. Amy Anguish"));
    assertTrue(gameLog.toString().contains("Dr. Benny Bruise"));
    assertTrue(gameLog.toString().contains("Nurse Camila Crisis"));
    assertTrue(gameLog.toString().contains("Nurse Denise Danger"));
    assertTrue(gameLog.toString().contains("Nurse Evan Emergency"));
    assertTrue(gameLog.toString().contains("Frank Febrile"));
  }

  @Test
  public void testDisplayAllStaffAfterAddingDrPhil() {
    StringBuilder gameLog = new StringBuilder();
    String input = "2\n" + "Phil\n" + "Askander\n" + "physician\n" + "15\n" + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);

    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    assertTrue(gameLog.toString().contains("Dr. Phil Askander"));
    assertTrue(gameLog.toString().contains("Dr. Amy Anguish"));
    assertTrue(gameLog.toString().contains("Dr. Benny Bruise"));
    assertTrue(gameLog.toString().contains("Nurse Camila Crisis"));
    assertTrue(gameLog.toString().contains("Nurse Denise Danger"));
    assertTrue(gameLog.toString().contains("Nurse Evan Emergency"));
    assertTrue(gameLog.toString().contains("Frank Febrile"));
  }

  /**
   * This test checks to see if it the method of displaying the total number of patients for a
   * physician works correctly.
   */
  @Test
  public void testNumPatientsEverPresent() {
    StringBuilder gameLog = new StringBuilder();
    String input = "15\n" + "16\n"; // Enter
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);
    Staff denise = null;
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains("Denise") && staff.getLastName().contains("Danger")) {
        denise = staff;
      }
    }
    assertEquals(0, denise.getNumPatients());
    assertTrue(gameLog.toString().contains("Total number of assigned patients ever: 0"));

  }

  /**
   * This test checks to see if it the method of displaying the total number of patients for a
   * physician works correctly after a patient has been assigned.
   */
  @Test
  public void testNumPatientsEverAfterAssignment() {
    StringBuilder gameLog = new StringBuilder();
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n"
        + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);
    Staff denise = null;
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains("Amy") && staff.getLastName().contains("Anguish")) {
        denise = staff;
      }
    }
    assertEquals(1, denise.getNumPatients());
    assertTrue(gameLog.toString().contains("Total number of assigned patients ever: 1"));

  }

  /**
   * This test checks to see if it the method of displaying the total number of patients for a
   * physician works correctly after a patient has been assigned and discharged.
   */
  @Test
  public void testNumPatientsEverAfterAssignmentAndDischarge() {
    StringBuilder gameLog = new StringBuilder();
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n"
        + "3\n" + "Fatima\n" + "Follicle\n" + "Amy\n + Anguish\n" + "15\n" + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);
    Staff denise = null;
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains("Amy") && staff.getLastName().contains("Anguish")) {
        denise = staff;
      }
    }
    assertTrue(gameLog.toString().contains("Total number of assigned patients ever: 1"));
    assertEquals(1, denise.getNumPatients());
    assertTrue(gameLog.toString().contains("Total number of assigned patients ever: 0"));
  }

  /**
   * This test assigns a physician to a patient, discharges that patient, and reassigns that same
   * patient to the same physician. We want this to return 1 and not 2 because it's the same
   * patient. Since it's the same patient being assigned to the same physician, he has already been
   * accounted for.
   */
  @Test
  public void testNumPatientsEverAfterAssignmentDischargeAndReassignmentOfSamePatient() {
    StringBuilder gameLog = new StringBuilder();
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n"
        + "3\n" + "Fatima\n" + "Follicle\n" + "Amy\n + Anguish\n" + "15\n" + "1\n" + "Fatima\n"
        + "Follicle\n" + "01/06/2001\n" + "Fever\n" + "99\n" + "no\n" + "4\n" + "Fatima\n"
        + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n" + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);
    Staff denise = null;
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains("Amy") && staff.getLastName().contains("Anguish")) {
        denise = staff;
      }
    }

    assertTrue(gameLog.toString().contains("Total number of assigned patients ever: 1"));
    assertEquals(1, denise.getNumPatients());

  }

  /**
   * This test checks to see if it the method of displaying the total number of patients for a
   * physician works correctly after a patient has been assigned and discharged and a different
   * patient gets assigned to that physician.
   */
  @Test
  public void testNumPatientsEverAfterAssignmentDischargeAndReassignmentOfDifferentPatient() {
    StringBuilder gameLog = new StringBuilder();
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n"
        + "3\n" + "Fatima\n" + "Follicle\n" + "Amy\n + Anguish\n" + "15\n" + "1\n" + "Phil\n"
        + "Askander\n" + "01/06/2001\n" + "Fever\n" + "99\n" + "no\n" + "4\n" + "Phil\n"
        + "Askander\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n" + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);
    Staff denise = null;
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains("Amy") && staff.getLastName().contains("Anguish")) {
        denise = staff;
      }
    }
    assertTrue(gameLog.toString().contains("Total number of assigned patients ever: 0"));
    assertTrue(gameLog.toString().contains("Total number of assigned patients ever: 1"));
    assertTrue(gameLog.toString().contains("Total number of assigned patients ever: 2"));
    assertEquals(2, denise.getNumPatients());
  }

  /**
   * This test checks if the method to unassign staff members from patients works.
   */
  @Test
  public void testUnassignStaffMemberFromPatient() {
    StringBuilder gameLog = new StringBuilder();
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "12\n"
        + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n" + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);
    Staff denise = null;
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains("Amy") && staff.getLastName().contains("Anguish")) {
        denise = staff;
      }
    }

    assertEquals(1, denise.getNumPatients());
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if (entry.getKey() == denise) {
        assertEquals(0, entry.getValue().size());
      }
    }
  }

  /**
   * This test checks if the method to unassign staff members from patients works and is able to
   * assign the same patient to the same physician without any problems.
   */
  @Test
  public void testUnassignedStaffMemberFromPatientAndReassignSamePatient() {
    StringBuilder gameLog = new StringBuilder();
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "12\n"
        + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "4\n" + "Fatima\n"
        + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n" + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);
    Staff denise = null;
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains("Amy") && staff.getLastName().contains("Anguish")) {
        denise = staff;
      }
    }

    assertEquals(1, denise.getNumPatients());
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if (entry.getKey() == denise) {
        assertEquals(1, entry.getValue().size());
      }
    }
  }

  /**
   * This test checks if the method to unassign staff members from patients works and is able to
   * assign a new patient to the same physician and remove that patient correctly.
   */

  @Test
  public void testUnassignedStaffMmberFromPatientAndReassignDifferentPatientAndRemoveThatPatient() {
    StringBuilder gameLog = new StringBuilder();
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "12\n"
        + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "4\n" + "Fatima\n"
        + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "15\n" + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);
    Staff denise = null;
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains("Amy") && staff.getLastName().contains("Anguish")) {
        denise = staff;
      }
    }

    assertEquals(1, denise.getNumPatients());
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      if (entry.getKey() == denise) {
        assertEquals(1, entry.getValue().size());
      }
    }
  }

  /**
   * This test checks if the game generates a map.
   */
  @Test
  void testDisplayGame() {
    StringBuilder gameLog = new StringBuilder();
    String input = "4\n" + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "12\n"
        + "Fatima\n" + "Follicle\n" + "Amy\n" + "Anguish\n" + "no\n" + "4\n" + "Aandi\n" + "Acute\n"
        + "Amy\n" + "Anguish\n" + "no\n" + "12\n" + "Aandi\n" + "Acute\n" + "Amy\n" + "Anguish\n"
        + "no\n" + "15\n" + "16\n";
    ClinicController c = new ClinicConsoleController(new StringReader(input), gameLog);
    File file = new File("clinic-2.txt");
    c.playGame(clinic, file);
    // Call the displayGame method

    // Check if the file "clinic_representation.png" is created
    Path filePath = Paths.get("clinic_representation.png");
    assertTrue(filePath.toFile().exists());
  }

}
