import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DataBase {
    private String name;
    private File file;
    private RandomAccessFile readFile;
    private Hash hash;
    
    DataBase(String fileName) throws Exception {
        name = fileName;
        file = new File(name);
        readFile = new RandomAccessFile(name, "rw");
        hash = new Hash();
        readFile.seek(0);
    }
    
    public void create(Games game) throws Exception {
        create(readFile, game);
    }

    private void create(RandomAccessFile file, Games game) throws Exception {
        file.seek(0);
        file.writeInt(game.getApp_id());

        file.seek(file.length());
        byte[] bytes = game.toByteArray(); 
        long position = file.getFilePointer();
        file.writeInt(bytes.length); 
        file.write(bytes); 
        hash.insert(game.getApp_id(), position);
    }

    public Games read(int x) throws Exception {        
        long position = hash.read(x);
        Games game = null;

        if(position != -1) {
            game = readBytesForGames(readFile, position);
        } else {
            System.out.println("Registro nao Encontrado");
        }

        return game;
    }

    public boolean delete(int x) throws IOException {
        long position;
        int registerSize, id;
        boolean fileValue, find = false;
        readFile.seek(0);
        readFile.skipBytes(4);

        try {
            while(readFile.getFilePointer() < readFile.length()) {
                registerSize = readFile.readInt();
                position = readFile.getFilePointer();
                fileValue = readFile.readBoolean();
                id = readFile.readInt();

                if(fileValue) {
                    if(id == x) {
                        System.out.println("DELETE");
                        System.out.println("Tamanho: " + registerSize + " Id: " + id);
                        readFile.seek(position);
                        readFile.writeBoolean(false);
                        find = true;
                        break;
                    } else {
                        readFile.skipBytes(registerSize - 5);
                    }
                } else {
                    readFile.skipBytes(registerSize - 5);
                }
            }
        } catch (IOException e) {
            System.err.println("Registro nao encontrado");
        }

        return find;
    }

    public void update(Games game) throws Exception {
        long position;
        int registerSize, id;
        boolean registerValue;
        readFile.seek(0);
        readFile.skipBytes(4);

        try {
            while(readFile.getFilePointer() < readFile.length()) {
                registerSize = readFile.readInt();
                position = readFile.getFilePointer();
                registerValue = readFile.readBoolean();
                id = readFile.readInt();

                if(registerValue) {
                    if(id == game.getApp_id()) {
                        byte[] bytes = game.toByteArray();

                        System.out.println("UPDATE");
                        System.out.println("Tamanho: " + registerSize + " ID: " + id);

                        if(registerSize > bytes.length) {
                            System.out.println("MENOR");
                            writeGameToByte(readFile, position, game);
                        } else {
                            System.out.println("MAIOR");
                            readFile.seek(position);
                            readFile.writeBoolean(false);

                            create(game);
                        }

                        break;
                    } else {
                        readFile.skipBytes(registerSize - 5);
                    }
                } else {
                    readFile.skipBytes(registerSize - 5);
                }
            }
        } catch (IOException e) {
            System.err.println("Registro nao Encontrado");
        }
    }

    public void show() throws Exception {
        int n;
        readFile.seek(0);
       
        System.out.println("Id ULtimo Registro: " + readFile.readInt());
        while(readFile.getFilePointer() < readFile.length()) {
            System.out.print("Tamanho do Resgistro: " + readFile.readInt() + " Lapide: " + readFile.readBoolean() + " ID: " + readFile.readInt() + " Name(" + readFile.readInt() + "): " + readFile.readUTF() + " Date(" + readFile.readInt() + "): " + readFile.readUTF() + " Owners(" + readFile.readInt() + "): " + readFile.readUTF() + " Age: " + readFile.readInt() + " Price: " + readFile.readDouble() + " Dlcs: " + readFile.readInt() + " ");

            n = readFile.readInt();
            System.out.print("Languages(" + n + ") ");
            
            for(int i = 0; i < n; i++) {
                System.out.print("(" + readFile.readInt() + ")" + readFile.readUTF() + " ");
            }
            
            System.out.print("Web(" + readFile.readInt() + "): " + readFile.readUTF() + " Windows: " + readFile.readBoolean() + " Linux: " + readFile.readBoolean() + " Mac: " + readFile.readBoolean() + " Upvotes: " + readFile.readDouble() + " Avg: " + readFile.readInt() + " Developes(" + readFile.readInt() + "): " + readFile.readUTF() + " ");
    
            n = readFile.readInt();
            System.out.print("Genres(" + n + ") ");
            
            for(int i = 0; i < n; i++) {
                System.out.print("(" + readFile.readInt() + ")" + readFile.readUTF() + " ");
            }
            System.out.println();
            System.out.println();
        }
    }

    public void show(RandomAccessFile file) throws Exception {
        int n;
        file.seek(0);
       
        System.out.println("Id ULtimo Registro: " + file.readInt());
        while(file.getFilePointer() < file.length()) {
            System.out.print("Tamanho do Resgistro: " + file.readInt() + " Lapide: " + file.readBoolean() + " ID: " + file.readInt() + " Name(" + file.readInt() + "): " + file.readUTF() + " Date(" + file.readInt() + "): " + file.readUTF() + " Owners(" + file.readInt() + "): " + file.readUTF() + " Age: " + file.readInt() + " Price: " + file.readDouble() + " Dlcs: " + file.readInt() + " ");

            n = file.readInt();
            System.out.print("Languages(" + n + ") ");
            
            for(int i = 0; i < n; i++) {
                System.out.print("(" + file.readInt() + ")" + file.readUTF() + " ");
            }
            
            System.out.print("Web(" + file.readInt() + "): " + file.readUTF() + " Windows: " + file.readBoolean() + " Linux: " + file.readBoolean() + " Mac: " + file.readBoolean() + " Upvotes: " + file.readDouble() + " Avg: " + file.readInt() + " Developes(" + file.readInt() + "): " + file.readUTF() + " ");
    
            n = file.readInt();
            System.out.print("Genres(" + n + ") ");
            
            for(int i = 0; i < n; i++) {
                System.out.print("(" + file.readInt() + ")" + file.readUTF() + " ");
            }
            System.out.println();
        }
    }

    public void showHash() throws IOException { hash.show(); }

    public void deleteFile() throws IOException { deleteFIle(file); }

    public RandomAccessFile getFile() { return readFile; }

    private boolean deleteFIle(File file) { return file.delete(); }

    public static Games readBytesForGames(RandomAccessFile file, long position) throws Exception {
        file.seek(position);
        Games game = new Games();
        file.readInt();

        game.setFileValue(file.readBoolean());
        game.setApp_id(file.readInt());

        file.readInt();
        game.setName(file.readUTF());

        file.readInt();
        game.setRelease_date(Games.convertToDate(file.readUTF()));

        file.readInt();
        game.setOwners(file.readUTF());

        game.setAge(file.readInt());
        game.setPrice(file.readDouble());
        game.setDlcs(file.readInt());

        String[] languages = new String[file.readInt()];
        for(int i = 0; i < languages.length; i++) {
            file.readInt();
            languages[i] = file.readUTF();
        }
        game.setLanguages(languages);

        file.readInt();
        game.setWebsite(file.readUTF());

        game.setWindows(file.readBoolean());
        game.setLinux(file.readBoolean());
        game.setMac(file.readBoolean());
        game.setUpvotes(file.readDouble());
        game.setAvg_pt(file.readInt());

        file.readInt();
        game.setDevelopes(file.readUTF());

        String[] genres = new String[file.readInt()];
        for(int i = 0; i < genres.length; i++) {
            file.readInt();
            genres[i] = file.readUTF();
        }
        game.setGenres(genres);

        return game;
    }

    private long writeGameToByte(RandomAccessFile file, long position, Games game) throws Exception {
        file.seek(position);
        file.writeBoolean(game.getFileValue());
        file.writeInt(game.getApp_id());

        file.writeInt(game.getName().length());
        file.writeUTF(game.getName());

        file.writeInt(Games.formatDate(game.getRelease_date()).length());
        file.writeUTF(Games.formatDate(game.getRelease_date()));

        file.writeInt(game.getOwners().length());
        file.writeUTF(game.getOwners());

        file.writeInt(game.getAge());
        file.writeDouble(game.getPrice());
        file.writeInt(game.getDlcs());

        String[] languages = game.getLanguages();
        file.writeInt(languages.length);
        for(int i = 0; i < languages.length; i++) {
            file.writeInt(languages[i].length());
            file.writeUTF(languages[i]);
        }

        file.writeInt(Games.formatWeb(game.getWebsite()).length());
        file.writeUTF(Games.formatWeb(game.getWebsite()));

        file.writeBoolean(game.getWindows());
        file.writeBoolean(game.getLinux());
        file.writeBoolean(game.getMac());
        file.writeDouble(game.getUpvotes());
        file.writeInt(game.getAvg_pt());

        file.writeInt(game.getDevelopes().length());
        file.writeUTF(game.getDevelopes());

        String[] genres = game.getGenres();
        file.writeInt(genres.length);
        for(int i = 0; i < genres.length; i++) {
            file.writeInt(genres[i].length());
            file.writeUTF(languages[i]);
        }

        return file.getFilePointer();
    }
}
