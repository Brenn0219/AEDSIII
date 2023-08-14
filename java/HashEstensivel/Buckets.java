import java.io.IOException;
import java.io.RandomAccessFile;

public class Buckets {
    private long position;
    private int depthLocation, totalElements, totalBuckets;

    Buckets(int totalBuckets) {
        this.totalBuckets = totalBuckets;
        position = -1;
        depthLocation = totalElements = 0;
    }

    Buckets(RandomAccessFile file) throws IOException {
        position = file.readLong();
        depthLocation = file.readInt();
        totalElements = file.readInt();
    }

    public void writeBuckte(RandomAccessFile file) throws IOException {
        position = file.getFilePointer();
        file.writeLong(position);
        file.writeInt(depthLocation);
        file.writeInt(totalElements);
    }

    public static void writeEmptyBucket(RandomAccessFile file, int n, int depth) throws IOException {
        file.writeLong(file.getFilePointer());
        file.writeInt(depth);
        file.writeInt(0);

        for(int i = 0; i < n; i++) {
            file.writeInt(0);
            file.writeLong(-1);
        }
    }

    public void setDepthLocation(int depthLocation) { this.depthLocation = depthLocation; }
    public void setTotalElements(int totalElements) { this.totalElements = totalElements; }

    public long getPosition() { return position; }
    public int getDepthLocation() { return depthLocation; }
    public int getTotalElements() { return totalElements; }
}
