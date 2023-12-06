package models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OptionWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Clinic clinic;

    public OptionWindow(Clinic clinic) {
        this.clinic = clinic;

        // Set the title of the window
        setTitle("Discharge Patient");

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the options
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        // Create a button to discharge a patient
        JButton dischargeButton = new JButton("Discharge Patient");
        dischargeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dischargePatient();
            }
        });

        // Add buttons to the panel
        panel.add(dischargeButton);

        // Add the panel to the content pane
        getContentPane().add(panel, BorderLayout.CENTER);

        // Set the size of the window
        setSize(300, 150);

        // Set the window to be visible
        setVisible(true);
    }

    private void dischargePatient() {
        // Create a dialog for user input
        String dischargeFirstName = (String) JOptionPane.showInputDialog(
                OptionWindow.this,
                "Please select the patient to discharge:",
                "Discharge Patient",
                JOptionPane.PLAIN_MESSAGE,
                null,
                getClinicClientNames().toArray(),
                null);

        if (dischargeFirstName == null) {
            return; // User canceled the operation
        }
        // Find the selected patient
        Client patient = findClientByName(dischargeFirstName);

        if (patient == null) {
            JOptionPane.showMessageDialog(
                    OptionWindow.this,
                    "Patient doesn't exist. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a dialog for user input
        String disChargeStaffFirstName = (String) JOptionPane.showInputDialog(
                OptionWindow.this,
                "Please select the staff who approved this:",
                "Discharge Patient",
                JOptionPane.PLAIN_MESSAGE,
                null,
                getClinicStaffNames().toArray(),
                null);

        if (disChargeStaffFirstName == null) {
            return; // User canceled the operation
        }

        // Find the selected staff member
        Staff staffApproval = findStaffByName(disChargeStaffFirstName);

        if (staffApproval == null || !staffApproval.getFirstName().contains("Dr.")) {
            JOptionPane.showMessageDialog(
                    OptionWindow.this,
                    "Staff doesn't exist or is not approved to discharge patients. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Perform the discharge operation (replace with your actual method)
        sendPatientHome(patient, staffApproval, clinic);

        // Display a message
        JOptionPane.showMessageDialog(
                OptionWindow.this,
                patient.getFirstName() + " has been discharged, approved by: "
                        + disChargeStaffFirstName,
                "Discharge Successful",
                JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
    }

    // Replace these methods with your actual implementations
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

    private List<String> getClinicStaffNames() {
      List <String>docs = new ArrayList<>();
      for(Staff staff : clinic.getClinicStaffs()) {
        if(staff.getOccupation().contains("physician")) {
          docs.add(staff.getFirstName() + " " + staff.getLastName());
        }
      }
      return docs;
      // Replace this with your actual logic to get staff names
        // For now, returning a placeholder list
    }

    private Staff findStaffByName(String firstName) {
      for(Staff staff : clinic.getClinicStaffs()) {
        String name = staff.getFirstName() + " " + staff.getLastName();
        if (name.contains(firstName)) {
          return staff;
        }
      }
      return null;
    }

    private void sendPatientHome(Client patient, Staff staffApproval, Clinic clinic) {
        // Replace this with your actual logic to discharge a patient
        // For now, just printing a message
      clinic.removeClinicClient(patient, staffApproval);  
      System.out.println("Discharging patient: " + patient.getFirstName() + " " + patient.getLastName());
    }


    }

