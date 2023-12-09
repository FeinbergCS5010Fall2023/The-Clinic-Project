package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.Client;
import models.Clinic;
import models.MockClinicController;

import models.Registration;
import models.Room;
import models.Staff;
import models.VisitRecord;

public class MockClinicControllerTest {

  private Clinic clinic;
  private MockClinicController controller;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  public void setUp() {
    clinic = new Clinic("Test Clinic");
    controller = new MockClinicController(new StringReader(""), new StringWriter());
    outputStream = new ByteArrayOutputStream();

  }

  @Test
  public void testRegisterNewStaff() {
    MockClinicController controller = new MockClinicController();
    Staff staff = new Staff("John", "Doe", "Doctor");
    Clinic clinic = new Clinic("Test Clinic");
    Staff result = controller.registerNewStaff(staff, clinic);
    assertEquals(staff, result);
  }

  @Test
  public void testRegisterExistingClientWithVisitRecord() {
    MockClinicController controller = new MockClinicController();
    Client client = new Client(1, "John", "Doe", "1990-01-01");
    VisitRecord record = new VisitRecord("2023-01-01", "Checkup", BigDecimal.valueOf(37.5));
    Clinic clinic = new Clinic("Test Clinic");
    Client result = controller.registerExistingClientWithVisitRecord(client, clinic, record);
    assertEquals(client, result);
  }

  @Test
  public void testDisplayClientInfoWithVisitRecord() {
    MockClinicController controller = new MockClinicController();
    Client client = new Client(1, "John", "Doe", "1990-01-01");
    VisitRecord record = new VisitRecord("2023-01-01", "Checkup", BigDecimal.valueOf(37.5));
    client.setRecord(record);
    Clinic clinic = new Clinic("Test Clinic");
    String result = controller.displayClientInfo(client, clinic);
    String expected = "Mock Client Info: " + client.toString();
    assertEquals(expected, result);
  }

