import java.io.IOException;
import java.io.RandomAccessFile;

public class Node {
    private long nodeFather, positionOfTotalKeys, firstNodePointer;
    private boolean leaf;
    private int totalKeysInNode;

    // Construtor para quando a pagina so ter o pai
    Node(long nodeFather) {
        this.nodeFather = nodeFather;
        leaf = true;
        positionOfTotalKeys = firstNodePointer = -1;
        totalKeysInNode = 0;
    }

    // Construtor para quando a pagina tem as informacoes
    Node(RandomAccessFile file) throws IOException {
        nodeFather = file.readLong();
        leaf = file.readBoolean();
        positionOfTotalKeys = file.getFilePointer();
        totalKeysInNode = file.readInt();
        firstNodePointer = file.readLong();
    }

    // Metodo para escrever as informacoes da Pagina
    public void write(RandomAccessFile file) throws IOException {
        file.writeLong(nodeFather);
        file.writeBoolean(leaf);
        file.writeInt(totalKeysInNode);
        file.writeLong(firstNodePointer);
    }

    // Metodo para escrever uma Chave
    public void writeKey(RandomAccessFile file, int id, long position) throws IOException {
        file.writeInt(id);
        file.writeLong(position);
        file.writeLong(-1);
        
        file.seek(positionOfTotalKeys);
        totalKeysInNode++; 
        file.writeInt(totalKeysInNode);
    }

    public void overwriteKeys(RandomAccessFile file, int id, long position) throws IOException {
        file.writeInt(id);
        file.writeLong(position);
        file.writeLong(-1);
    }

    public void readKey(RandomAccessFile file) throws IOException {
        file.readInt();
        file.readLong();
        file.readLong();
    }

    public long getNodeFather() { return nodeFather; }
    public long getFirstNodePointer() { return firstNodePointer; }
    public long getPositionOfTotalKeys() { return positionOfTotalKeys; }
    public int getTotalKeysInNode() { return totalKeysInNode; }
    public boolean getLeaf() { return leaf; }
}
