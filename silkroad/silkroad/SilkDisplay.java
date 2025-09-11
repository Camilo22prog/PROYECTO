import shapes.*;
import javax.swing.*;


/**
 * SilkDisplay.java - Ventana que muestra la ruta, tiendas y robots.
 * @author MELO-ROZO
 * @version CICLO-3
 */
public class SilkDisplay {

    private final JFrame frame;
    private final Canvas canvas;
    private final SquareSpiral spiral;

    /**
     * Crea el panel gráfico con la espiral base.
     * @param length longitud total de la ruta (nº de pasos).
     */
    public SilkDisplay(int length) {
        spiral = new SquareSpiral(length);
        canvas= new shapes.Canvas();
        canvas.add(spiral);

        frame = new JFrame("SilkRoad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(canvas);
        frame.pack();
    }

    /**
     * Muestra la ventana (thread-safe).
     */
    public void makeVisible() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    /* ========== MÉTODOS CICLO-3 ========== */

    /**
     * Añade un círculo que representa una tienda.
     * @param s tienda con posición y color.
     */
    public void addStore(Store s) {
        int[] xy = spiral.toXY(s.getLocation());
        Circle c = new Circle(xy[0], xy[1], 10);
        c.changeColor(s.getColor());
        canvas.add(c);
        canvas.repaint();
    }

    /**
     * Añade un círculo que representa un robot.
     * @param r robot con posición y color.
     */
    public void addRobot(Robot r) {
        int[] xy = spiral.toXY(r.getLocation());
        Circle c = new Circle(xy[0], xy[1], 10);
        c.changeColor(r.getColor());
        canvas.add(c);
        canvas.repaint();
    }

    /**
     * Libera la ventana y sus recursos.
     */
    public void dispose() {
        frame.dispose();
    }
}