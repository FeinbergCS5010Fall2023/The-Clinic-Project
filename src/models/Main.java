package models;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

/**
 * This is the driver class that is run for the program to begin.
 */
public class Main {

  private static JFrame frame;
  private static JPanel contentPane;

  /**
   * This is the main function.
   * @param args isn't used for our main function.
   */
  public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {
      frame = new JFrame("The Clinic Project");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JMenuBar menuBar = createMenuBar();
      frame.setJMenuBar(menuBar);

      contentPane = createAboutPanel();
      frame.setContentPane(contentPane);

      frame.setMinimumSize(new Dimension(500, 500)); // Set minimum size

      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    });
  }

  private static JMenuBar createMenuBar() {

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
    JMenu fileMenu = new JMenu("File");
    fileMenu.add(loadFileItem);
    fileMenu.add(quitItem);
    JMenuBar menuBar = new JMenuBar();
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
    aboutText += "<p>To play the game, click on Files, load the clinic files, "
        + "and start playing! It's a beautiful day to save lives!<p>";
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
      Clinic clinic = new Clinic("Cybernetic Implant Clinic"); 
      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;
      new ClinicConsoleController(input, output).playNewGame(clinic, selectedFile);
      contentPane.add(createAboutPanel(), BorderLayout.CENTER);
      frame.revalidate();
      frame.repaint();
    }

  }

}