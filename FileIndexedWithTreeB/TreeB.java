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

        root = page.getPositionIndexedFile();
        page = null;
    }

    public void insertKey(Key key) throws IOException {
        insertKey(key, root);
    }

    private void insertKey(Key key, long fatherPage) throws IOException {
        indexedFile.seek(fatherPage);
        Page page = new Page(order);
        page.readPage(indexedFile);

        if(page.getLeaf()) {
            if(page.getTotalKeysInPage() < order-1) {
                page.addKey(key);
                indexedFile.seek(fatherPage);
                page.writePage(indexedFile);
            } else {
                fragment(page, key, fatherPage);
            }
        } else {
            long teste = searchPage(page, key);
            insertKey(key, teste);
        }
    }
   
    private void fragment(Page page, Key key, long pos) throws IOException {
        Page fatherPage = new Page(order), rightSisterPage = new Page(order);
        Key keyProvided;
        int n = (order-1) / 2; // calculo para a posicao da chave promovida

        // Se a raiz que for fragmentada pela Primeira 
        if(page.getFatherPage() == -1 && page.getTotalKeysInPage() == order-1) {
            keyProvided = page.getKeys()[n]; // chave promovida

            // inserindo as chaves que sobraram na pagina irma (right page)
            for(int i = n+1; i < order-1; i++) { rightSisterPage.addKey(page.getKeys()[i]); }
            
            // atualizando a o total de chaves na pagina fragmentada (n)
            page.setTotalKeysInPage(n);

            // Verificando em qual pagina a nova chave ira ser inserida
            if(key.getId() < keyProvided.getId()) {
                page.addKey(key);
            } else {
                rightSisterPage.addKey(key);
            }

            // Atualizando as Informacoes da Pagina Fragmentada e escrevendo no arquivo
            page.setFatherPage(fatherPage.getPositionIndexedFile());
            page.writePage(indexedFile);

            // Inserido a Posicao da Pagina Pai e Escrevendo a Pagina irma no Arquivo   
            rightSisterPage.setFatherPage(fatherPage.getPositionIndexedFile());
            rightSisterPage.writePage(indexedFile);

            // Atualizando todas as informacoes da Pagina Pai
            fatherPage.setLeaf(false);
            fatherPage.addKey(keyProvided);
            fatherPage.setFirstPagePointer(page.getPositionIndexedFile());

            // ==== OTIMIZAR FAZENDO BUSCA BINARIA ====
            // Procurando a Chave Promovida e atualizando o ponteiro para proxima chave
            Key[] keys = fatherPage.getKeys();
            for(int i = 0; i < fatherPage.getTotalKeysInPage(); i++) {
                if(keys[i].getId() == keyProvided.getId()) {
                    keys[i].setPointer(rightSisterPage.getPositionIndexedFile());
                    i = fatherPage.getTotalKeysInPage();
                }
            }
            fatherPage.setKeys(keys);
            keys = null;

            // Escrevendo Atualizacoes da Pagina Pai no Arquivo
            indexedFile.seek(pos);
            fatherPage.writePage(indexedFile);

            // Atualiza o Ponteiro da Raiz da Arvore
            root = fatherPage.getPositionIndexedFile();
        } else {
            // Acessando os dados da Pagina Pai
            indexedFile.seek(page.getFatherPage());
            fatherPage.readPage(indexedFile);

            // Caso a Pagina Pai esteja cheia, Fragmentar Recursivamente
            if(fatherPage.getTotalKeysInPage() >= order-1) {
                // fragementar
            }

            keyProvided = page.getKeys()[n];
            
            for(int i = n+1; i < order-1; i++) {rightSisterPage.addKey(page.getKeys()[i]);}
            
            page.setTotalKeysInPage(n);
            if(key.getId() < keyProvided.getId()) {
                page.addKey(key);
            } else {
                rightSisterPage.addKey(key);
            }
            
            // Escrever a Pagina Irma no Final do Arquivo pois e uma Pagina Nova
            rightSisterPage.setFatherPage(fatherPage.getPositionIndexedFile());
            indexedFile.seek(indexedFile.length());
            rightSisterPage.writePage(indexedFile);
            
            // Atualiza e Escreve o ponteiro da Chave Promovida (aponta para pagina irma)
            keyProvided.setPointer(rightSisterPage.getPositionIndexedFile());
            fatherPage.addKey(keyProvided);
            
            // Escreve atualizacoes da Pagina Fragmentada
            indexedFile.seek(pos);
            page.writePage(indexedFile);

            // Escreve e Atualiza informacoes da Pagina Pai
            indexedFile.seek(fatherPage.getPositionIndexedFile());
            fatherPage.writePage(indexedFile);
        }
    }   

    private long searchPage(Page page, Key key) {
        Key keys[] = page.getKeys();
        int n = page.getTotalKeysInPage()-1;
        long pointer = 0;

        if(n == 0) {
            if(keys[n].getId() > key.getId()) {
                pointer = page.getFirstPagePointer();
            } else {
                pointer = keys[n].getPointer();
            }
        } else {
            for(int i = 0; i < n-1; i++) {
                if(keys[i].getId() < key.getId() && keys[i + 1].getId() > key.getId()) {
                    pointer = keys[i].getPointer();
                }
            }
        }
        
        return pointer;
    }

    public void show() throws IOException {
        indexedFile.seek(root);
        
        while(eof()) {
            System.out.println();
            System.out.println("==============");
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
