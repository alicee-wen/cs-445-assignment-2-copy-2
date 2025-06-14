import org.junit.Test;
import static org.junit.Assert.*;

public class SnakeTest {

    @Test(expected = IllegalStateException.class)
    public void testMethodsBeforeInitialize() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.length(); // Should throw IllegalStateException
    }

    @Test
    public void testInitialize() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("H", 0, 0);
        assertEquals(1, snake.length());
        assertEquals("H", snake.getAt(0).value);
    }

    @Test
    public void testGrowAndMove() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("H", 1, 1);
        snake.grow("T");
        snake.move("R");
        assertEquals(2, snake.length());
        assertEquals(2, snake.getAt(0).x);
        assertEquals(1, snake.getAt(1).x);
    }

    @Test
    public void testShrink() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("H", 0, 0);
        snake.grow("T");
        snake.move("D");
        snake.shrink();
        assertEquals(1, snake.length());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidGet() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("X", 0, 0);
        snake.getAt(1);
    }

    @Test
    public void testInsertAndRemoveAt() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("A", 0, 0);
        snake.grow("B");
        snake.move("R");
        snake.insertAt(1, "C");
        assertEquals("C", snake.getAt(1).value);
        snake.removeAt(1);
        assertEquals("B", snake.getAt(1).value);
    }

    @Test
    public void testCollisionDetection() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("A", 5, 5);
        assertTrue(snake.checkCollision(5, 5));
        assertFalse(snake.checkCollision(4, 4));
    }
}