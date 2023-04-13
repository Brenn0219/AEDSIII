import java.io.IOException;
import java.io.RandomAccessFile;

public class Key {
    private int id;
    private long positionFile;
    public static final int SIZE_KEY = 12;

    Key(int id, long positionFile) {
        this.id = id;
        this.positionFile = positionFile;
    }

    Key() { this(-1, -1); }

    public void writeKey(RandomAccessFile file) throws IOException {
        file.writeInt(id);
        file.writeLong(positionFile);
    }
    
    // Getters
    public int getId() { return id; }
    public long getPositionFile() { return positionFile; }
}
