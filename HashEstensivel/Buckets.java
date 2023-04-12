import java.io.IOException;
import java.io.RandomAccessFile;

public class Buckets {
    private long position, positionTotalElements;
    private int depthLocation, totalElements, totalBuckets;

    Buckets(int totalBuckets) {
        this.totalBuckets = totalBuckets;
        position = positionTotalElements = -1;
        depthLocation = totalElements = 0;
    }

    Buckets(RandomAccessFile file) throws IOException {
        position = file.readLong();
        depthLocation = file.readInt();
        positionTotalElements = file.getFilePointer();
        totalElements = file.readInt();
    }

    public static void writeEmptyBucket(RandomAccessFile file, int n) throws IOException {
        file.writeLong(-1);
        file.writeInt(0);
        file.writeInt(0);

        for(int i = 0; i < n; i++) {
            file.writeInt(0);
            file.writeLong(-1);
        }
    }

    public long getPosition() { return position; }
    public int getDepthLocation() { return depthLocation; }
    public int getTotalElements() { return totalElements; }
    public long getPositionTotalElements() { return positionTotalElements; }
}
