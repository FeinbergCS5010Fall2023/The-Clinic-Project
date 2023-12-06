package models;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class RectanglePanel extends JPanel {
  
    private Clinic clinic;
    private double zoomFactor = 2.5; // Initial zoom factor
    private JFrame frame;

    

    public RectanglePanel(Clinic clinic) {
      this.clinic = clinic;
      addMouseWheelListener(new ZoomListener());

    }

    private static final long serialVersionUID = 1L;

    
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

            int scaledX = (int) (x1 * 32 * zoomFactor - 250);
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
        }
    }
    private class ZoomListener implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int notches = e.getWheelRotation();
            if (notches < 0) {
                zoomFactor *= 1.1; // Zoom in
            } else {
                zoomFactor /= 1.1; // Zoom out
            }
            repaint();
        }
    }

}