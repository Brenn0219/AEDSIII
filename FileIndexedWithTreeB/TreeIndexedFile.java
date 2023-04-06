import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TreeIndexedFile {
    private File indexedFileName;
    private RandomAccessFile indexedFile;
    private long root;
    private int order;

    TreeIndexedFile() throws IOException {
        order = 8;
        indexedFileName = new File("indexedFile");
        indexedFile = new RandomAccessFile(indexedFileName, "rw");
        indexedFile.seek(0);

        root = indexedFile.getFilePointer();
        Node pageRoot = new Node(root);
        pageRoot.write(indexedFile);
    }
    
    public void insertKey(int id, long position) throws IOException {
        indexedFile.seek(root);
        Node page = new Node(indexedFile);

        if(page.getLeaf()) {
            if(page.getTotalKeysInNode() < order-1) {
                long startingPosition = indexedFile.getFilePointer();
                
                indexedFile.skipBytes(page.getTotalKeysInNode() * 20);
                page.writeKey(indexedFile, id, position);
                indexedFile.seek(startingPosition);

                int n = page.getTotalKeysInNode()-1;
                for(int  i = 0; i < n; i++) {
                    if(id < indexedFile.readInt()) {
                        indexedFile.seek(startingPosition);
                        byte[] bytes = new byte[(n-i) * 20];
                        indexedFile.read(bytes);
                        indexedFile.seek(startingPosition);
                        page.overwriteKeys(indexedFile, id, position);
                        indexedFile.write(bytes);
                        i = n;
                    }
                }
                
            } else {
                // fragementar
            }
        } else {
            // procurar recursivamente a pagina
        }

        page = null;
    }

    public long search(int id) throws IOException {
        indexedFile.seek(0);

        while(eof()) {
            if(indexedFile.readInt() == id) {
                return indexedFile.readLong();
            } else {
                indexedFile.skipBytes(8);
            }
        }

        return -1;
    }

    private boolean eof() throws IOException {
        return indexedFile.getFilePointer() < indexedFile.length();
    }

    public void show() throws IOException {
        indexedFile.seek(root);
        
        System.out.println("Ponteiro Pai: " + indexedFile.readLong());
        System.out.println("Folha: " + indexedFile.readBoolean());
        System.out.println("Total de Chaves: " + indexedFile.readInt());
        indexedFile.readLong(); // firstNodePointer

        while(eof()) {
            System.out.println("Id: " + indexedFile.readInt() + " Position: " + indexedFile.readLong());
            indexedFile.readLong(); // keyPointer
        }
    }

    public File getFile() {
       return indexedFileName;
    }
}
