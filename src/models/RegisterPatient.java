package models;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RegisterPatient extends JFrame {
  private static final long serialVersionUID = 1L;
  private JTextArea outputArea = new JTextArea();

  Clinic clinic;

  public RegisterPatient(Clinic clinic) {

          this.clinic = clinic;
          registerPatient();          

}

  private void registerPatient() {

        try {
            String firstName = getValidUserInput("Please enter the First Name of the client:");
            String lastName = getValidUserInput("Please enter the Last Name of the client:");
            String birthday = getValidUserInput("Please enter the Date Of Birth of the Client in this format (MM/DD/YYYY):");

            String symptoms = getValidUserInput("What symptoms does the patient have?");

            BigDecimal temp = null;
            while (temp == null) {
                try {
                    temp = new BigDecimal(getValidUserInput("And can you tell me what their body temperature is in Celsius?"));
                } catch (NumberFormatException e) {
                    outputArea.append("Error: Invalid input for body temperature. Please enter a valid number.\n");
                }
            }

            boolean isValid = false;
            for (int i = 0; i < clinic.getClinicClients().size(); i++) {
                if (clinic.getClinicClients().get(i).getFirstName().equals(firstName)
                        && clinic.getClinicClients().get(i).getLastName().equals(lastName)) {
                    outputArea.append("This patient is already in the clinic.\n");
                    isValid = true;
                }
            }

            if (!isValid) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm");
                String formattedDateTime = now.format(formatter);
                VisitRecord record = new VisitRecord(formattedDateTime, symptoms, temp);

                Client client = registerClientWithVisitRecord(firstName, lastName, birthday, record, clinic);
                client.setRecord(record);
                client.getRecordHistory().add(record);
            }


            String patientInfoMessage = "Patient Registered:\n" +
                "First Name: " + firstName + "\n" +
                "Last Name: " + lastName + "\n" +
                "Date of Birth: " + birthday + "\n" +
                "Symptoms: " + symptoms + "\n" +
                "Body Temperature: " + temp + "\n";

        JOptionPane.showMessageDialog(this, patientInfoMessage, "Patient Information", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            outputArea.append("Error: " + e.getMessage() + ", please try again.\n");
        }
    
}

// Helper method to get valid user input
private String getValidUserInput(String prompt) {
    String userInput = "";
    while (userInput.trim().isEmpty()) {
        userInput = JOptionPane.showInputDialog(prompt);
    }
    return userInput;
}

public Client registerClientWithVisitRecord(String firstName, String lastName, String birthDay,
    VisitRecord record, Clinic clinic) throws IOException {
  for (int i = 0; i < clinic.getClinicArchives().size(); i++) {
    if (clinic.getClinicArchives().get(i).getFirstName().contains(firstName)
        && clinic.getClinicArchives().get(i).getLastName().contains(lastName)) {
      clinic.getClinicArchives().get(i).setActive(true);
      // clinic.getClinicArchives().get(i).getRecordHistory().add(record);
      clinic.getClinicClients().add(clinic.getClinicArchives().get(i));

      return clinic.getClinicArchives().get(i);
    }
  }
  Registration registration = new Registration(firstName, lastName, birthDay);
  Client client = clinic.registerClient(registration, record);
  client.setRecord(record);
  return client;

}

}
