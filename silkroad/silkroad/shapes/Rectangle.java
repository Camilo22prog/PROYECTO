package shapes;
import java.awt.*;

/**
 * Rectangle.java - Cuadrado coloreado.
 * @author MELO-ROZO
 * @version CICLO-7
 */
public class Rectangle extends Shape {
    private final int side;

    public Rectangle(int x, int y, int side){
        this.x = x; this.y = y; this.side = side;
    }

    @Override
    public void draw(Graphics2D g2){
        g2.setColor(color);
        g2.fillRect(x, y, side, side);
    }
}