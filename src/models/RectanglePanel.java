package models;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;




/**
 * This is the class that creates the rooms for the clinic.
 */
public class RectanglePanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private Clinic clinic;
  private double zoomFactor = 2.5; // Initial zoom factor
  private Point selectedPoint = null;

  /**
   * This constructor takes in one parameter to create the rooms, the clinic.
   * @param clinic is where the rooms are located.
   */
  public RectanglePanel(Clinic clinic) {
    this.clinic = clinic;
    addMouseListener(new ClickListener());
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (Room room : clinic.getClinicRooms()) {
      int x1 = room.getElement(0); // X-coordinate of the top-left corner
      int y1 = room.getElement(1); // Y-coordinate of the top-left corner
      int x2 = room.getElement(2); // X-coordinate of the bottom-right corner
      int y2 = room.getElement(3); // Y-coordinate of the bottom-right corner

      Font newFont = new Font("Arial", Font.PLAIN, 15);
      g.setFont(newFont);

      int scaledX = (int) (x1 * 32 * zoomFactor - 2000);
      int scaledY = (int) (y1 * 32 * zoomFactor + 125);
      int scaledWidth = (int) ((x2 - x1) * 50 * zoomFactor);
      int scaledHeight = (int) ((y2 - y1) * 32 * zoomFactor);

      g.setColor(Color.white);
      g.fillRect(scaledX, scaledY, scaledWidth, scaledHeight);
      g.setColor(Color.BLACK);
      g.drawString(room.getRoomName(), scaledX, scaledY - 5); // Adjusted for room name
      g.setColor(Color.BLACK);
      String res = clinic.displayRoomInfo(room);
      String[] lines = res.split("\n");
      int startY = scaledY - 5; // Adjusted for room name
      for (String line : lines) {
        g.drawString(line, scaledX, startY + 20);
        startY += 20;
      }

      // Highlight selected room
      if (selectedPoint != null
          && pointInRectangle(selectedPoint, scaledX, scaledY, scaledWidth, scaledHeight)) {
        g.setColor(Color.RED);
        g.drawRect(scaledX, scaledY, scaledWidth, scaledHeight);
      }
    }
  }

  private class ClickListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      selectedPoint = new Point(x, y);
      if (SwingUtilities.isRightMouseButton(e)) {
        showPopupMenu(e);
      }
      repaint();
    }
  }

  private void showPopupMenu(MouseEvent e) {
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem addItem = new JMenuItem("Add Patient to Room");
    addItem.addActionListener(actionEvent -> {
      // Implement logic to add patient to the selected room
      // You may want to display another dialog or perform some action here
      JOptionPane.showMessageDialog(null, "Adding patient to the room");
    });
    addItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        handleAssignPatientToRoom(clinic, getSelectedRoom());
      }
    });
    popupMenu.add(addItem);
    popupMenu.show(e.getComponent(), e.getX(), e.getY());
  }

  private Room getSelectedRoom() {
    for (Room room : clinic.getClinicRooms()) {
      int x1 = room.getElement(0);
      int y1 = room.getElement(1);
      int x2 = room.getElement(2);
      int y2 = room.getElement(3);

      int scaledX = (int) (x1 * 32 * zoomFactor - 2000);
      int scaledY = (int) (y1 * 32 * zoomFactor + 125);
      int scaledWidth = (int) ((x2 - x1) * 50 * zoomFactor);
      int scaledHeight = (int) ((y2 - y1) * 32 * zoomFactor);

      if (pointInRectangle(selectedPoint, scaledX, scaledY, scaledWidth, scaledHeight)) {
        return room;
      }
    }
    return null;
  }

  private void handleAssignPatientToRoom(Clinic clinic, Room selectedRoom) {
    if (selectedRoom != null) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          new AddPatientToRoom(clinic, selectedRoom);
        }
      });
    }
  }

  private boolean pointInRectangle(Point point, int x, int y, int width, int height) {
    return point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height;
  }
}
