package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board b1 = new Board("b");
    Board b2 = new Board("b");
    Board b3 = new Board("b", "test1");
    Board b4 = new Board("bb");
    Board b5 = this.b1;

    @Test
    void testId(){
        this.b1.setId(1L);
        assertEquals(1L, this.b1.getId());
    }
    @Test
    void testName() {
        assertEquals("b", this.b1.getName());
        this.b1.setName("b1");
        assertEquals("b1", this.b1.getName());
        this.b1.setName("b");
    }

    @Test
    void testPassword() {
        assertNull(this.b1.getPassword());
        assertNotNull(this.b3.getPassword());
        assertEquals("test1", this.b3.getPassword());
        this.b3.setPassword("test2");
        assertNotNull(this.b3.getPassword());
        assertEquals("test2", this.b3.getPassword());
    }

    @Test
    void testHashEquals() {
        this.b1.setId(1L);
        assertEquals(this.b1, this.b1);
        assertNotEquals(this.b1, this.b2);
        assertNotEquals(this.b1, this.b3);
        assertNotEquals(this.b1, this.b4);
        assertEquals(this.b1, this.b5);

        assertEquals(this.b1.hashCode(), this.b1.hashCode());
        assertNotEquals(this.b1.hashCode(), this.b2.hashCode());
        assertNotEquals(this.b1.hashCode(), this.b3.hashCode());
        assertNotEquals(this.b1.hashCode(), this.b4.hashCode());
        assertEquals(this.b1.hashCode(), this.b5.hashCode());
    }

    @Test
    void testToString(){
        assertEquals("Board: b, 0", this.b1.toString());
        assertEquals("Board: bb, 0", this.b4.toString());
        this.b1.setId(1L);
        assertEquals("Board: b, 1", this.b1.toString());
    }
}