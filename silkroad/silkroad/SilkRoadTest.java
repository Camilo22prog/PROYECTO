import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SilkRoadTest {

    private SilkRoad silkRoad;

    @BeforeEach
    public void setUp() {
        silkRoad = new SilkRoad(100);
    }

    @Test
    public void testConstructorLength() {
        SilkRoad road = new SilkRoad(50);
        int[][] stores = road.stores();
        assertNotNull(stores);
    }

    @Test
    public void testPlaceStoreSuccess() {
        silkRoad.placeStore(10, 50);
        int[][] stores = silkRoad.stores();
        assertEquals(1, stores.length);
        assertEquals(10, stores[0][0]);
        assertEquals(50, stores[0][1]);
    }

    @Test
    public void testPlaceStoreInvalidLocation() {
        silkRoad.placeStore(-1, 50);
        assertFalse(silkRoad.ok());
    }

    @Test
    public void testPlaceStoreDuplicateLocation() {
        silkRoad.placeStore(10, 50);
        silkRoad.placeStore(10, 30);
        assertFalse(silkRoad.ok());
    }

    @Test
    public void testPlaceRobotSuccess() {
        silkRoad.placeRobot(20);
        int[][] robots = silkRoad.robots();
        assertEquals(1, robots.length);
        assertEquals(20, robots[0][0]);
        assertEquals(0, robots[0][1]); // money collected
    }

    @Test
    public void testPlaceRobotDuplicateLocation() {
        silkRoad.placeRobot(20);
        silkRoad.placeRobot(20);
        assertFalse(silkRoad.ok());
    }

    @Test
    public void testMoveRobotSuccess() {
        silkRoad.placeStore(10, 100);
        silkRoad.placeRobot(5);
        silkRoad.moveRobot(5, 5); // Robot se mueve de 5 a 10
        assertEquals(100, silkRoad.profit());
    }

    @Test
    public void testProfitAfterStoreEmpty() {
        silkRoad.placeStore(10, 100);
        silkRoad.placeRobot(5);
        silkRoad.moveRobot(5, 5); // Mueve de 5 a 10, recoge dinero
        assertEquals(100, silkRoad.profit());
    }

    @Test
    public void testEmptiedStoresAfterCollection() {
        silkRoad.placeStore(10, 100);
        silkRoad.placeRobot(5);
        silkRoad.moveRobot(5, 5); // Mueve de 5 a 10, recoge dinero
        int[][] emptied = silkRoad.emptiedStores();
        assertEquals(1, emptied.length);
        assertEquals(1, emptied[0][1]); // veces vaciada
    }

    @Test
    public void testRobotsReturnsMoneyCollected() {
        silkRoad.placeStore(10, 100);
        silkRoad.placeRobot(5);
        silkRoad.moveRobot(5, 5); // Mueve de 5 a 10, recoge dinero
        int[][] robots = silkRoad.robots();
        assertEquals(100, robots[0][1]); // money collected
    }

    @Test
    public void testRebootRestoresState() {
        silkRoad.placeStore(10, 100);
        silkRoad.placeRobot(5);
        silkRoad.moveRobot(5, 5); // Recoge 100 tenges
        assertEquals(100, silkRoad.profit());

        silkRoad.reboot();
        assertEquals(0, silkRoad.profit());
        int[][] stores = silkRoad.stores();
        assertEquals(100, stores[0][1]); // Restaurado
    }

    @Test
    public void testProfitPerMoveReturnsCorrectFormat() {
        silkRoad.placeStore(10, 100);
        silkRoad.placeRobot(5);
        silkRoad.moveRobot(5, 5); // Mueve de 5 a 10, recoge dinero
        int[][] history = silkRoad.profitPerMove();
        assertTrue(history.length > 0);
        // Ahora cada fila es [location, profit_move_1, profit_move_2, ...]
        // El robot ahora está en 10, no en 5
        assertTrue(history[0].length >= 2); // [location, al menos un movimiento]
        assertEquals(10, history[0][0]); // ubicación del robot después del movimiento
        assertEquals(100, history[0][1]); // ganancia del primer movimiento
    }

    @Test
    public void testMoveRobotToOccupiedLocationFails() {
        silkRoad.placeRobot(10);
        silkRoad.placeRobot(20);
        silkRoad.moveRobot(20, -10); // Intenta mover de 20 a 10 (ocupada)
        assertFalse(silkRoad.ok());
    }
}