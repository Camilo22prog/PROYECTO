package shapes;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Canvas.java - Panel donde se dibujan todas las figuras.
 * @author MELO - ROZO
   * CICLO 3
 */
public class Canvas extends JPanel {
    private final List<Shape> shapes = new ArrayList<>();

    public Canvas(){
        setPreferredSize(new Dimension(600, 600));
    }

    public void add(Shape s){ shapes.add(s); }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Shape s : shapes) s.draw(g2);
        paintLegend(g2); // ← leyenda SIEMPRE visible
    }
    private void paintLegend(Graphics2D g2) {
        int bx = getWidth() - 110;
        int by = getHeight() - 60;
    
        g2.setColor(Color.BLACK);
        g2.drawString("Tienda", bx + 20, by + 10);
        g2.drawString("Robot", bx + 20, by + 30);
    
        g2.setColor(Color.getHSBColor(0.33f, 1f, 1f)); // verde tienda
        g2.fillRect(bx, by, 15, 15);
    
        g2.setColor(Color.getHSBColor(0.55f, 1f, 0.9f)); // azul robot
        g2.fillOval(bx, by + 20, 15, 15);
    }
}