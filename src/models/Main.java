package models;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStreamReader;

public class Main {
    private static JFrame frame;
    private static JPanel contentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("The Clinic Project");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");

            JMenuItem loadFileItem = new JMenuItem("Load Clinic File");
            loadFileItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadClinicFile();
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

            contentPanel = createAboutPanel();
            frame.getContentPane().add(contentPanel);

            fileMenu.add(loadFileItem);
            fileMenu.add(clearRecordsItem);
            fileMenu.add(quitItem);
            menuBar.add(fileMenu);

            frame.setJMenuBar(menuBar);

            // Comment the line below if you want to show the About dialog on startup
            // showAboutDialog();

            frame.setVisible(true);
        });
    }

    private static void loadClinicFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);

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
        aboutPanel.add(aboutTextPane, BorderLayout.CENTER);

        return aboutPanel;

  }

}
