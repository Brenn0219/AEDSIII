import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IndexedFile implements FileStructure {
    private RandomAccessFile file;

    IndexedFile(String nameFile) {
        try {
            file = new RandomAccessFile(nameFile, "rw");
        } catch (FileNotFoundException e) {
            System.err.println("Error instantiating: class IndexedFile" + e.getMessage());
        }   
    }

    // Metodo de crear/escrever um jogo no arquivo
    @Override
    public void create(int id, long gamePosition) {
        try {
            file.seek(file.length());
            file.writeBoolean(true);
            file.writeInt(id);
            file.writeLong(gamePosition);
        } catch (IOException e) {
            System.err.println("Erro when creating a new record (game) in the file: Class IndexedFile - " + e.getMessage());
        }
    }

    // Metodo para recuperar um jogo no arquivo
    @Override
    public long read(int id) {
        try {
            long posiition = -1;
            file.seek(0);
         
            while(file.getFilePointer() < file.length()) {
                if(file.readBoolean()) {
                    if (id == file.readInt()) {
                        posiition = file.readLong();
                        break;
                    } else {
                        file.skipBytes(Long.BYTES);
                    }
                } else {
                    file.skipBytes(Integer.BYTES + Long.BYTES );
                }
            }

            return posiition;
        } catch (Exception e) {
            System.err.println("Error when retrieving a record (game) from the file (Indexed File)" + e.getMessage());
            return -1;
        }
    }

    // Metodo para deletar um jogo do arquivo
    @Override
    public boolean delete(int id) {
        try {
            boolean find = false;
            file.seek(0);

            while(file.getFilePointer() < file.length()) {
                long position = file.getFilePointer();

                if(file.readBoolean()) {
                    if(file.readInt() == id) {
                        file.seek(position);
                        file.writeBoolean(false);
                        find = true;
                        break;
                    } else {
                        file.skipBytes(Long.BYTES);
                    }
                } else {
                    file.skipBytes(Integer.BYTES + Long.BYTES);
                }
            }

            return find;
        } catch (IOException e) {
            System.err.println("Error when deleting a record (game) from the file: class IndexedFile - " + e.getMessage());
            return false;
        }
    }
    
    // Metodo para atualizar um jogo do arquivo
    @Override
    public boolean update(int id, long gamePosition) {
        try {
            boolean find = false;
            file.seek(0);

            while(file.getFilePointer() < file.length()) {
                if(file.readBoolean()) {
                    if(file.readInt() == id) {
                        file.writeLong(gamePosition);
                        find = true;
                        break;
                    } else {
                        file.skipBytes(Long.BYTES);
                    }
                } else {
                    file.skipBytes(Integer.BYTES + Long.BYTES);
                }
            }

            return find;
        } catch (IOException e) {
            System.err.println("Error when deleting a record (game) from the file: class IndexedFile - " + e.getMessage());
            return false;
        }
    }
}
