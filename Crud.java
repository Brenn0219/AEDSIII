import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Crud {
    private String name;
    private File file;
    private RandomAccessFile readFile;
    private int size;

    Crud(String fileName) throws Exception {
        name = fileName;
        file = new File(name);
        readFile = new RandomAccessFile(name, "rw");
        readFile.seek(0);
        size = 5;
    }

    public void create(Games game) throws Exception {
        create(readFile, game);
    }

    public Games select(int x) throws Exception {
        int registerSize, id;
        boolean fileValue;
        long position;
        Games game = null;

        readFile.seek(0);
        readFile.skipBytes(4);

        try {
            while(readFile.getFilePointer() < readFile.length()) {
                position = readFile.getFilePointer();
                registerSize = readFile.readInt();
                fileValue = readFile.readBoolean();
                id = readFile.readInt();
    
                if(fileValue) {
                    if(id == x) {
                        game = readBytesForGames(readFile, position);
                        break;
                    } else {
                        readFile.skipBytes(registerSize - 5);
                    }
                } else {
                    readFile.skipBytes(registerSize - 5);
                }
            }  
        } catch (IOException e) { // Caso o Registro nao existir no arquivo
            System.err.println("Registro nao Encontrado");
            return null;
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
            System.out.println();
        }
    }

    public void deleteFile() throws IOException { deleteFIle(file); }

    public void sort() throws Exception {
        File fileTempOne = new File("temp1"), fileTempTwo = new File("temp2"), fileTempThree = new File("temp3"), fileTempFour = new File("temp4");
        RandomAccessFile firstTempFile = new RandomAccessFile(fileTempOne, "rw"), secondTempFile = new RandomAccessFile(fileTempTwo, "rw"), thirdTempFile = new RandomAccessFile(fileTempThree, "rw"), fourthTempFile = new RandomAccessFile(fileTempFour, "rw");

        writeBlocks(readFile, firstTempFile, secondTempFile, size);

        firstTempFile.seek(0);
        secondTempFile.seek(0);
        firstTempFile.skipBytes(4);
        secondTempFile.skipBytes(4);
       
        while(true) {
            if(firstTempFile.getFilePointer() < firstTempFile.length() && secondTempFile.getFilePointer() < secondTempFile.length()) {
                if(firstTempFile.getFilePointer() < firstTempFile.length() || secondTempFile.getFilePointer() < secondTempFile.length()) {
                    intercalation(thirdTempFile, firstTempFile, secondTempFile, size);
                }

                if(firstTempFile.getFilePointer() < firstTempFile.length() || secondTempFile.getFilePointer() < secondTempFile.length()) {
                    intercalation(fourthTempFile, firstTempFile, secondTempFile, size);
                }
            } else {
                break;
            }
        }

        deleteFIle(fileTempOne);
        deleteFIle(fileTempTwo);

        thirdTempFile.seek(0);
        fourthTempFile.seek(0);
        thirdTempFile.skipBytes(4);
        fourthTempFile.skipBytes(4);

        while(true) {
            if(thirdTempFile.getFilePointer() < thirdTempFile.length() && fourthTempFile.getFilePointer() < fourthTempFile.length()) {
                if(thirdTempFile.getFilePointer() < thirdTempFile.length() || fourthTempFile.getFilePointer() < fourthTempFile.length()) {
                    intercalation(firstTempFile, thirdTempFile, fourthTempFile, size*2);
                }

                if(thirdTempFile.getFilePointer() < thirdTempFile.length() || fourthTempFile.getFilePointer() < fourthTempFile.length()) {
                    intercalation(fourthTempFile, secondTempFile, fourthTempFile, size*2);
                }
            } else {
                break;
            }
        }

        File fileTempSeis = new File("temp6"), fileTempSete = new File("temp7");
        RandomAccessFile sextoTempFile = new RandomAccessFile(fileTempSeis, "rw"), seteTempFile = new RandomAccessFile(fileTempSete, "rw");

        while(true) {
            if(thirdTempFile.getFilePointer() < thirdTempFile.length() && fourthTempFile.getFilePointer() < fourthTempFile.length()) {
                if(thirdTempFile.getFilePointer() < thirdTempFile.length() || fourthTempFile.getFilePointer() < fourthTempFile.length()) {
                    intercalation(sextoTempFile, thirdTempFile, fourthTempFile, size*2);
                }

                if(thirdTempFile.getFilePointer() < thirdTempFile.length() || fourthTempFile.getFilePointer() < fourthTempFile.length()) {
                    intercalation(seteTempFile, secondTempFile, fourthTempFile, size*2);
                }
            } else {
                break;
            }
        }

        readFile.seek(0);
        readFile.skipBytes(4);
        intercalation(readFile, sextoTempFile, seteTempFile);

        show(readFile);

        deleteFIle(fileTempOne);
        deleteFIle(fileTempTwo);
        deleteFIle(fileTempThree);
        deleteFIle(fileTempFour);
    }

    private void create(RandomAccessFile file, Games game) throws Exception {
        file.seek(0);
        file.writeInt(game.getApp_id());

        file.seek(file.length());
        byte[] bytes = game.toByteArray(); 
        file.writeInt(bytes.length); 
        file.write(bytes); 
    }

    private boolean deleteFIle(File file) { return file.delete(); }

    private Games readBytesForGames(RandomAccessFile file, long position) throws Exception {
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

    // Escrev Blocos de Registros intercalando em dois Arquivos
    private void writeBlocks(RandomAccessFile file, RandomAccessFile firstFile, RandomAccessFile secondFile, int sizeRegister) throws Exception {    
        file.seek(0);
        file.skipBytes(4);
        long position = file.getFilePointer();

        while(position < file.length()) {
            if(position < file.length()) {
                position = writeFile(firstFile, file, sizeRegister);
            }

            if(position < file.length()) {
                position = writeFile(secondFile, file, sizeRegister);
            }
        }
    }

    private long writeFile(RandomAccessFile file, RandomAccessFile fileToRead, int sizeRegister) throws Exception {
        file.seek(0);   
        Games[] games = new Games[sizeRegister];

        for(int i = 0; i < sizeRegister; i++) {
            games[i] = readBytesForGames(fileToRead, fileToRead.getFilePointer());
        }

        quickSort(games, 0, sizeRegister-1);

        for(int i = 0; i < sizeRegister; i++) {
            create(file, games[i]);
        }

        return fileToRead.getFilePointer();
    }

    // Ordenacao dos Blocos de Registros depois de transformados em Games
    private void quickSort(Games[] games, int left, int right) {
        int i = left, j = right;
        Games pivo = games[(left + right) / 2];

        while(i <= j) {
            while(games[i].getApp_id() < pivo.getApp_id()) i++;
            while(games[j].getApp_id() > pivo.getApp_id()) j--;

            if(i <= j) {
                Games tmp = games[i];
                games[i] = games[j];
                games[j] = tmp;

                i++;
                j--;
            }
        }

        if(left < j) quickSort(games, left, j);
        if(i < right) quickSort(games, i, right);
    }

    private void intercalation(RandomAccessFile file, RandomAccessFile firstFile, RandomAccessFile secondFile, int sizeRegister) throws Exception {
        int counterFirstFile = 0, counterSecondFile = 0;
        long positionFirstFile = -1, positionSecondFile = -1;
        Games gamesFirstFile = null, gamesSecondFile = null;

        // int contador = 0;

        while(counterFirstFile < sizeRegister || counterSecondFile < sizeRegister) {
            // System.out.println(contador+1 + " LAÇO");
            if(firstFile.getFilePointer() < firstFile.length() && secondFile.getFilePointer() < secondFile.length()) {
                if(firstFile.getFilePointer() < firstFile.length()) {
                    if(counterFirstFile < sizeRegister) {
                        positionFirstFile = firstFile.getFilePointer();
                        gamesFirstFile = readBytesForGames(firstFile, positionFirstFile);
                        // System.out.println("PRIMEIRO ARQUIVO");
                        // System.out.println("ID: " + gamesFirstFile.getApp_id());
                    }
                }

                if(secondFile.getFilePointer() < secondFile.length()) {
                    if(counterSecondFile < sizeRegister) {
                        positionSecondFile = secondFile.getFilePointer();
                        gamesSecondFile = readBytesForGames(secondFile, positionSecondFile);
                        // System.out.println("SEGUNDO ARQUIVO");
                        // System.out.println("ID: " + gamesSecondFile.getApp_id());
                    }
                }
                // System.out.println();
                if(gamesFirstFile != null && gamesSecondFile != null) {
                    if(gamesFirstFile.getApp_id() < gamesSecondFile.getApp_id()) {
                        create(file, gamesFirstFile);
                        counterFirstFile++;
                        secondFile.seek(positionSecondFile);
                        // System.out.println("GAME1 - ID: " + gamesFirstFile.getApp_id());
                        // System.out.println("Contador Arquivo 1: " + counterFirstFile);
                    } else {
                        create(file, gamesSecondFile);
                        counterSecondFile++;
                        firstFile.seek(positionFirstFile);
                        // System.out.println("GAME2 - ID: " + gamesSecondFile.getApp_id());
                        // System.out.println("Contador Arquivo 2: " + counterSecondFile);
                    }
                    // System.out.println();
                } else if (gamesFirstFile == null) {
                    create(file, gamesSecondFile);
                    counterSecondFile++;
                    firstFile.seek(positionFirstFile);
                } else {
                    create(file, gamesFirstFile);
                    counterFirstFile++;
                    secondFile.seek(positionSecondFile);
                }
            } else {
                // System.out.println("BREAK");
                break;
            }

            gamesFirstFile = null;
            gamesSecondFile = null;
            // contador++;
        }
    }

    private void intercalation(RandomAccessFile file, RandomAccessFile firstFile, RandomAccessFile secondFile) throws Exception {
        long positionFirstFile = -1, positionSecondFile = -1;
        Games gamesFirstFile = null, gamesSecondFile = null;

        // int contador = 0;

        while(firstFile.getFilePointer() < firstFile.length() || secondFile.getFilePointer() < secondFile.length()) {
            // System.out.println(contador+1 + " LAÇO");
            if(firstFile.getFilePointer() < firstFile.length() && secondFile.getFilePointer() < secondFile.length()) {
                if(firstFile.getFilePointer() < firstFile.length()) {
                    positionFirstFile = firstFile.getFilePointer();
                    gamesFirstFile = readBytesForGames(firstFile, positionFirstFile);
                }

                if(secondFile.getFilePointer() < secondFile.length()) {
                    positionSecondFile = secondFile.getFilePointer();
                    gamesSecondFile = readBytesForGames(secondFile, positionSecondFile);
                }
                // System.out.println();
                if(gamesFirstFile != null && gamesSecondFile != null) {
                    if(gamesFirstFile.getApp_id() < gamesSecondFile.getApp_id()) {
                        writeGameToByte(file, file.getFilePointer(), gamesFirstFile);
                        secondFile.seek(positionSecondFile);
                        // System.out.println("GAME1 - ID: " + gamesFirstFile.getApp_id());
                        // System.out.println("Contador Arquivo 1: " + counterFirstFile);
                    } else {
                        writeGameToByte(file, file.getFilePointer(), gamesSecondFile);
                        firstFile.seek(positionFirstFile);
                        // System.out.println("GAME2 - ID: " + gamesSecondFile.getApp_id());
                        // System.out.println("Contador Arquivo 2: " + counterSecondFile);
                    }
                    // System.out.println();
                } else if (gamesFirstFile == null) {
                    writeGameToByte(file, file.getFilePointer(), gamesFirstFile);
                    firstFile.seek(positionFirstFile);
                } else {
                    writeGameToByte(file, file.getFilePointer(), gamesSecondFile);
                    secondFile.seek(positionSecondFile);
                }
            } else {
                // System.out.println("BREAK");
                break;
            }

            gamesFirstFile = null;
            gamesSecondFile = null;
            // contador++;
        }
    }
}
