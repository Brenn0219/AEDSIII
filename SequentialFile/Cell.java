public class Cell {
    public Games game;
    private int priority;

    Cell(Games x) {
        game = x;
        priority = 0;
    }

    public void setPriority(int priority) { this.priority = priority; }
    public int getPriority() { return priority; }
}
