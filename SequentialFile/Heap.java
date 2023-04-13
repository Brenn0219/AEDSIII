import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class Heap {
    private RandomAccessFile file;
    private Cell[] heap;
    private int size;

    Heap(RandomAccessFile readFile) throws FileNotFoundException {
        file = readFile;
        size = 7;
        Cell[] heap = null;
    }

    public void sort() throws Exception {
        heap = new Cell[size];
        file.seek(0);
        file.skipBytes(4);
        
        for(int i = 0; i < size; i++) {
            heap[i] = new Cell(DataBase.readBytesForGames(file, file.getFilePointer()));
        }
        
        rebuild();
        
        for(int i = 0; i < size; i++) {
            heap[i].game.show();
        }
    }

    private void rebuild() {
        int n = size-1;
        
        while(n > 1) {
            swap(1, n--);
            reconstruct(n);
        }
    }

    private void swap(int i, int j) {
        Cell tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    private void reconstruct(int n) {
        int i = 1;

        while(i <= (n/2)) {
            int son = getBiggerSon(i, n);

            if(heap[i].game.getApp_id() < heap[son].game.getApp_id()) {
                swap(i, son);
                i = son;
            } else {
                i = n;
            }
        }
    }

    private int getBiggerSon(int i, int n) {
        int son = 0;

        if(2*i == n || heap[2*i].game.getApp_id() > heap[2*i+1].game.getApp_id()) {
            son = 2*i;
        } else {
            son = 2*i+1;
        }

        return son;
    }
}
