import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TreeB {
    private File indexedFileName;
    private RandomAccessFile indexedFile;
    private long root;
    private int order;

    TreeB(int x) throws IOException {
        order = x;
        indexedFileName = new File("indexedFile");
        indexedFile = new RandomAccessFile(indexedFileName, "rw");
        indexedFile.seek(0);

        Page page = new Page(order);
        page.writePage(indexedFile);

        root = page.getPosition();
        page = null;
    }

    public void insertKey(Key key) throws IOException {
        insertKey(key, root);
    }

    private void insertKey(Key key, long posPage) throws IOException {
        indexedFile.seek(posPage);
        Page page = new Page(order);
        page.readPage(indexedFile);

        if(page.getLeaf()) {
            if(page.getTotalKeys() < order-1) {
                page.addKey(key);
                indexedFile.seek(posPage);
                page.writePage(indexedFile);
            } else {
                fragment(page, key);
            }
        } else {
            posPage = searchPage(page, key);
            insertKey(key, posPage);
        }
    }

    private void fragment(Page page, Key key) throws IOException {
        Page father = new Page(order), right = new Page(order);
        int n = (order-1)/2;
        Key[] keysPage = page.getKeys();
        Key keyPromovid = keysPage[n];
        boolean recursionControl = true;

        for(int i = n+1; i < order-1; i++) { right.addKey(keysPage[i]); } 

        page.setTotalKeys(n);

        if(key.getId() < keyPromovid.getId()) { page.addKey(key); } 
        else { right.addKey(key); } 

        if(page.getFather() != -1) { 
            indexedFile.seek(page.getFather());
            father.readPage(indexedFile);
        }

        indexedFile.seek(indexedFile.length());
        right.setFather(father.getPosition());
        right.setFirstPointer(keyPromovid.getPointer());
        right.setLeaf((right.getFirstPointer() != -1 ? false:true));
        right.writePage(indexedFile);

        keyPromovid.setPointer(right.getPosition());

        if(father.getTotalKeys() >= order -1) {
            fragment(father, keyPromovid);
            indexedFile.seek(searchPageInKey(keyPromovid, root));
            father.readPage(indexedFile);
            recursionControl = false;
        }
        
        indexedFile.seek(right.getPosition());
        right.setFather(father.getPosition());
        right.writePage(indexedFile);

        if(page.getPosition() != 0) { indexedFile.seek(page.getPosition()); }
        else { indexedFile.seek(indexedFile.length()); }
        page.setFather(father.getPosition());
        page.setLeaf((page.getFirstPointer() != -1 ? false:true));
        page.writePage(indexedFile);

        indexedFile.seek(father.getPosition());
        if(recursionControl) { father.addKey(keyPromovid); }
        father.setLeaf(false);
        if(page.getFather() == root && father.getFirstPointer() == -1) { father.setFirstPointer(page.getPosition());}
        father.writePage(indexedFile);
    }

    private long searchPage(Page page, Key key) {
        Key keys[] = page.getKeys();
        int n = page.getTotalKeys();
        long pointer = -1;

        if(n == 1) {
            if(keys[0].getId() > key.getId()) {
                pointer = page.getFirstPointer();
            } else {
                pointer = keys[0].getPointer();
            }
        } else {
            for(int i = 1; i < n; i++) {
                if(key.getId() > keys[i-1].getId() && key.getId() < keys[i].getId()) {
                    pointer = keys[i-1].getPointer();
                    i = n;
                } else if (i == 1 && key.getId() < keys[i-1].getId()) {
                    pointer = page.getFirstPointer();
                    i = n;
                } else if (i == n-1 && key.getId() > keys[i].getId()) {
                    pointer = keys[i].getPointer();
                    i = n;
                }
            }
        }
        
        return pointer;
    }

    private long searchPageInKey(Key key, long pos) throws IOException {
        indexedFile.seek(pos);
        Page page = new Page(order);
        page.readPage(indexedFile);
        Key[] keys = page.getKeys();
        int i = 0, n = page.getTotalKeys()-1, middle = 0;

        while(i <= n) {
            middle = (i + n)/2;
            if(keys[middle].getId() == key.getId()) {
                return page.getPosition();
            }

            if(keys[middle].getId() < key.getId()) {
                i = middle + 1;
            } else {
                n = middle - 1;
            }
        }
        if(n == -1) { n = 0; }
        if(n == 0) {
            if(keys[0].getId() > key.getId()) {
                return searchPageInKey(key, page.getFirstPointer());
            } else {
                return searchPageInKey(key, keys[0].getPointer());
            }
        } else {
            if(keys[middle].getId() < key.getId()) {
                return searchPageInKey(key, keys[middle].getPointer());
            } else {
                return searchPageInKey(key, keys[middle - 1].getPointer());
            }
        }
    }

    public void show() throws IOException {
        indexedFile.seek(root);
        
        while(eof()) {
            System.out.println();
            System.out.println("==============");
            System.out.println("Posicao da Pagina: " + indexedFile.getFilePointer());
            System.out.println("Ponteiro Pai: " + indexedFile.readLong());
            System.out.println("Folha: " + indexedFile.readBoolean());
            int n = indexedFile.readInt();
            System.out.println("Total de Chaves: " + n);
            System.out.println("Primeiro Ponteiro da Pagina: " + indexedFile.readLong());
            for(int i = 0; i < 7; i++) {
               if(i < n) {
                  System.out.println("Id: " + indexedFile.readInt() + " Position: " + indexedFile.readLong() + " Pointer: " + indexedFile.readLong());
               } else {
                  indexedFile.readInt(); indexedFile.readLong(); indexedFile.readLong();
               }
            }
        }
    }

    public File getFile() {
       return indexedFileName;
    }

    private boolean eof() throws IOException {
        return indexedFile.getFilePointer() < indexedFile.length();
    }
}
