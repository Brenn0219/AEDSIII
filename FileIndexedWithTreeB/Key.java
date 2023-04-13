public class Key {
    private int id;
    private long positionFile, pointer;

    Key(int id, long positionFile, long pointer) {
        this.id = id;
        this.positionFile = positionFile;
        this.pointer = pointer;
    }

    Key(int id, long positionFile) {
        this(id, positionFile, -1);
    }

    Key() { this(-1, -1, -1); }

    // Setters
    public void setPointer(long pointer) { this.pointer = pointer; }

    // Getters
    public int getId() { return id; }
    public long getPositionFile() { return positionFile; }
    public long getPointer() { return pointer; }
}