  @Test
  void testSendPatientHome() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    MockClinicController controller = new MockClinicController();
    Client client = new Client(1, "John", "Doe", "1990-01-01");
    Staff raymond = new Staff("physician", "Raymond", "Allen");
    Clinic clinic = new Clinic("Test Clinic");
    clinic.addClinicClient(client);
    clinic.addClinicStaff(raymond);
    controller.sendPatientHome(client, raymond, clinic);
    System.setOut(System.out);
    String expectedOutput = "Mock: Sending patient home - *First Name: John, Last Name: Doe, Date Of Birth: 1990-01-01\n"
        + " Room Number: 1, No Current Record\n";
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testAssignPatientToNewRoom() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    MockClinicController controller = new MockClinicController();
    Room room = new Room(new int[] { 1, 2, 3 }, "Standard", "Room 101");
    Client client = new Client(1, "John", "Doe", "1990-01-01");
    Clinic clinic = new Clinic("Test Clinic");
    clinic.addClinicRoom(room);
    controller.assignPatientToNewRoom(room, client, clinic);
    System.setOut(System.out);
    String expectedOutput = "Mock: Assigning patient to a new room - *First Name: John, Last Name: Doe, Date Of Birth: 1990-01-01\n"
        + " Room Number: 1, No Current Record\n" + "1\n";
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testAssignStaffToPatient() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    MockClinicController controller = new MockClinicController();
    Staff staff = new Staff("John", "Doe", "Doctor");
    Client client = new Client(1, "John", "Doe", "1990-01-01");
    Clinic clinic = new Clinic("Test Clinic");
    boolean result = controller.assignStaffToPatient(clinic, staff, client);
    System.setOut(System.out);
    String expectedOutput = "Mock: Assigning staff to patient - " + staff.toString() + " to "
        + client.toString() + "\n";
    assertEquals(expectedOutput, outputStream.toString());
    assertTrue(result);
  }

  @Test
  public void testRegisterClientWithVisitRecord() throws IOException {
    Clinic mockClinic = new Clinic("Mock Clinic");
    MockClinicController mockController = new MockClinicController();
    String firstName = "John";
    String lastName = "Doe";
    String birthDay = "1990-01-01";
    VisitRecord record = new VisitRecord("2023-01-01", "Checkup", BigDecimal.valueOf(37.5));
    Client result = mockController.registerClientWithVisitRecord(firstName, lastName, birthDay,
        record, mockClinic);
    assertEquals(firstName, result.getFirstName());
    assertEquals(lastName, result.getLastName());
    assertEquals(birthDay, result.getBirthDateTime());
    assertEquals(record, result.getRecord());
  }

  @Test
  public void testHandleAddStaff() throws IOException {
    String userInput = "MockFirstName\nMockLastName\nMockOccupation\n";
    InputStream inputStream = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    System.setOut(new PrintStream(outputStream));
    MockClinicController mockController = new MockClinicController(inputStream, System.out);
    mockController.handleAddStaff(new Clinic("Test")); // You might want to pass a real Clinic
                                                       // instance
    System.setIn(inputStream);
    System.setOut(originalOut);
    String expectedOutput = "Mock: Simulating the addition of a new staff member through console\n"
        + "Mock: Please enter the First Name of the Staff member: MockFirstName\n"
        + "Mock: Please enter the Last Name of the Staff member: MockLastName\n"
        + "Mock: Please enter the occupation of the Staff member: MockOccupation\n"
        + "Mock: Successfully added the following staff member: MockFirstName MockLastName\n" + "";
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  public void testHandleAddPatient() throws IOException {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    MockClinicController mockController = new MockClinicController();
    mockController.handleAddPatient(new Clinic("Test")); // Assuming an empty Clinic instance
    System.setOut(System.out);
    String expectedOutput = "Mock: Simulating the addition of a new patient through console\n"
        + "Mock: Please enter the First Name of the patient: MockPatientFirstName\n"
        + "Mock: Please enter the Last Name of the patient: MockPatientLastName\n"
        + "Mock: Please enter the Birth Day of the patient (YYYY-MM-DD): 2000-01-01\n"
        + "Mock: Successfully added the following patient: *First Name: MockPatientFirstName, Last Name: MockPatientLastName, Date Of Birth: 2000-01-01\n"
        + " Room Number: 0, No Current Record\n" + "";

    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testHandleViewAllStaffMembers() {
    Clinic mockClinic = new Clinic("TEST");
    MockClinicController mockController = new MockClinicController();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    Staff staff1 = new Staff("MockOccupation1", "MockFirstName1", "MockLastName1");
    Client clientA = new Client(1, "MockFirstNameA", "MockLastNameA", "1990-01-01");
    Client clientB = new Client(2, "MockFirstNameB", "MockLastNameB", "1991-02-02");

    mockClinic.assignStaffToClient(staff1, clientA);
    mockClinic.assignStaffToClient(staff1, clientB);

    Staff staff2 = new Staff("MockOccupation2", "MockFirstName2", "MockLastName2");
    Client clientC = new Client(3, "MockFirstNameC", "MockLastNameC", "1992-03-03");

    mockClinic.assignStaffToClient(staff2, clientC);

    try {
      mockController.handleViewAllStaffMembers(mockClinic);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.setOut(System.out);
    String expectedOutput = "Mock: Simulating the view of all staff members through console\n"
        + "Mock: --------------------------------------------------------------------------\n"
        + "Mock: Staff: MockFirstName1 MockLastName1\n" + "Mock: Clients assigned:\n"
        + "Mock: MockLastNameA, MockFirstNameA\n" + "Mock: MockLastNameB, MockFirstNameB\n"
        + "Mock: Total number of assigned patients ever: 2\n"
        + "Mock: Staff: MockFirstName2 MockLastName2\n" + "Mock: Clients assigned:\n"
        + "Mock: MockLastNameC, MockFirstNameC\n"
        + "Mock: Total number of assigned patients ever: 1\n"
        + "Mock: --------------------------------------------------------------------------\n";

    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  public void testHandleYearNoVisit() {
    Clinic mockClinic = new Clinic("TEST");
    MockClinicController mockController = new MockClinicController();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    try {
      mockController.handleYearNoVisit(mockClinic);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.setOut(System.out);
    String expectedOutput = "Mock: Simulating the display of patients with no visits for more than 365 days\n"
        + "Mock: There are no patients that haven't visited the clinic for more than 365 days from today.\n";

    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testDisplayRoomInfo() {
    Room room = new Room(new int[] { 1, 2, 3 }, "Test Room Type", "Test Room Name");
    clinic.addClinicRoom(room);
    Registration registration = new Registration("John", "Doe", "2000-01-01");
    registration.waitingRoomNumberGenerator(clinic); // Assuming this generates the room number
    VisitRecord visitRecord = new VisitRecord("2023-01-01 10:00:00", "Fever",
        new BigDecimal("37.5"));
    Client client = clinic.registerClient(registration, visitRecord);
    Staff staff1 = new Staff("Doctor", "Dr.", "Smith");
    clinic.addClinicStaff(staff1);
    MockClinicController mockController = new MockClinicController();
    clinic.assignClientToNewRoom(client, room);
    String result = mockController.displayRoomInfo(room, clinic);
    String expectedOutput = "Mock: *First Name: John, Last Name: Doe, Date Of Birth: 2000-01-01\n"
        + " Room Number: 1, No Current Record\n" + "Mock: \n" + "";
    assertEquals(expectedOutput, result);
  }

  @SuppressWarnings("unused")
  @Test
  void testHandleAddNewRoom() throws IOException {
    MockClinicController mockController = new MockClinicController();

    mockController.handleAddNewRoom(clinic);
    Room room = new Room(new int[] { 1, 2, 3 }, "Test Room Type", "Test Room Name");
    clinic.addClinicRoom(room);
    assertEquals(1, clinic.getClinicRooms().size());

  }

  @Test
  void testHandleRemovePatient() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    String userInput = "MockPatientFirstName\nMockPatientLastName\n2000-01-01\nMockStaffFirstName\nMockLastName\nMockOccupation\n";
    InputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
    System.setIn(inputStream);
    Clinic clinic = new Clinic("Test Clinic");
    MockClinicController controller = new MockClinicController();
    controller.handleRemovePatient(clinic);
    String expectedOutput = "Mock: Simulating the removal of a patient from the clinic\n"
        + "Mock: Please select the patient to discharge: MockPatientFirstName\n"
        + "Mock: Please select the staff who approved this: Dr. MockStaffFirstName\n"
        + "Mock: Sending patient home - *First Name: MockPatientFirstName, Last Name: MockPatientLastName, Date Of Birth: 2000-01-01\n"
        + " Room Number: 0, No Current Record\n"
        + "Mock: MockPatientFirstName has been discharged, approved by: Dr. MockStaffFirstName"
        + "\n";
    assertEquals(expectedOutput, outputStream.toString());

  }

  @Test
  void testHandleAssignStaffToClient() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    String userInput = "MockPatientFirstName\nMockPatientLastName\nDr. MockStaffFirstName\nMockLastName\nphysician\n";
    InputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
    System.setIn(inputStream);
    Client patient = new Client(1, "MockPatientFirstName", "MockPatientLastName", "2000-01-01");
    clinic.addClinicClient(patient);
    Staff staff = new Staff("physician", "Dr. MockStaffFirstName", "MockLastName");
    clinic.addClinicStaff(staff);
    try {
      controller.handleAssignStaffToClient(clinic);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String expectedOutput = "Mock: Simulating the assignment of staff to a patient through console\n"
        + "Mock: Please select the patient to assign staff: MockPatientFirstName\n"
        + "Mock: Please select the staff who will be assigned to the patient: Dr. MockStaffFirstName\n"
        + "Mock: Assigning staff to patient - Dr. Dr. MockStaffFirstName MockLastName to *First Name: MockPatientFirstName, Last Name: MockPatientLastName, Date Of Birth: 2000-01-01\n"
        + " Room Number: 1, No Current Record\n"
        + "Mock: MockPatientFirstName MockPatientLastName has been assigned to Dr. Dr. MockStaffFirstName MockLastName\n"
        + "";
    assertEquals(expectedOutput, outputStream.toString());
    assertTrue(clinic.getStaffKey().containsKey(staff));
    assertTrue(clinic.getStaffKey().get(staff).contains(patient));
  }

  @Test
  public void testHandleDisplayRoomInfo() throws IOException {
    String input = "MockRoom\nno\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    StringBuilder out = new StringBuilder();
    MockClinicController testInstance = new MockClinicController(in, out);
    Room mockRoom = new Room(new int[] { 1, 2, 3, 4 }, "MockType", "MockRoom");
    Clinic testClinic = new Clinic("Test Clinic");
    testClinic.addClinicRoom(mockRoom);
    testInstance.handleDisplayRoomInfo(testClinic);
    assertTrue(out.toString().contains("Here are the rooms in the clinic:"));
    assertTrue(out.toString().contains("Please enter the name of the room you would like to see:"));
    assertTrue(out.toString().contains("Room Information:"));
    assertTrue(out.toString().contains(mockRoom.getRoomName()));
    assertTrue(out.toString().contains("Do you want to see another room? (yes/no)"));
  }

  @Test
  void testHandleViewStaff() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    Staff staff1 = new Staff("John", "Doe", "Doctor");
    Staff staff2 = new Staff("Jane", "Smith", "Nurse");
    Client client1 = new Client(1, "Alice", "Johnson", "1990-01-01");
    Client client2 = new Client(2, "Bob", "Miller", "1991-02-02");
    clinic.addClinicClient(client2);
    clinic.addClinicClient(client1);
    clinic.addClinicStaff(staff2);
    clinic.addClinicStaff(staff1);
    clinic.assignStaffToClient(staff1, client1);
    clinic.assignStaffToClient(staff2, client2);
    controller.handleViewStaff(clinic);
    String expectedOutput = "--------------------------------------------------------------------------\n"
        + "Staff: Smith Nurse\n" + "Clients assigned: \n" + "\n" + " Miller, Bob\n" + "\n"
        + "Total number of assigned patients ever: 1\n" + "\n" + "Staff: Doe Doctor\n"
        + "Clients assigned: \n" + "\n" + " Johnson, Alice\n" + "\n"
        + "Total number of assigned patients ever: 1\n" + "\n"
        + "--------------------------------------------------------------------------\n" + "";
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testHandleRemoveStaffStaffNotFound() throws IOException {
    String input = "Jane\nDoe\nno\n";
    MockClinicController controller = new MockClinicController();
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    Staff staffToRemove = new Staff("John", "Doe", "Doctor");
    clinic.addClinicStaff(staffToRemove);
    controller.handleRemoveStaff(clinic, "Jane", "Doe");
    assertTrue(clinic.getClinicStaffs().isEmpty());
  }
  
  @Test
  public void testHandleUnassignStaffFromClient() {
      Clinic clinic = new Clinic("Test Clinic");
      MockClinicController controller = new MockClinicController(new StringReader(""), new StringWriter());
      Staff staff = new Staff("StaffFirstName", "StaffLastName", "StaffOccupation");
      Client client = new Client(1, "PatientFirstName", "PatientLastName", "1990-01-01");
      clinic.addClinicStaff(staff);
      clinic.addClinicClient(client);
      clinic.assignStaffToClient(staff, client);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputStream));
      try {
          controller.handleUnassignStaffFromClient(clinic, "PatientFirstName", "PatientLastName", "StaffFirstName", "StaffLastName");
      } catch (IOException e) {
          e.printStackTrace();
      }
      System.setOut(System.out);
      String expectedOutput = "Mock: Simulating unassigning staff from a patient through GUI.\n"
          + "Mock: Unassigning staff from patient...\n"
          + "Able to remove him\n"
          + "\n"
          + "Mock: Successfully unassigned staff from the patient.\n"
          + "";
      assertEquals(expectedOutput, outputStream.toString());
  }
  
  
  
  

}