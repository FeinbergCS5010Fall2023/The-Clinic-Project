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

/**
 * This class implements the ability to assign a clinical staff member to a patient.
 */
public class AssignStaffToPatient extends JFrame {
  private static final long serialVersionUID = 1L;

  private final Clinic clinic;

  /**
   * The constructor takes two parameters and initializes the frame of the functionality.
   * @param clinic is the clinic where this is all taking place.
   */
  public AssignStaffToPatient(Clinic clinic) {
    this.clinic = clinic;

    // Set the title of the window
    setTitle("Assign Staff to Patient");

    // Set the default close operation
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create a panel to hold the options
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 1));

    // Create a button to discharge a patient
    JButton dischargeButton = new JButton("Assign Staff To Patient");
    dischargeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        findPatient();
      }
    });

    // Add buttons to the panel
    panel.add(dischargeButton);

    // Add the panel to the content pane
    getContentPane().add(panel, BorderLayout.CENTER);

    // Set the size of the window
    setSize(300, 150);

    setLocationRelativeTo(null);

    // Set the window to be visible
    setVisible(true);
  }

  private void findPatient() {
    // Create a dialog for user input
    String patientName = (String) JOptionPane.showInputDialog(AssignStaffToPatient.this,
        "Please select the patient:", "Patient", JOptionPane.PLAIN_MESSAGE, null,
        getClinicClientNames().toArray(), null);

    if (patientName == null) {
      return; // User canceled the operation
    }
    // Find the selected patient
    Client patient = findClientByName(patientName);

    if (patient == null) {
      JOptionPane.showMessageDialog(AssignStaffToPatient.this,
          "Patient doesn't exist. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    // Create a dialog for user input
    String disChargeStaffFirstName = (String) JOptionPane.showInputDialog(AssignStaffToPatient.this,
        "Please select the staff member who will be assigned to " + patient.getFirstName(),
        "Discharge Patient", JOptionPane.PLAIN_MESSAGE, null, getClinicStaffNames().toArray(),
        null);

    if (disChargeStaffFirstName == null) {
      return; // User canceled the operation
    }

    // Find the selected staff member
    Staff staffApproval = findStaffByName(disChargeStaffFirstName);

    if (staffApproval == null) {
      JOptionPane.showMessageDialog(AssignStaffToPatient.this,
          "Staff doesn't exist or is not approved to discharge patients. Please try again.",
          "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (!makeTheAssignment(patient, staffApproval, clinic)) {
      JOptionPane.showMessageDialog(AssignStaffToPatient.this,
          patient.getFirstName() + " " + patient.getLastName() + " is already assigned to "
              + disChargeStaffFirstName,
          "Assignment Unsuccessful", JOptionPane.INFORMATION_MESSAGE);
    } else {

      // Display a message
      JOptionPane.showMessageDialog(
          AssignStaffToPatient.this, patient.getFirstName() + " " + patient.getLastName()
              + " has been assigned to " + disChargeStaffFirstName,
          "Assignment Successful", JOptionPane.INFORMATION_MESSAGE);
    }
    dispose();

  }

  private List<String> getClinicClientNames() {
    List<String> names = new ArrayList<>();
    // Replace this with your actual logic to get client names
    // For now, returning a placeholder list
    for (Client client : clinic.getClinicClients()) {
      names.add(client.getFirstName() + " " + client.getLastName());
    }
    return names;
  }

  private Client findClientByName(String firstName) {
    for (Client client : clinic.getClinicClients()) {
      String name = client.getFirstName() + " " + client.getLastName();
      if (name.contains(firstName)) {
        return client;
      }
    }
    return null;
  }

  private List<String> getClinicStaffNames() {
    List<String> docs = new ArrayList<>();
    for (Staff staff : clinic.getClinicStaffs()) {
      docs.add(staff.getFirstName() + " " + staff.getLastName());
    }
    return docs;
    // Replace this with your actual logic to get staff names
    // For now, returning a placeholder list
  }

  private Staff findStaffByName(String firstName) {
    for (Staff staff : clinic.getClinicStaffs()) {
      String name = staff.getFirstName() + " " + staff.getLastName();
      if (name.contains(firstName)) {
        return staff;
      }
    }
    return null;
  }

  private boolean makeTheAssignment(Client patient, Staff staff, Clinic clinic) {
    if (!clinic.assignStaffToClient(staff, patient)) {
      return false;
    }
    return true;
  }
}
