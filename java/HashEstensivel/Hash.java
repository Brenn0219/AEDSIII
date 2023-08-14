import java.io.IOException;
import java.io.RandomAccessFile;

public class Hash {
    private int deapth;
    private RandomAccessFile file;
    private final int TOTAL_BUCKETS_ELEMENTS = 10;
    private final int BUCKET_SIZE = Long.BYTES + Integer.BYTES + Integer.BYTES + TOTAL_BUCKETS_ELEMENTS * (Integer.BYTES + Long.BYTES);

    public Hash() throws IOException { 
        deapth = 0; 
        file = new RandomAccessFile("hash", "rw");
        file.seek(4);
        increaseDepth();
    }

    public void insert(int id, long positionFile) throws IOException {
        Key key = new Key(id, positionFile);
        insert(key);
    }

    private void insert(Key key) throws IOException {
        file.seek(hash(key.getId()));
        Buckets bucket = new Buckets(file);

        if(bucket.getTotalElements() < TOTAL_BUCKETS_ELEMENTS) {
            file.skipBytes(bucket.getTotalElements() * Key.SIZE_KEY);
            key.writeKey(file);
            file.seek(bucket.getPosition());
            bucket.setTotalElements(bucket.getTotalElements() + 1);
            bucket.writeBuckte(file);
        } else {
            keyRedistribution(bucket.getPosition());
        }
    }

    public long read(int id) throws IOException {
        int prof = 0;
        while(prof <= deapth) {
            file.seek(hash(id, prof));
            Buckets bucket = new Buckets(file);

            if(bucket.getDepthLocation() == prof) {
                for(int j = 0; j < bucket.getTotalElements(); j++) {
                    if(id == file.readInt()) {
                        return file.readLong(); 
                    } else {
                        file.readLong();
                    }
                }
            }  
            prof++;
        }

        return -1;
    }

    private long hash(int id) {
        return hash(id, deapth);
    }

    private long hash(int id, int p) {
        int h = (int)((id + (id % 13)) % ((int)(Math.pow(2, p))));
        return 4 + BUCKET_SIZE * h;
    }

    private void keyRedistribution(long pos) throws IOException {
        increaseDepth();
        file.seek(pos);
        Buckets bucket = new Buckets(file);
        Key[] keys = new Key[TOTAL_BUCKETS_ELEMENTS];
        
        for(int i = 0; i < TOTAL_BUCKETS_ELEMENTS; i++) {
            keys[i] = new Key(file.readInt(), file.readLong());
        } 

        file.seek(bucket.getPosition());
        bucket.setDepthLocation(deapth);
        bucket.setTotalElements(0);
        bucket.writeBuckte(file);

        for(int i = 0; i < TOTAL_BUCKETS_ELEMENTS; i++) {
            insert(keys[i]);
        }
    }

    private void increaseDepth() throws IOException {
        int oldValue = (int)Math.pow(2, deapth++);
        int newValue = (int)Math.pow(2, deapth);
        file.seek(0);
        file.writeInt(deapth);
        file.seek(file.length());
        for(int i = oldValue; i < newValue; i++) {
            Buckets.writeEmptyBucket(file, TOTAL_BUCKETS_ELEMENTS, deapth);
        }
        Buckets.writeEmptyBucket(file, TOTAL_BUCKETS_ELEMENTS, deapth);
    }

    public void show() throws IOException {
        file.seek(0);
        int n = 0;
        System.out.println("Profundidade Hash: " + file.readInt());

        while(eof(file)) {
            System.out.println();
            System.out.println("Position Buckets: " + file.readLong());
            System.out.println("Deapth: " + file.readInt());
            n = file.readInt();
            System.out.println("Total Elements: " + n);
            for(int i = 0; i < TOTAL_BUCKETS_ELEMENTS; i++) {
                if(i < n) {
                    System.out.println("Id: " + file.readInt() + " PositionFile: " + file.readLong());
                } else { file.readInt(); file.readLong(); }
            }
        }
    }

    private boolean eof(RandomAccessFile file) throws IOException { 
        return file.getFilePointer() < file.length(); 
    }
}
