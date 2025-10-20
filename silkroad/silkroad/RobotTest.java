import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RobotTest {

    private Robot robot;

    @BeforeEach
    public void setUp() {
        robot = new Robot(0); // Ubicación inicial 0
    }

    @Test
    public void testRobotInitialLocation() {
        assertEquals(0, robot.getLocation());
    }

    @Test
    public void testRobotInitialMoney() {
        assertEquals(0, robot.getCollectedMoney());
    }

    @Test
    public void testRobotMove() {
        robot.move(5);
        assertEquals(5, robot.getLocation());
    }

    @Test
    public void testRobotAddMoney() {
        robot.addMoney(100);
        assertEquals(100, robot.getCollectedMoney());
    }

    @Test
    public void testRobotReset() {
        robot.move(10);
        robot.addMoney(50);
        robot.reset();
        assertEquals(0, robot.getLocation());
    }

    @Test
    public void testRobotResetMoney() {
        robot.addMoney(100);
        robot.resetMoney();
        assertEquals(0, robot.getCollectedMoney());
    }

    @Test
    public void testRobotMovementHistoryAfterMove() {
        robot.move(10);
        assertEquals(1, robot.getMovementHistory().size());
    }

    @Test
    public void testRobotMovementRecordContainsCorrectData() {
        robot.move(5);
        var history = robot.getMovementHistory();
        assertEquals(0, history.get(0).getFromLocation());
        assertEquals(5, history.get(0).getToLocation());
        assertEquals(5, history.get(0).getDistance());
    }

    @Test
    public void testRobotMovementRecordProfitAfterAddMoney() {
        robot.move(5);
        robot.addMoney(100);
        var history = robot.getMovementHistory();
        assertEquals(100, history.get(0).getProfitGained());
    }

    @Test
    public void testRobotDecideBestMovementReturnsZeroIfNoStores() {
        int decision = robot.decideBestMovement(new ArrayList<>(), 100);
        assertEquals(0, decision);
    }

    @Test
    public void testRobotDecideBestMovementReturnsZeroIfDisabled() {
        robot.enable(false);
        Store store = new Store(10, 100);
        ArrayList<Store> stores = new ArrayList<>();
        stores.add(store);
        int decision = robot.decideBestMovement(stores, 100);
        assertEquals(0, decision);
    }

    @Test
    public void testRobotIsEnabledByDefault() {
        assertFalse(robot.isEnabled()); // Suponiendo que por defecto está deshabilitado
    }

    @Test
    public void testRobotEnableChangesState() {
        robot.enable(true);
        assertTrue(robot.isEnabled());
    }
}