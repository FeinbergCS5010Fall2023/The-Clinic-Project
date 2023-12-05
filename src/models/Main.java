package models;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStreamReader;

public class Main {

    private static JFrame frame;
    private static JPanel contentPane;

    public static void main(String[] args) {
      
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("The Clinic Project");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JMenuBar menuBar = createMenuBar();
            frame.setJMenuBar(menuBar);

            contentPane = createAboutPanel();
            frame.setContentPane(contentPane);

            frame.setMinimumSize(new Dimension(800, 600)); // Set minimum size

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");


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
        
        JMenuItem loadFileItem = new JMenuItem("Load Clinic File");
        loadFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadClinicFile();
            }
        });
        fileMenu.add(loadFileItem);
        fileMenu.add(clearRecordsItem);
        fileMenu.add(quitItem);

        menuBar.add(fileMenu);

        return menuBar;
    }

    private static JPanel createAboutPanel() {
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BorderLayout());

        JTextPane aboutTextPane = new JTextPane();
        aboutTextPane.setContentType("text/html");
        aboutTextPane.setEditable(false);

        String aboutText = "<html><body style='font-family: Arial, sans-serif; padding: 10px;'>";
        aboutText += "<h2>Welcome to the Clinic Game!</h2>";
        aboutText += "<p>To play the game, click on Files, load the clinic files, and start playing! It's a beautiful day to save lives!<p>";
        aboutText += "<p><b>Credits:</b></p>";
        aboutText += "<ul>";
        aboutText += "<li>Phil Askander - Software Developer</li>";
        aboutText += "<li>Michael Eshak - Software Developer</li>";
        aboutText += "</ul>";
        aboutText += "</body></html>";

        aboutTextPane.setText(aboutText);
        aboutPanel.add(new JScrollPane(aboutTextPane), BorderLayout.CENTER);

        return aboutPanel;
    }

    private static void loadClinicFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Clinic clinic = new Clinic("Cybernetic Implant Clinic"); // Replace with your logic to load the clinic from the file
            Readable input = new InputStreamReader(System.in);
            Appendable output = System.out;
            new ClinicConsoleController(input, output).playNewGame(clinic,selectedFile);
            // You may also want to update the about panel or perform other actions
            contentPane.add(createAboutPanel(), BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
        }
        
    }

    private static void clearRecords() {
        // Add logic to clear records in memory
        // This could involve creating a method in Clinic or ClinicConsoleController
        // to reset or clear the records.
        JOptionPane.showMessageDialog(frame, "Records cleared.", "Records Cleared", JOptionPane.INFORMATION_MESSAGE);
    }
}