import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SilkRoadContestTest {

    @Test
    public void testSolveReturnsCorrectProfit() {
        int[][] days = {
            {0, 100},
            {2, 150},
            {5, 200}
        };

        int expectedProfit = 450; // 100 + 150 + 200
        int actualProfit = SilkRoadContest.solve(days);

        assertEquals(expectedProfit, actualProfit, "La ganancia calculada debe coincidir con la esperada.");
    }

    @Test
    public void testSolveWithEmptyDays() {
        int[][] days = {};

        int expectedProfit = 0;
        int actualProfit = SilkRoadContest.solve(days);

        assertEquals(expectedProfit, actualProfit, "La ganancia debe ser 0 si no hay días.");
    }

    @Test
    public void testSolveWithSingleDay() {
        int[][] days = {
            {3, 100}
        };

        int expectedProfit = 100;
        int actualProfit = SilkRoadContest.solve(days);

        assertEquals(expectedProfit, actualProfit, "La ganancia debe coincidir con la del único día.");
    }

    @Test
    public void testSimulateRunsWithoutException() {
        int[][] days = {
            {0, 50},
            {1, 100},
            {3, 75}
        };

        // Simular sin pausas
        assertDoesNotThrow(() -> {
            SilkRoadContest.simulate(days, false);
        }, "La simulación no debe lanzar excepciones.");

        // Simular con pausas
        assertDoesNotThrow(() -> {
            SilkRoadContest.simulate(days, true);
        }, "La simulación en modo lento no debe lanzar excepciones.");
    }
}