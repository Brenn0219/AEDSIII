import java.io.IOException;
import java.io.RandomAccessFile;

public class Page {
    private long fatherPage, firstPagePointer, positionIndexedFile;
    private boolean leaf;
    private int totalKeysInPage, indexes;
    private Key[] keys;
    
    Page(int order) {
        fatherPage = firstPagePointer = -1;
        positionIndexedFile = 0;
        leaf = true;
        totalKeysInPage = 0;
        indexes = order-1;
        keys = new Key[indexes];
        for(int i = 0; i < indexes; i++) keys[i] = new Key();
    }

    Page(int order, long fatherPage, boolean leaf, int totalKeysInPage, long firstPagePointer) {
        positionIndexedFile = 0;
        this.fatherPage = fatherPage;
        this.leaf = leaf;
        this.totalKeysInPage = totalKeysInPage;
        this.firstPagePointer = firstPagePointer;
        indexes = order-1;
        keys = new Key[indexes];
        for(int i = 0; i < indexes; i++) keys[i] = null;
    }

    // Metodo de Ler uma Pagina
    public void readPage(RandomAccessFile file) throws IOException {
        positionIndexedFile = file.getFilePointer();
        fatherPage = file.readLong();
        leaf = file.readBoolean();
        totalKeysInPage = file.readInt();
        firstPagePointer = file.readLong();

        for(int i = 0; i < totalKeysInPage; i++) {
            keys[i] = new Key(file.readInt(), file.readLong(), file.readLong());
        }
    }

    // Metodo para Escrever uma Página
    public void writePage(RandomAccessFile file) throws IOException {
        positionIndexedFile = file.getFilePointer();
        file.writeLong(fatherPage);
        file.writeBoolean(leaf);
        file.writeInt(totalKeysInPage);
        file.writeLong(firstPagePointer);

        for(int i = 0; i < indexes; i++) {
            file.writeInt(keys[i].getId());
            file.writeLong(keys[i].getPositionFile());
            file.writeLong(keys[i].getPointer()); 
        }

        updateChildrenPointer(file);
    }  
    
    // Metodo para Adicionar uma nova Chave no Array e no Arquivo e tambem Ordena o Array
    public void addKey(Key key) throws IOException {
        keys[totalKeysInPage] = key;
        quick(0, totalKeysInPage++);
    }

    // Metodo para ordenar o Array de Chaves
    private void quick(int left, int right) {
        int i = left, j = right;
        int pivot = keys[(left + right)/2].getId();

        while(i <= j) {
            while(keys[i].getId() < pivot) i++;
            while(keys[j].getId() > pivot) j--;

            if(i <= j) {
                Key temp = keys[i];
                keys[i] = keys[j];
                keys[j] = temp;
                temp = null;

                i++;
                j--;
            }
        }

        if(left < j) quick(left, j);
        if(i < right) quick(i, right);
    }

    private void updateChildrenPointer(RandomAccessFile file) throws IOException {

        if(firstPagePointer != -1) {
            file.seek(firstPagePointer);
            file.writeLong(positionIndexedFile);
        } 

        for(int i = 0; i < totalKeysInPage; i++) {
            if(keys[i].getPointer() != -1) {
                file.seek(keys[i].getPointer());
                file.writeLong(positionIndexedFile);
            }
        } 
    }

    // Setters
    public void setFather(long fatherPage) { this.fatherPage = fatherPage; }
    public void setFirstPointer(long firstPagePointer) { this.firstPagePointer = firstPagePointer; }
    public void setLeaf(boolean leaf) { this.leaf = leaf; }
    public void setTotalKeys(int totalKeysInPage) { this.totalKeysInPage = totalKeysInPage; }
    public void setKeys(Key[] keys) { this.keys = keys; }

    // Getters
    public int getTotalKeys() { return totalKeysInPage; }
    public boolean getLeaf() { return leaf; }
    public long getPosition() { return positionIndexedFile; }
    public long getFather() { return fatherPage; }
    public Key[] getKeys() { return keys; }
    public long getFirstPointer() { return firstPagePointer; }
}
