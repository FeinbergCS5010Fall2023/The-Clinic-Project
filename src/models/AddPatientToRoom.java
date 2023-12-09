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
 * This class implements the ability to add a patient to a new room.
 */
public class AddPatientToRoom extends JFrame {
  private static final long serialVersionUID = 1L;

  private final Clinic clinic;
  private final Room room;

  /**
   * This class implements the ability to add a patient to a new room.
   * @param clinic is the clinic that they are being assigned.
   * @param room is the room that the patient is going to be assigned to.
   */
  public AddPatientToRoom(Clinic clinic, Room room) {
    this.clinic = clinic;
    this.room = room;

    // Set the title of the window
    setTitle("Adding a patient to a room");

    // Set the default close operation
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create a panel to hold the options
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 1));

    // Create a button to discharge a patient
    JButton dischargeButton = new JButton("Assign to a new Room");
    dischargeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        movePatient();
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

  private void movePatient() {
    // Create a dialog for user input
    String dischargeFirstName = (String) JOptionPane.showInputDialog(AddPatientToRoom.this,
        "Please select the patient to move:", "Move Patient", JOptionPane.PLAIN_MESSAGE, null,
        getClinicClientNames().toArray(), null);

    if (dischargeFirstName == null) {
      return; // User canceled the operation
    }
    // Find the selected patient
    Client patient = findClientByName(dischargeFirstName);

    if (patient == null) {
      JOptionPane.showMessageDialog(AddPatientToRoom.this,
          "Patient doesn't exist. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (room.getStatus() == true) {
      JOptionPane.showMessageDialog(AddPatientToRoom.this, room.getRoomName() + " is full.",
          "Room Assignment unSuccessful", JOptionPane.INFORMATION_MESSAGE);

      dispose();
    } else {
      sendPatientToNewRoom(patient, room, clinic);
      JOptionPane.showMessageDialog(AddPatientToRoom.this,
          room.getRoomName() + " now has " + patient.getFirstName() + " " + patient.getLastName(),
          "Room Assignment unSuccessful", JOptionPane.INFORMATION_MESSAGE);

      dispose();
    }

  }

  private void sendPatientToNewRoom(Client patient, Room room2, Clinic clinic) {
    clinic.assignClientToNewRoom(patient, room2);

  }

  // Replace these methods with your actual implementations
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
}
