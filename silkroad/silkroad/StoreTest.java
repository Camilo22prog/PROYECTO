import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {

    private Store store;

    @BeforeEach
    public void setUp() {
        store = new Store(10, 200);
    }

    @Test
    public void testStoreInitialLocation() {
        assertEquals(10, store.getLocation());
    }

    @Test
    public void testStoreInitialTenges() {
        assertEquals(200, store.getTenges());
    }

    @Test
    public void testStoreIsEmptyAfterEmpty() {
        store.empty();
        assertTrue(store.isEmpty());
    }

    @Test
    public void testStoreReturnsCorrectAmountWhenEmptied() {
        int amount = store.empty();
        assertEquals(200, amount);
    }

    @Test
    public void testStoreCannotBeEmptiedTwice() {
        store.empty();
        int amount = store.empty();
        assertEquals(0, amount);
    }

    @Test
    public void testStoreRestockRestoresOriginalTenges() {
        store.empty();
        store.restock();
        assertEquals(200, store.getTenges());
        assertFalse(store.isEmpty());
    }

    @Test
    public void testStoreCanNotBeEmptiedIfAlreadyEmpty() {
        store.empty();
        assertFalse(store.canBeEmptied());
    }

    @Test
    public void testStoreCanNotBeEmptiedIfZeroTenges() {
        Store zeroStore = new Store(5, 0);
        assertFalse(zeroStore.canBeEmptied());
    }

    @Test
    public void testStoreTimesEmptiedIncrementsAfterEmpty() {
        store.empty();
        assertEquals(1, store.getTimesEmptied());
        store.restock();
        store.empty();
        assertEquals(2, store.getTimesEmptied());
    }
}