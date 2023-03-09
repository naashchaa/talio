package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board b1 = new Board("b");
    Board b2 = new Board("b");
    Board b3 = new Board("b", "test1");
    Board b4 = new Board("bb");

    @Test
    void getName() {
        assertEquals("b", b1.getName());
        b1.setName("b1");
        assertEquals("b1", b1.getName());
        b1.setName("b");
    }

    @Test
    void getPassword() {
        assertNull(b1.getPassword());
        assertNotNull(b3.getPassword());
        assertEquals("test1", b3.getPassword());
        b3.setPassword("test2");
        assertNotNull(b3.getPassword());
        assertEquals("test2", b3.getPassword());
    }

    @Test
    void getTaskLists() {
        assertEquals(0, b1.getTaskLists().size());
//        test for setting a list needed
    }

    @Test
    void testEquals() {
        assertTrue(b1.equals(b1));
        assertTrue(b1.equals(b2));
        assertFalse(b1.equals(b4));
        assertFalse(b1.equals(b3));
    }

    @Test
    void testHashCode() {
        assertEquals(b1.hashCode(), b1.hashCode());
        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b1.hashCode(), b3.hashCode());
        assertNotEquals(b1.hashCode(), b4.hashCode());
    }
}