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
            // System.out.println("==== SEACRH ====");
            // long teste = searchPage(page, key);
            // System.out.println("Position da Pagina: " + fatherPage + " Position da Pagina Selecionada: " + teste + " Chaves: " + page.getTotalKeysInPage());
            // insertKey(key, teste);
            
            fatherPage = searchPage(page, key);
            insertKey(key, fatherPage);
        }
    }
   
    private void fragment(Page page, Key key, long pos) throws IOException {
        Page fatherPage = new Page(order), rightSisterPage = new Page(order);
        Key keyProvided;
        int n = (order-1) / 2, oldQuantity = page.getTotalKeysInPage(); // calculo para a posicao da chave promovida

        // chave promovida
        keyProvided = page.getKeys()[n];

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
        
        // Fragmentacao na Raiz
        if(page.getFatherPage() == -1 && oldQuantity == order-1) {
            // Atualizando o Ponteiro do Pai das Paginas Filhas (esq, dir) e Escrevendo no Arquivo
            page.setFatherPage(pos);
            indexedFile.seek(indexedFile.length());
            page.writePage(indexedFile);
  
            rightSisterPage.setFatherPage(pos);
            rightSisterPage.setFirstPagePointer(keyProvided.getPointer());
            if(rightSisterPage.getFirstPagePointer() != -1) { rightSisterPage.setLeaf(false); }
            rightSisterPage.writePage(indexedFile);

            // Atualizando o Ponteiro para apontar para Pagina da Direita (irma)
            keyProvided.setPointer(rightSisterPage.getPositionIndexedFile());

            // Atualizando todas as informacoes da Pagina Pai e Escrevendo no Arquivo
            fatherPage.setLeaf(false);
            fatherPage.addKey(keyProvided);
            fatherPage.setFirstPagePointer(page.getPositionIndexedFile());

            indexedFile.seek(pos);
            fatherPage.writePage(indexedFile);
        } else {
            boolean recursionCheck = true;

            // Acessando os dados da Pagina Pai
            indexedFile.seek(page.getFatherPage());
            fatherPage.readPage(indexedFile);

            // Caso a Pagina Pai esteja cheia, Fragmentar Recursivamente
            if(fatherPage.getTotalKeysInPage() >= order-1) {
                long grandfathersPosition = fatherPage.getFatherPage();
                if(grandfathersPosition == -1) { grandfathersPosition = 0; }

                fragment(fatherPage, keyProvided, grandfathersPosition);
                recursionCheck = false;
            }
            
            // Escrever a Pagina Irma no Final do Arquivo pois e uma Pagina Nova
            rightSisterPage.setFatherPage(fatherPage.getPositionIndexedFile());
            indexedFile.seek(indexedFile.length());
            rightSisterPage.writePage(indexedFile);
            
            // Atualiza e Escreve o ponteiro da Chave Promovida (aponta para pagina irma)
            keyProvided.setPointer(rightSisterPage.getPositionIndexedFile());
            if(recursionCheck) { fatherPage.addKey(keyProvided); }
            
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
        int n = page.getTotalKeysInPage();
        long pointer = -1;

        if(n == 1) {
            if(keys[0].getId() > key.getId()) {
                pointer = page.getFirstPagePointer();
            } else {
                pointer = keys[0].getPointer();
            }
        } else {
            for(int i = 1; i < n; i++) {
                if(key.getId() > keys[i-1].getId() && key.getId() < keys[i].getId()) {
                    pointer = keys[i-1].getPointer();
                    i = n;
                } else if (i == 1 && key.getId() < keys[i-1].getId()) {
                    pointer = page.getFirstPagePointer();
                    i = n;
                } else if (i == n-1 && key.getId() > keys[i].getId()) {
                    pointer = keys[i].getPointer();
                    i = n;
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
