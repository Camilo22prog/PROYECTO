package shapes;
import java.awt.*;

/**
 * Círculo coloreado.
 * @author MELO-ROZO
 * @CICLO-3
 */
public class Circle extends Shape {
    private final int radius;

    public Circle(int x, int y, int radius){
        this.x = x; this.y = y; this.radius = radius;
    }

    @Override
    public void draw(Graphics2D g2){
        g2.setColor(color);
        g2.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    /** Mueve el círculo a nuevas coordenadas. */
    public void moveTo(int nx, int ny){ x = nx; y = ny; }
}