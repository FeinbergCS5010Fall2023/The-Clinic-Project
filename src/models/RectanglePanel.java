package models;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class RectanglePanel extends JPanel {
  
    private Clinic clinic;
    
    

    public RectanglePanel(Clinic clinic) {
      this.clinic = clinic;
    }

    private static final long serialVersionUID = 1L;
//    private int[][] rectangles = {
//            {28, 0, 35, 5},
//            {30, 6, 35, 11},
//            {28, 12, 35, 19},
//            {30, 20, 35, 25},
//            {26, 13, 27, 18}
//    };

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        for (Room room : clinic.getClinicRooms()) {
            int x1 = room.getElement(0); // X-coordinate of the top-left corner
            int y1 = room.getElement(1); // Y-coordinate of the top-left corner
            int x2 = room.getElement(2); // X-coordinate of the bottom-right corner
            int y2 = room.getElement(3); // Y-coordinate of the bottom-right corner

            int scaledX1 = scaleCoordinate(x1, panelWidth);
            int scaledY1 = scaleCoordinate(y1, panelHeight);
            int scaledWidth = scaleCoordinate(x2 - x1, panelWidth);
            int scaledHeight = scaleCoordinate(y2 - y1, panelHeight);

            // Draw the rectangles with scaling
            g.drawRect(scaledX1 - 2000, scaledY1, scaledWidth+90, scaledHeight);

            // Draw the room name next to the rectangle
            drawText(g, room.getRoomName(), scaledX1 - 500, scaledY1, scaledWidth, scaledHeight);
            String res = "";
            res = clinic.displayRoomInfo(room);
            String[] lines = res.split("\n");
            int i = 30;
            for (String line : lines) {
              drawText(g, line, scaledX1, scaledY1, scaledWidth, scaledHeight+i);
              i+=30;
            }
        }
    }

    private void drawText(Graphics g, String text, int x, int y, int width, int height) {
      // Set font and color for the text
      Font originalFont = g.getFont();
      Font font = originalFont.deriveFont(Font.PLAIN);
      g.setFont(font);
      g.setColor(Color.BLACK);

      int textX = x + width + 5;
      int textY = y + height / 2;
      // Draw the text
      g.drawString(text, textX-2000, textY);

      // Reset the font to the original after drawing the text
      g.setFont(originalFont);
  }


    
    private int scaleCoordinate(int coordinate, int panelSize) {
      // Adjust the scaling factor as needed
      double scaleFactor = 95; // Example scaling factor

      return (int) (coordinate * scaleFactor);
  }

//        for (int[] coords : rectangles) {
//            int x1 = coords[0];
//            int y1 = coords[1];
//            int x2 = coords[2];
//            int y2 = coords[3];
//
//            // Draw rectangles using the given coordinates
//            g.drawRect(x1*10, y1*10, (x2 - x1)*10, (y2 - y1) * 10);
//        }
}

/*
    for (Map.Entry<Integer, int[]> entry : clinic.getRoomKey().entrySet()) {
        String res = "";
        for (Room room : clinic.getClinicRooms()) {
            int x1 = room.getElement(0); // X-coordinate of the top-left corner
            int y1 = room.getElement(1); // Y-coordinate of the top-left corner
            int x2 = room.getElement(2); // X-coordinate of the bottom-right corner
            int y2 = room.getElement(3); // Y-coordinate of the bottom-right corner

            int rectX = Math.min(x1 * 10, x2 * 10);
            int rectY = Math.min(y1 * 10 + 1, y2 * 10);
            int rectWidth = Math.abs(x2 * 10 - x1 * 10);
            int rectHeight = Math.abs(y2 * 10 - y1 * 10 + 1);

            if (entry.getValue() == room.getId()) {
                graphics.setColor(Color.BLACK);
                graphics.drawRect(rectX * 10 - 2250, rectY * 10 + 100, rectWidth * 15, rectHeight * 10);
                graphics.drawString(room.getRoomName(), rectX * 10 - 2250 + 1, rectY * 10 + 120);
                res = clinic.displayRoomInfo(room);
                String[] lines = res.split("\n");
                int startY = rectY * 10 + 120;
                for (String line : lines) {
                    graphics.drawString(line, rectX * 10 + -2250 + 1, startY + 20);
                    startY += 20; // Increase Y-coordinate to skip a line
                }
            }
        }
        
    }
*/