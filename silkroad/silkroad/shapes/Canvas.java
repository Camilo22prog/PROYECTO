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
    }
}