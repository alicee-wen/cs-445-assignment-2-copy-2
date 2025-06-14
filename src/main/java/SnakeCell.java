// SnakeCell.java - Represents a single cell in the snake body

public class SnakeCell<T> {
    public int x;
    public int y;
    public T value;

    public SnakeCell(int x, int y, T value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
}