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
        size = 60;
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
        File temp1 = new File("temp1"), temp2 = new File("temp2"), tem3 = new File("temp3"), tem4 = new File("temp4");
        RandomAccessFile firstTempFile = new RandomAccessFile(temp1, "rw");
        RandomAccessFile secondTempFile = new RandomAccessFile(temp2, "rw");
        RandomAccessFile thirdTempFile = new RandomAccessFile(tem3, "rw");
        RandomAccessFile fourthTempFile = new RandomAccessFile(tem4, "rw");
        
        // ESTAPA 1 -> DISTRIBUIÇÃO
        readFile.seek(0);
        readFile.skipBytes(4);
        long position = readFile.getFilePointer();

        while(position < readFile.length()) {
            position = writeFile(firstTempFile, readFile, size);

            if(position < readFile.length()) {
                position = writeFile(secondTempFile, readFile, size);
            }
        }   

        // ETAPA 2 -> PRIMEIRA INTERCALACAO
        firstTempFile.seek(0);
        firstTempFile.skipBytes(4);
        secondTempFile.seek(0);
        secondTempFile.skipBytes(4);

        while(true) {
            if(firstTempFile.getFilePointer() >= firstTempFile.length() && secondTempFile.getFilePointer() >= secondTempFile.length()) { break; }
            intercalation(thirdTempFile, firstTempFile, secondTempFile, size);

            if(firstTempFile.getFilePointer() >= firstTempFile.length() && secondTempFile.getFilePointer() >= secondTempFile.length()) { break; }
            intercalation(fourthTempFile, firstTempFile, secondTempFile, size);
        }
        
        // ETAPA 3 -> SEGUNDA INTERCALACAO
        firstTempFile.setLength(0);
        secondTempFile.setLength(0);
        thirdTempFile.seek(0);
        thirdTempFile.skipBytes(4);
        fourthTempFile.seek(0);
        fourthTempFile.skipBytes(4);
        int n = 2;

        while(true) {
            if(thirdTempFile.getFilePointer() >= thirdTempFile.length() && fourthTempFile.getFilePointer() >= fourthTempFile.length()) { break; }
            intercalation(firstTempFile, thirdTempFile, fourthTempFile, size*n);

            if(thirdTempFile.getFilePointer() >= thirdTempFile.length() && fourthTempFile.getFilePointer() >= fourthTempFile.length()) { break; }
            intercalation(secondTempFile, thirdTempFile, fourthTempFile, size*n);
        }

        // ETAPA 4 -> TERCEIRA INTERCALACAO
        readFile.setLength(0);
        firstTempFile.seek(0);
        firstTempFile.skipBytes(4);
        secondTempFile.seek(0);
        secondTempFile.skipBytes(4);
        n *= 2;

        while(true) {
            if(firstTempFile.getFilePointer() >= firstTempFile.length() && secondTempFile.getFilePointer() >= secondTempFile.length()) { break; }
            intercalation(readFile, firstTempFile, secondTempFile, size*n); 
        }

        deleteFIle(temp1);
        deleteFIle(temp2);
        deleteFIle(tem3);
        deleteFIle(tem4);
    }

    public void flexibleOrdering() throws Exception {
        File temp1 = new File("temp1"), temp2 = new File("temp2"), tem3 = new File("temp3"), tem4 = new File("temp4");
        RandomAccessFile firstTempFile = new RandomAccessFile(temp1, "rw");
        RandomAccessFile secondTempFile = new RandomAccessFile(temp2, "rw");
        RandomAccessFile thirdTempFile = new RandomAccessFile(tem3, "rw");
        RandomAccessFile fourthTempFile = new RandomAccessFile(tem4, "rw");
        
        // ESTAPA 1 -> DISTRIBUIÇÃO
        readFile.seek(0);
        readFile.skipBytes(4);
        long position = readFile.getFilePointer();

        while(position < readFile.length()) {
            position = writeFile(firstTempFile, readFile, size);

            if(position < readFile.length()) {
                position = writeFile(secondTempFile, readFile, size);
            }
        }

        // ETAPA 2 -> PRIMEIRA INTERCALACAO
        firstTempFile.seek(0);
        firstTempFile.skipBytes(4);
        secondTempFile.seek(0);
        secondTempFile.skipBytes(4);       
        int limit1, limit2;

        while(true) {
            if(firstTempFile.getFilePointer() >= firstTempFile.length() && secondTempFile.getFilePointer() >= secondTempFile.length()) { break; }

            limit1 = readBlocks(firstTempFile, firstTempFile.getFilePointer());
            limit2 = readBlocks(secondTempFile, secondTempFile.getFilePointer());
            softCollation(thirdTempFile, firstTempFile, secondTempFile, limit1, limit2);

            if(firstTempFile.getFilePointer() >= firstTempFile.length() && secondTempFile.getFilePointer() >= secondTempFile.length()) { break; }

            limit1 = readBlocks(firstTempFile, firstTempFile.getFilePointer());
            limit2 = readBlocks(secondTempFile, secondTempFile.getFilePointer());
            softCollation(fourthTempFile, firstTempFile, secondTempFile, limit1, limit2);
        }


        // ETAPA 3 -> SEGUNDA INTERCALACAO
        // firstTempFile.setLength(0);
        // secondTempFile.setLength(0);
        // thirdTempFile.seek(0);
        // thirdTempFile.skipBytes(4);
        // fourthTempFile.seek(0);
        // fourthTempFile.skipBytes(4);

        // while(true) {
        //     if(thirdTempFile.getFilePointer() >= thirdTempFile.length() && fourthTempFile.getFilePointer() >= fourthTempFile.length()) { break; }

        //     limit1 = readBlocks(thirdTempFile, thirdTempFile.getFilePointer());
        //     limit2 = readBlocks(fourthTempFile, fourthTempFile.getFilePointer());
        //     softCollation(firstTempFile, thirdTempFile, fourthTempFile, limit1, limit2);
        // }

        deleteFIle(temp1);
        deleteFIle(temp2);
        deleteFIle(tem3);
        deleteFIle(tem4);
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

    private long writeFile(RandomAccessFile file, RandomAccessFile readToFile, int totalRegister) throws IOException, Exception {
        Games[] games = new Games[totalRegister];

        int i = 0;
        for(; i < totalRegister; i++) {
            if(readToFile.getFilePointer() < readToFile.length()) {
                games[i] = readBytesForGames(readToFile, readToFile.getFilePointer());
            } else {
                break;
            }
        }

        quickSort(games, 0, i-1);

        for(int j = 0; j < totalRegister; j++) {
            if(games[j] != null) {
                create(file, games[j]);
            } else {
                break;
            }
        }

        return readToFile.getFilePointer();
    }

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

    private void intercalation(RandomAccessFile fileWrite, RandomAccessFile firstReadFile, RandomAccessFile secondReadFile, int sizeRegitser) throws Exception {
        int counterFirstFile = 0, counterSecondFile = 0;
        long positionFirstFile = -1, positionSecondFile = -1;
        Games gameFirstFile = null, gamesSecondFile = null;

        //System.out.println("ARQ1 -> POSICAO INICIO: " + firstReadFile.getFilePointer() + " POSICAO FINAL: " + firstReadFile.length());

        // System.out.println("ARQ2 -> POSICAO INICIO: " + secondReadFile.getFilePointer() + " POSICAO FINAL: " + secondReadFile.length());
        while(true) {
            if(counterFirstFile >= sizeRegitser && counterSecondFile >= sizeRegitser) { break;  }
            else if (firstReadFile.getFilePointer() >= firstReadFile.length() && secondReadFile.getFilePointer() >= secondReadFile.length()) { break; }

            if(firstReadFile.getFilePointer() < firstReadFile.length()) {
                if(counterFirstFile < sizeRegitser) {
                positionFirstFile = firstReadFile.getFilePointer();
                    gameFirstFile = readBytesForGames(firstReadFile, positionFirstFile);
                    //System.out.println("ARQ1 -> POSICAO GAMES: " + firstReadFile.getFilePointer() + " POSICAO FINAL: " + firstReadFile.length());
                }
            }

            if(secondReadFile.getFilePointer() < secondReadFile.length()) {
                if(counterSecondFile < sizeRegitser) {
                positionSecondFile = secondReadFile.getFilePointer();
                    gamesSecondFile = readBytesForGames(secondReadFile, positionSecondFile);
                    // System.out.println("ARQ2 -> POSICAO GAMES: " + secondReadFile.getFilePointer() + " POSICAO FINAL: " + secondReadFile.length());
                }
            }

            if(gameFirstFile != null && gamesSecondFile != null) {
                if(gameFirstFile.getApp_id() < gamesSecondFile.getApp_id()) {
                    create(fileWrite, gameFirstFile);
                    counterFirstFile++;
                    if(counterSecondFile < sizeRegitser) {
                        if(positionSecondFile != -1) { secondReadFile.seek(positionSecondFile); }
                    }
                    //System.out.println("ARQ1 -> POSICAO ESCREVENDO 1: " + firstReadFile.getFilePointer() + " POSICAO FINAL: " + firstReadFile.length());
                } else {
                    create(fileWrite, gamesSecondFile);
                    counterSecondFile++;
                    if(counterFirstFile < sizeRegitser) {
                        if(positionFirstFile != -1) { firstReadFile.seek(positionFirstFile); }
                    }
                    // System.out.println("ARQ2 -> POSICAO ESCREVENDO 1: " + secondReadFile.getFilePointer() + " POSICAO FINAL: " + secondReadFile.length());
                }
            } else {
                if(gamesSecondFile == null && gameFirstFile == null) { break; }

                if (gameFirstFile == null) {
                    create(fileWrite, gamesSecondFile);
                    counterSecondFile++;
                    if(counterFirstFile < sizeRegitser) {
                        if(positionFirstFile != -1) { firstReadFile.seek(positionFirstFile); }
                    }
                    // System.out.println("ARQ2 -> POSICAO ESCREVENDO 2: " + secondReadFile.getFilePointer() + " POSICAO FINAL: " + secondReadFile.length());
                } else {
                    create(fileWrite, gameFirstFile);
                    counterFirstFile++;
                    if(counterSecondFile < sizeRegitser) {
                        if(positionSecondFile != -1) { secondReadFile.seek(positionSecondFile); }
                    }
                    // System.out.println("ARQ1 -> POSICAO ESCREVENDO 2: " + firstReadFile.getFilePointer() + " POSICAO FINAL: " + firstReadFile.length());
                }
            }

            gameFirstFile = null;
            gamesSecondFile = null;
        }
    }

    private int readBlocks(RandomAccessFile file, long position) throws Exception {
        boolean control = (file.getFilePointer() < file.length());
        Games lastGame, firstGame;
        int counter = 0;
        
        while(control) {
            for(counter = 0; counter < size-1; counter++) {
                if(file.getFilePointer() < file.length()) {
                    file.skipBytes(file.readInt());
                } else {
                    break;
                }
            }

            if(file.getFilePointer() < file.length()) {
                lastGame = readBytesForGames(file, file.getFilePointer());
                counter++;

                if(file.getFilePointer() < file.length()) {
                    firstGame = readBytesForGames(file, file.getFilePointer());
                    counter++; 

                    if(lastGame.getApp_id() > firstGame.getApp_id()) { 
                        control = false;
                        counter--;
                    }
                } else { control = false; }
            } else { control = false; }
        }

        file.seek(position);

        return counter;
    }

    private void softCollation(RandomAccessFile fileWrite, RandomAccessFile firstReadFile, RandomAccessFile secondReadFile, int firstLimit, int lastLimit) throws Exception {
        int counterFirstFile = 0, counterSecondFile = 0;
        long positionFirstFile = -1, positionSecondFile = -1;
        Games gameFirstFile = null, gamesSecondFile = null;

        while(true) {
            if(counterFirstFile >= firstLimit && counterSecondFile >= lastLimit) { break;  }
            else if (firstReadFile.getFilePointer() >= firstReadFile.length() && secondReadFile.getFilePointer() >= secondReadFile.length()) { break; }

            if(firstReadFile.getFilePointer() < firstReadFile.length()) {
                if(counterFirstFile < lastLimit) {
                positionFirstFile = firstReadFile.getFilePointer();
                    gameFirstFile = readBytesForGames(firstReadFile, positionFirstFile);
                }
            }

            if(secondReadFile.getFilePointer() < secondReadFile.length()) {
                if(counterSecondFile < lastLimit) {
                positionSecondFile = secondReadFile.getFilePointer();
                    gamesSecondFile = readBytesForGames(secondReadFile, positionSecondFile);
                }
            }

            if(gameFirstFile != null && gamesSecondFile != null) {
                if(gameFirstFile.getApp_id() < gamesSecondFile.getApp_id()) {
                    create(fileWrite, gameFirstFile);
                    counterFirstFile++;
                    if(counterSecondFile < lastLimit) {
                        if(positionSecondFile != -1) { secondReadFile.seek(positionSecondFile); }
                    }
                } else {
                    create(fileWrite, gamesSecondFile);
                    counterSecondFile++;
                    if(counterFirstFile < firstLimit) {
                        if(positionFirstFile != -1) { firstReadFile.seek(positionFirstFile); }
                    }
                }
            } else {
                if(gamesSecondFile == null && gameFirstFile == null) { break; }

                if (gameFirstFile == null) {
                    create(fileWrite, gamesSecondFile);
                    counterSecondFile++;
                    if(counterFirstFile < firstLimit) {
                        if(positionFirstFile != -1) { firstReadFile.seek(positionFirstFile); }
                    }
                } else {
                    create(fileWrite, gameFirstFile);
                    counterFirstFile++;
                    if(counterSecondFile < lastLimit) {
                        if(positionSecondFile != -1) { secondReadFile.seek(positionSecondFile); }
                    }
                }
            }

            gameFirstFile = null;
            gamesSecondFile = null;
        }
    }
}
