import java.util.ArrayList;
import java.util.List;

public class SilkRoad {
    private int length;
    private final List<Store> stores = new ArrayList<>();
    private final List<Robot> robots = new ArrayList<>();
    private final SilkDisplay display;

    public SilkRoad(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longitud debe ser positiva.");
        }
        this.length = length;
        this.display = new SilkDisplay(length, stores, robots);
    }

    public SilkRoad(int[][] days) {
        int maxLength = 0;
        for (int[] day : days) {
            if (day[0] > maxLength) {
                maxLength = day[0];
            }
        }
        this.length = maxLength + 10;
        this.display = new SilkDisplay(length, stores, robots);

        for (int[] day : days) {
            int dayNum = day[0];
            int tenges = day[1];

            placeStore(dayNum, tenges);
            placeRobot(dayNum);
        }
    }

    public void placeStore(int location, int tenges) {
        lastOperationOk = false;

        if (!isValidLocation(location) || tenges < 0 || findStoreAt(location) != null) {
            showError("No se puede colocar la tienda en la ubicación " + location);
            return;
        }

        Store newStore = new Store(location, tenges);
        stores.add(newStore);
        display.addStore(newStore);

        updateProfitDisplay();
        lastOperationOk = true;
    }

    public void placeRobot(int location) {
        lastOperationOk = false;

        if (!isValidLocation(location)) {
            showError("Ubicación inválida: " + location);
            return;
        }

        if (findRobotAt(location) != null) {
            showError("Ya existe un robot en la ubicación: " + location);
            return;
        }

        Robot newRobot = new Robot(location);
        robots.add(newRobot);
        display.addRobot(newRobot);

        lastOperationOk = true;
    }

    public void removeStore(int location) {
        lastOperationOk = false;

        Store storeToRemove = findStoreAt(location);
        if (storeToRemove == null) {
            showError("No hay tienda " + location);
            return;
        }

        stores.remove(storeToRemove);
        display.removeStore(storeToRemove);
        updateProfitDisplay();
        lastOperationOk = true;
    }

    public void removeRobot(int location) {
        lastOperationOk = false;

        Robot robotToRemove = findRobotAt(location);
        if (robotToRemove == null) {
            showError("Ubicación inválida" + location);
            return;
        }

        robots.remove(robotToRemove);
        display.removeRobot(robotToRemove);
        lastOperationOk = true;
    }

    public void moveRobot(int location, int meters) {
        lastOperationOk = false;

        Robot robot = findRobotAt(location);
        if (robot == null) {
            showError("No hay robot " + location);
            return;
        }

        int newLocation = location + meters;
        if (!isValidLocation(newLocation)) {
            showError("El robot se movería fuera de los límites");
            return;
        }

        if (findRobotAt(newLocation) != null) {
            showError("Ya existe un robot en la ubicación destino: " + newLocation);
            return;
        }

        processRobotMove(robot, newLocation);

        display.refreshAfterMove();
        updateProfitDisplay();
        updateTopRobotDisplay();
        lastOperationOk = true;
    }

    public void moveRobots() {
        lastOperationOk = false;

        boolean anyRobotEnabled = robots.stream().anyMatch(Robot::isEnabled);
        if (!anyRobotEnabled) {
            showError("Ningún robot tiene movimientos automaticos habilitados");
            return;
        }

        for (Robot robot : robots) {
            if (!robot.isEnabled()) continue;

            int distance = robot.decideBestMovement(getActiveStores(), length);

            if (distance != 0) {
                int newLocation = robot.getLocation() + distance;
                if (isValidLocation(newLocation)) {
                    if (findRobotAt(newLocation) != null) {
                        continue;
                    }
                    processRobotMove(robot, newLocation);
                }
            }
        }

        display.refreshAfterMove();
        updateProfitDisplay();
        updateTopRobotDisplay();
        lastOperationOk = true;
    }


    public void returnRobots() {
        for (Robot robot : robots) {
            robot.reset();
        }
        display.refreshAfterMove();
        lastOperationOk = true;
    }

    public void reboot() {
        lastOperationOk = false;

        for (Store store : stores) {
            store.restock();
        }

        for (Robot robot : robots) {
            robot.reset();
            robot.resetMoney();
        }

        display.clearAll();
        for (Store store : stores) display.addStore(store);
        for (Robot robot : robots) display.addRobot(robot);
        updateProfitDisplay();

        lastOperationOk = true;
    }

    public void resupplyStores() {
        for (Store store : stores) {
            store.restock();
        }
        display.refreshAfterMove();
        updateProfitDisplay();
        lastOperationOk = true;
    }

    public int profit() {
        return robots.stream()
                     .mapToInt(Robot::getCollectedMoney)
                     .sum();
    }

    private boolean lastOperationOk = true;
    public boolean ok() { return lastOperationOk; }

    public int[][] stores() {
        return stores.stream()
            .sorted((s1, s2) -> Integer.compare(s1.getLocation(), s2.getLocation()))
            .map(s -> new int[]{s.getLocation(), s.getTenges()})
            .toArray(int[][]::new);
    }

    public int[][] robots() {
        List<RobotInfoForOutput> robotInfos = new ArrayList<>();
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = robots.get(i);
            robotInfos.add(new RobotInfoForOutput(robot.getLocation(), robot.getCollectedMoney()));
        }

        robotInfos.sort((r1, r2) -> {
            int locationCompare = Integer.compare(r1.location, r2.location);
            return locationCompare != 0 ? locationCompare : Integer.compare(r1.money, r2.money);
        });

        return robotInfos.stream()
            .map(info -> new int[]{info.location, info.money})
            .toArray(int[][]::new);
    }

    public int[][] emptiedStores() {
        return stores.stream()
            .sorted((s1, s2) -> Integer.compare(s1.getLocation(), s2.getLocation()))
            .map(s -> new int[]{s.getLocation(), s.getTimesEmptied()})
            .toArray(int[][]::new);
    }

    /**
     * Consultar historial de ganancias por movimiento de todos los robots.
     * @return int[][] donde cada fila es [location, profit_move_1, profit_move_2, ...]
     *         ordenadas por ubicación de menor a mayor.
     */
    public int[][] profitPerMove() {
        List<int[]> allProfitRecords = new ArrayList<>();

        for (int i = 0; i < robots.size(); i++) {
            Robot robot = robots.get(i);
            List<Robot.MovementRecord> history = robot.getMovementHistory();

            int[] record = new int[history.size() + 1];
            record[0] = robot.getLocation();

            for (int j = 0; j < history.size(); j++) {
                record[j + 1] = history.get(j).getProfitGained();
            }

            allProfitRecords.add(record);
        }

        allProfitRecords.sort((r1, r2) -> Integer.compare(r1[0], r2[0]));

        return allProfitRecords.toArray(new int[0][]);
    }

    public void makeVisible() { display.makeVisible(); }
    public void makeInvisible() { display.makeInvisible(); }
    public void finish() { display.finish(); }

    private boolean isValidLocation(int location) {
        return location >= 0 && location < length;
    }

    private Store findStoreAt(int location) {
        return stores.stream()
            .filter(s -> s.getLocation() == location)
            .findFirst()
            .orElse(null);
    }

    private Robot findRobotAt(int location) {
        return robots.stream()
            .filter(r -> r.getLocation() == location)
            .findFirst()
            .orElse(null);
    }

    private List<Store> getActiveStores() {
        return stores.stream()
            .filter(s -> !s.isEmpty())
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private void updateProfitDisplay() {
        int maxPossibleProfit = stores.stream()
            .mapToInt(Store::getTenges)
            .sum();
        int currentProfit = profit();
        display.updateProfit(currentProfit, Math.max(maxPossibleProfit, 1));
    }

    private void updateTopRobotDisplay() {
        int topRobotIndex = getTopRobotIndex();
        display.setTopRobot(topRobotIndex);
    }

    private int getTopRobotIndex() {
        int topIndex = -1;
        int maxMoney = -1;

        for (int i = 0; i < robots.size(); i++) {
            if (robots.get(i).getCollectedMoney() > maxMoney) {
                maxMoney = robots.get(i).getCollectedMoney();
                topIndex = i;
            }
        }

        return topIndex;
    }

    private void processRobotMove(Robot robot, int newLocation) {
        int distance = newLocation - robot.getLocation();
        robot.move(distance);

        Store storeAtDestination = findStoreAt(newLocation);
        if (storeAtDestination != null && storeAtDestination.canBeEmptied()) {
            int profit = storeAtDestination.empty();
            robot.addMoney(profit);
        }
    }

    private void showError(String message) {
        display.showError(message);
    }

    public static SilkRoad simulateDays(int[][] days) {
        SilkRoad road = new SilkRoad(days.length * 10);

        for (int[] dia : days) {
            int diaNum = dia[0];
            int tenges = dia[1];

            road.placeStore(diaNum, tenges);
            road.placeRobot(diaNum);
        }

        return road;
    }

    private static class RobotInfoForOutput {
        final int location, money;
        RobotInfoForOutput(int location, int money) {
            this.location = location;
            this.money = money;
        }
    }
}