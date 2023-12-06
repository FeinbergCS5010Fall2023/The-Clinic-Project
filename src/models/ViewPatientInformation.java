package models;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ViewPatientInformation extends JFrame{
  private static final long serialVersionUID = 1L;

  private final Clinic clinic;
  public ViewPatientInformation(Clinic clinic) {
    this.clinic = clinic;

    // Set the title of the window
    setTitle("See Patient Information");

    // Set the default close operation
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create a panel to hold the options
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 1));

    // Create a button to discharge a patient
    JButton viewAllButton = new JButton("View Patient Info");
    viewAllButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            findPatient();
        }


    });
    // Add buttons to the panel
    panel.add(viewAllButton);

    // Add the panel to the content pane
    getContentPane().add(panel, BorderLayout.CENTER);

    // Set the size of the window
    setSize(300, 150);

 // Center the window on the screen
    setLocationRelativeTo(null);

    // Set the window to be visible
    setVisible(true);
  }
  private void findPatient() {
 // Create a dialog for user input
    String patientName = (String) JOptionPane.showInputDialog(
        ViewPatientInformation.this,
            "Please select the patient:",
            "Patient",
            JOptionPane.PLAIN_MESSAGE,
            null,
            getClinicClientNames().toArray(),
            null);

    if (patientName == null) {
        return; // User canceled the operation
    }
    // Find the selected patient
    Client patient = findClientByName(patientName);

    if (patient == null) {
        JOptionPane.showMessageDialog(
            ViewPatientInformation.this,
                "Patient doesn't exist. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    JOptionPane.showMessageDialog(
        ViewPatientInformation.this,
            "First Name: " + patient.getFirstName() + "\n" +
            "Last Name: " + patient.getLastName()+ "\n" +
            "Date of Birth: " + patient.getBirthDateTime() + "\n"
            + getVisitRecord(patient),
        "Discharge Successful",
        JOptionPane.INFORMATION_MESSAGE);

dispose();
   
    
  }
  
  private String getVisitRecord(Client patient) {
    String res = "";
    for (int i = 0; i < patient.getRecordHistory().size(); i++) {
      res += patient.getRecordHistory().get(i).toString();
    }
    if (patient.getRecord() == null) {
      res += patient.getFirstName() + ' ' + patient.getLastName()
          + " does not have a medical record with us.\n";
    }
    return res;
  }
  private List<String> getClinicClientNames() {
    List<String> names = new ArrayList<>();
      // Replace this with your actual logic to get client names
      // For now, returning a placeholder list
    for(Client client : clinic.getClinicClients()) {
      names.add(client.getFirstName() + " "  + client.getLastName());
    }
      return names;
  }

  private Client findClientByName(String firstName) {
      for(Client client : clinic.getClinicClients()) {
        String name = client.getFirstName() + " " + client.getLastName();
        if (name.contains(firstName)) {
          return client;
        }
      }
      return null;
  }
}
