package models;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("The Clinic Project");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");

            JMenuItem loadFileItem = new JMenuItem("Load Clinic File");
            loadFileItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadClinicFile(frame);
                }
            });

            JMenuItem clearRecordsItem = new JMenuItem("Clear Records");
            clearRecordsItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clearRecords();
                }
            });

            JMenuItem quitItem = new JMenuItem("Quit");
            quitItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            fileMenu.add(loadFileItem);
            fileMenu.add(clearRecordsItem);
            fileMenu.add(quitItem);
            menuBar.add(fileMenu);

            frame.setJMenuBar(menuBar);

            // Show the "About" dialog on startup
            showAboutDialog(frame);

            frame.setVisible(true);
        });
    }

    private static void loadClinicFile(JFrame parentFrame) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(parentFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Readable input = new InputStreamReader(System.in);
            Appendable output = System.out;
            new ClinicConsoleController(input, output).playGame(new Clinic("Test"), selectedFile);
        }
    }

    private static void clearRecords() {
        // Add logic to clear records in memory
        // This could involve creating a method in Clinic or ClinicConsoleController
        // to reset or clear the records.
        System.out.println("Records cleared.");
    }

    private static void showAboutDialog(JFrame parentFrame) {
        JOptionPane.showMessageDialog(parentFrame,
                "Welcome to the Clinic Game!\n\n" +
                        "Credits:\n" +
                        "Phil Askander - Software Developer\n" +
                        "Michael Eshak - Software Developer \n",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
