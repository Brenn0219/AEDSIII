import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileSorting {
    
    // Ordenacao de Arquivo
    public static void sort(RandomAccessFile readFile) throws Exception {
        sort(readFile, 2);
    }

    public static void sort(RandomAccessFile readFile, int totalTempFiles) throws Exception {
        int step, totalRecords = 4173, recordsForMemory = 150;
        File[] nameFiles = new File[totalTempFiles*2];
        RandomAccessFile[] file = new RandomAccessFile[totalTempFiles*2];
        
        // Inicializacao dos Arquivos Temporarios
        for(int i = 0; i < file.length; i++) {
            nameFiles[i] = new File("temp" + i);
            file[i] = new RandomAccessFile(nameFiles[i], "rw");
        }

        // Etapa de Distribuicao
        distribution(readFile, file[0], file[1], recordsForMemory);
        
        step = stridesCalculation(totalTempFiles, totalRecords/recordsForMemory);

        // Exclusao dos Arquivos Temporarios
        for(int i = 0; i < file.length; i++) {
            nameFiles[i].delete();
            file[i] = null;
        }
        file = null;
    }   

    // Metodo de Distribuiçao 
    private static void distribution(RandomAccessFile readFile, RandomAccessFile firstFile, RandomAccessFile secondFile, int recordsForMemory) throws Exception {
        readFile.seek(0);
        readFile.skipBytes(4);
        long position = readFile.getFilePointer();

        while(position < readFile.length()) {
            position = writeFileBlocks(firstFile, readFile, recordsForMemory);

            if(position < readFile.length()) {
                position = writeFileBlocks(secondFile, readFile, recordsForMemory);
            }
        }
    }

    // Metodo de Escrever Blocos de Registros em Arquivo
    private static long writeFileBlocks(RandomAccessFile file, RandomAccessFile readToFile, int totalRegister) throws IOException, Exception {
        Games[] games = new Games[totalRegister];

        int i = 0;
        for(; i < totalRegister; i++) {
            if(readToFile.getFilePointer() < readToFile.length()) {
                games[i] = DataBase.readBytesForGames(readToFile, readToFile.getFilePointer());
            } else {
                break;
            }
        }

        quickSort(games, 0, i-1);

        for(int j = 0; j < totalRegister; j++) {
            if(games[j] != null) {
                DataBase.create(file, games[j]);
            } else {
                break;
            }
        }

        return readToFile.getFilePointer();
    }

    // Metodo de Ordenacao na Memória Principal com QuickSort
    private static void quickSort(Games[] games, int left, int right) {
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

    // Metodo para calcular o total de Intercalacoes
    private static int stridesCalculation(int m, int n) {
        return (int)(1 + Math.round(log(m, n)));
    }

    // Metodo para calcular logaritmo
    private static double log(int base, int value) {
        return Math.log(value) / Math.log(base);
    }
    
    // Metodo de Intercalar os Arquivos
    private static void intercalation(RandomAccessFile firstFile, RandomAccessFile secondFile, RandomAccessFile thirdFile, RandomAccessFile fourthFile, int limit) throws Exception {
        firstFile.seek(0);
        secondFile.seek(0);

        int counterFirstFile = 0, counterSecondFile = 0;
        Games gameFirstFile = null, gameSecondFile = null;
        boolean writingChecker = true;

        while(eof(firstFile) && eof(secondFile)) {
            if(counterFirstFile < limit) {
                if(gameFirstFile == null) {
                    gameFirstFile = DataBase.readBytesForGames(firstFile, firstFile.getFilePointer());
                    counterFirstFile++;
                }
            } else { break; }

            if(counterSecondFile < limit) {
                if(gameSecondFile == null) {
                    gameSecondFile = DataBase.readBytesForGames(secondFile, secondFile.getFilePointer());
                    counterSecondFile++;
                }
            } else { break; }

            if(gameFirstFile.getApp_id() < gameSecondFile.getApp_id()) {
                if(writingChecker) {
                    DataBase.create(thirdFile, gameFirstFile);
                    gameFirstFile = null;
                    writingChecker = !writingChecker;
                } else {
                    DataBase.create(fourthFile, gameFirstFile);
                    gameFirstFile = null;
                    writingChecker = !writingChecker;
                }
            } else {
                if(writingChecker) {
                    DataBase.create(thirdFile, gameSecondFile);
                    gameSecondFile = null;
                    writingChecker = !writingChecker;
                } else {
                    DataBase.create(fourthFile, gameSecondFile);
                    gameSecondFile = null;
                    writingChecker = !writingChecker;
                }
            }
        }

        // Se ainda tiver Registro no Segundo Arquivo
        while(eof(secondFile) && counterSecondFile < limit) {
            gameSecondFile = DataBase.readBytesForGames(secondFile, secondFile.getFilePointer());
            counterSecondFile++;

            if(writingChecker) {
                DataBase.create(thirdFile, gameSecondFile);
                writingChecker = !writingChecker;
            } else {
                DataBase.create(fourthFile, gameSecondFile);
                writingChecker = !writingChecker;
            }
        }

        // Se ainda tiver Registro no Primeiro Arquivo
        while(eof(firstFile) && counterFirstFile < limit) {
            gameFirstFile = DataBase.readBytesForGames(firstFile, firstFile.getFilePointer());
            counterFirstFile++;

            if(writingChecker) {
                DataBase.create(thirdFile, gameFirstFile);
                writingChecker = !writingChecker;
            } else {
                DataBase.create(fourthFile, gameFirstFile);
                writingChecker = !writingChecker;
            }
        }
        
        firstFile.setLength(0);
        secondFile.setLength(0);
    }

    // Metodo de Verificacao de Fim de Arquivo
    private static boolean eof(RandomAccessFile file) throws IOException {
        return file.getFilePointer() < file.length();
    }
}