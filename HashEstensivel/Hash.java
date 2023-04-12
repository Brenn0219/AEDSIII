import java.io.IOException;
import java.io.RandomAccessFile;

public class Hash {
    private int deapth;
    private RandomAccessFile file;
    private final int BUCKET_SIZE = 2524;
    private final int TOTAL_BUCKETS_ELEMENTS = 209;

    public Hash() throws IOException { 
        deapth = 0; 
        file = new RandomAccessFile("hash", "rw");
        file.seek(0);
        file.write(1);
        increaseDepth();
    }

    public void insert(int id, long positionFile) throws IOException {
        Key key = new Key(id, positionFile);
        insert(key);
    }

    private void insert(Key key) throws IOException {
        file.seek(hash(key));
        Buckets bucket = new Buckets(file);

        if(bucket.getTotalElements() < TOTAL_BUCKETS_ELEMENTS) {
            file.skipBytes(bucket.getTotalElements() * Key.SIZE_KEY);
            key.writeKey(file);
            file.seek(bucket.getPositionTotalElements());
            file.writeInt(bucket.getTotalElements() + 1);
        }
    }

    private long hash(Key key) {
        int h = (int)(key.getId() % Math.pow(2, deapth));
        return 4 + BUCKET_SIZE * h-1;
    }

    private void increaseDepth() throws IOException {
        int oldValue = (int)Math.pow(2, deapth++);
        int newValue = (int)Math.pow(2, deapth);
        file.seek(file.length());
        for(int i = oldValue; oldValue <= newValue; i++) {
            Buckets.writeEmptyBucket(file, TOTAL_BUCKETS_ELEMENTS);
        }
    }

    public void show() throws IOException {
        file.seek(0);
        System.out.println("Profundidade Hash: " + file.readInt());

        while(eof(file)) {
            
        }
    }

    private boolean eof(RandomAccessFile file) throws IOException { 
        return file.getFilePointer() < file.length(); 
    }
}
