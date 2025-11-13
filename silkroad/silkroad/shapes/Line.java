package shapes;
import java.awt.*;

/**
 * Line.java - Línea recta entre dos puntos.
 * @author MELO-ROZO
 * @version 1.0
 */
public class Line extends Shape {
    private final int x2, y2;
    public Line(int posx1, int posy1, int posx2, int posy2){
        this.x = posx1; this.y = posy1;
        this.x2 = posx2; this.y2 = posy2;
    }
    @Override
    public void draw(Graphics2D g2){
        g2.setColor(color);
        g2.drawLine(x, y, x2, y2);
    }
}