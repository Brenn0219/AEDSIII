import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SequentialFile {
    private RandomAccessFile file;

    SequentialFile(String nameFile) {
        try {
            file = new RandomAccessFile(nameFile, "rw");
        } catch (FileNotFoundException e) {
            System.err.println("Error instantiating SequentialFile class " + e.getMessage());
        }
    }

    // Metodo de crear/escrever um jogo no arquivo
    public long create(Game game) {
        try {
            file.seek(file.length());
            long position = file.getFilePointer();
            byte[] record = TurnIntoBytes.toByteArray(game, true);
            file.writeInt(record.length);
            file.write(record);
            return position;
        } catch (Exception e) {
            System.err.println("Erro when creating a new record (game) in the file: Class SequentialFile - " + e.getMessage());
            return -1;
        }
    }

    // Metodo para recuperar um jogo no arquivo
    public Game read(int x) {
        try {
            int recordSize, id;
            long position;
            boolean status;
            Game game = null;

            while(file.getFilePointer() < file.length()) {
                position = file.getFilePointer();
                recordSize = file.readInt();
                status = file.readBoolean();
                id = file.readInt();

                if(status) {
                    if(id == x) {
                        game = readBytesForGame(position);
                        break;
                    } else {
                        file.skipBytes(recordSize - (Integer.BYTES + 1));
                    }
                } else {
                    file.skipBytes(recordSize - (Integer.BYTES + 1));
                }
            }
            
            return game;
        } catch (Exception e) {
            System.err.println("Error when retrieving a record (game) from the file " + e.getMessage());
            return null;
        }
    }

    // Metodo para deletar um jogo do arquivo
    public boolean delete(int x) {
        try {
            int recordSize, id;
            long position;
            boolean status, find = false;
        
            while(file.getFilePointer() < file.length()) {
                recordSize = file.readInt();
                position = file.getFilePointer();
                status = file.readBoolean();
                id = file.readInt();

                if(status) {
                    if(id == x) {
                        file.seek(position);
                        file.writeBoolean(false);
                        find = true;
                        break;
                    } else {
                        file.skipBytes(recordSize - (Integer.BYTES + 1));
                    }
                } else {
                    file.skipBytes(recordSize - (Integer.BYTES + 1));
                }
            }

            return find;
        } catch (IOException e) {
            System.err.println("Error when deleting a record (game) from the file class SequentialFile" + e.getMessage());
            return false;
        }
    }
    
    // Metodo para atualizar um jogo do arquivo
    public long update(Game game) {
        try {
            int recordSize, id;
            long positionStatus, originalFileGamePosition, pos = -1;
            boolean status;

            while(file.getFilePointer() < file.length()) {
                originalFileGamePosition = file.getFilePointer();
                recordSize = file.readInt();
                positionStatus = file.getFilePointer();
                status = file.readBoolean();
                id = file.readInt();

                if(status) {
                    if(id == game.getAppId()) {
                        byte[] bytes = TurnIntoBytes.toByteArray(game, true);

                        if(recordSize > bytes.length) {
                            writeGameToByte(positionStatus, game);
                            pos = originalFileGamePosition;
                        } else {
                            file.seek(positionStatus);
                            file.writeBoolean(false);

                            pos = create(game);
                        }

                        break;
                    } else {
                        file.skipBytes(recordSize - (Integer.BYTES + 1));
                    }
                } else {
                    file.skipBytes(recordSize - (Integer.BYTES + 1));
                }
            }

            return pos;
        } catch (Exception e) {
            System.err.println("Error when deleting a record (game) from the file " + e.getMessage());
            return -1;
        }
    }

    // Metodo para ler os bytes do arquivo e transformar em game
    public Game readBytesForGame(long position) {
        try {
            Game game = new Game();
            file.seek(position);
            file.readInt(); // recordSize
            file.readBoolean(); // status
            game.setAppId(file.readInt());

            file.readInt();
            game.setName(file.readUTF());

            file.readInt();
            game.setReleaseDate(game.convertToDate(file.readUTF()));

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
            game.setAvgPt(file.readInt());

            file.readInt();
            game.setDevelopes(file.readUTF());

            String[] genres = new String[file.readInt()];
            for(int i = 0; i < genres.length; i++) {
                file.readInt();
                genres[i] = file.readUTF();
            }
            game.setGenres(genres);

            return game;
        } catch (Exception e) {
            System.err.println("Error reading bytes from file to convert to game " + e.getMessage());
            return null;
        }
    }

    // Metodo para escrever um jogo no arquivo em posicao especifica
    private long writeGameToByte(long position, Game game) {
        try {
            file.seek(position);
            file.writeBoolean(true);
            file.writeInt(game.getAppId());

            file.writeInt(game.getName().length());
            file.writeUTF(game.getName());

            file.writeInt(GamePrinting.formatDate(game).length());
            file.writeUTF(GamePrinting.formatDate(game));

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

            file.writeInt(game.getWebsite().length());
            file.writeUTF(game.getWebsite());

            file.writeBoolean(game.getWindows());
            file.writeBoolean(game.getLinux());
            file.writeBoolean(game.getMac());
            file.writeDouble(game.getUpvotes());
            file.writeInt(game.getAvgPt());

            file.writeInt(game.getDevelopes().length());
            file.writeUTF(game.getDevelopes());

            String[] genres = game.getGenres();
            file.writeInt(genres.length);
            for(int i = 0; i < genres.length; i++) {
                file.writeInt(genres[i].length());
                file.writeUTF(genres[i]);
            }

            return file.getFilePointer();
        } catch (Exception e) {
            System.err.println("Erro writing a record (game) tp the file " + e.getMessage());
            return -1;
        }
    }
}
