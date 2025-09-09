import shapes.*;
import javax.swing.*;

/**
 * SilkDisplay.java - Ventana que muestra la ruta y sus elementos.
 * @author TuNombre
 * @version 1.0 (Ciclo 2)
 */
public class SilkDisplay {
    private final JFrame frame;
    private final Canvas canvas;
    private final SquareSpiral spiral;

    public SilkDisplay(int length){
        spiral = new SquareSpiral(length);
        canvas = new Canvas();
        canvas.add(spiral);

        frame = new JFrame("SilkRoad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(canvas);
        frame.pack();
    }

    public void makeVisible(){
        javax.swing.SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}