package models;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class defines the implementation for unassignning a staff member to a patient.
 */
public class UnassignStaffToPatient extends JFrame {
  private static final long serialVersionUID = 1L;
  private Clinic clinic;

  /**
   * The constructor takes in the clinic as a parameter.
   * @param clinic is where the unassignment is taking place.
   */
  public UnassignStaffToPatient(Clinic clinic) {
    this.clinic = clinic;

    // Set the title of the window
    setTitle("Unassign Patient from Staff");

    // Set the default close operation
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create a panel to hold the options
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 1));

    // Create a button to discharge a patient
    JButton unassignButton = new JButton("Unassign Patient from Staff");
    unassignButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        unassignPatient();
      }

    });

    // Add buttons to the panel
    panel.add(unassignButton);

    // Add the panel to the content pane
    getContentPane().add(panel, BorderLayout.CENTER);

    // Set the size of the window
    setSize(300, 150);

    // Center the window on the screen
    setLocationRelativeTo(null);

    // Set the window to be visible
    setVisible(true);

  }

  private void unassignPatient() {
    // Create a dialog for user input
    String dischargeFirstName = (String) JOptionPane.showInputDialog(UnassignStaffToPatient.this,
        "Please select a patient:", "Discharge Patient", JOptionPane.PLAIN_MESSAGE, null,
        getClinicClientNames().toArray(), null);

    if (dischargeFirstName == null) {
      return; // User canceled the operation
    }
    // Find the selected patient
    Client patient = findClientByName(dischargeFirstName);

    if (patient == null) {
      JOptionPane.showMessageDialog(UnassignStaffToPatient.this,
          "Patient doesn't exist. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Create a dialog for user input
    if (getPatientStaffs(patient).size() < 1) {
      JOptionPane.showMessageDialog(UnassignStaffToPatient.this,
          patient.getFirstName() + " " + patient.getLastName()
              + " has no clinical staff members assigned to him.",
          "Unassignment Unsuccessful", JOptionPane.INFORMATION_MESSAGE);
      dispose();
    } else {
      String disChargeStaffFirstName = (String) JOptionPane.showInputDialog(
          UnassignStaffToPatient.this,
          "Here is the list of staff members assigned to: " + patient.getFirstName() + " "
              + patient.getLastName(),
          "Discharge Patient", JOptionPane.PLAIN_MESSAGE, null, getPatientStaffs(patient).toArray(),
          null);

      // Find the selected staff member
      Staff staffApproval = findStaffByName(disChargeStaffFirstName);

      if (staffApproval == null || !staffApproval.getFirstName().contains("Dr.")) {
        JOptionPane.showMessageDialog(UnassignStaffToPatient.this,
            "Staff doesn't exist or is not approved to discharge patients. Please try again.",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Perform the discharge operation (replace with your actual method)
      unassignStaffFromPatient(patient, staffApproval, clinic);

      // Display a message
      JOptionPane.showMessageDialog(
          UnassignStaffToPatient.this, patient.getFirstName() + " " + patient.getLastName()
              + " Has been removed from " + disChargeStaffFirstName,
          "Discharge Successful", JOptionPane.INFORMATION_MESSAGE);

      dispose();

    }
  }

  private void unassignStaffFromPatient(Client patient, Staff staffApproval, Clinic clinic2) {
    for (Staff staff : clinic.getClinicStaffs()) {
      if (staff.getFirstName().contains(staff.getFirstName())
          && staff.getLastName().contains(staff.getLastName())) {
        clinic.removeStaffFromClient(staff, patient);
      }
    }
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

  private List<String> getPatientStaffs(Client client) {
    List<String> docs = new ArrayList<>();
    for (Entry<Staff, ArrayList<Client>> entry : clinic.getStaffKey().entrySet()) {
      for (int i = 0; i < entry.getValue().size(); i++) {
        String staffAssigned = "";
        if (entry.getValue().contains(client)) {
          staffAssigned = entry.getKey().getFirstName() + " " + entry.getKey().getLastName();
          docs.add(staffAssigned);
        }
      }
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

}
