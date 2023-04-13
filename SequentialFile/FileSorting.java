import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileSorting {
    
    // Ordenacao de Arquivo
    public static void sort(RandomAccessFile readFile) throws Exception {
        sort(readFile, 2);
    }

    public static void sort(RandomAccessFile readFile, int totalTempFiles) throws Exception {
        int step, totalRecords = 91, recordsForMemory = 15, n = recordsForMemory;
        File[] nameFiles = new File[totalTempFiles*2];
        RandomAccessFile[] file = new RandomAccessFile[totalTempFiles*2];
        
        // Inicializacao dos Arquivos Temporarios
        for(int i = 0; i < file.length; i++) {
            nameFiles[i] = new File("temp" + i);
            file[i] = new RandomAccessFile(nameFiles[i], "rw");
        }

        // Etapa de Distribuicao
        distribution(readFile, file[0], file[1], recordsForMemory);
        
        // Calculo para definir quantas intercalacoes o algoritmo fara
        step = stridesCalculation(totalTempFiles, totalRecords/recordsForMemory);

        // Intercalando os arquivos como; os dois primeiros parametros sao arquivos para ler os registros e intercalar no terceiro e penultimo parametros que sao os arquivos de escrita
        int j;
        for(j = 0; j < step; j++) {
            if (j == 1) { n *= 2;}

            if(j % 2 == 0) {
                intercalation(file[0], file[1], file[2], file[3], n);
            } else {
                intercalation(file[2], file[3], file[0], file[1], n);
            }
        }

        // DataBase.show(file[0]);

        // readFile.setLength(0);
        // --j;
        // if(j % 2 == 0) {
        //     mergeToOriginalFile(readFile, file[2], file[3]);
        // } else {
        //     mergeToOriginalFile(readFile, file[1], file[0]);
        // }

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
    
    // Metodo de Intercalar entre os Arquivos
    private static void intercalation(RandomAccessFile firstFile, RandomAccessFile secondFile, RandomAccessFile thirdFile, RandomAccessFile fourthFile, int limit) throws Exception {
        // Inicializando os arquivos que irao ser lido os registros
        firstFile.seek(0);
        secondFile.seek(0);
        firstFile.skipBytes(4);
        secondFile.skipBytes(4);

        int counterFirstFile = 0, counterSecondFile = 0;
        Games gameFirstFile = null, gameSecondFile = null;
        boolean writingChecker = true;

        while(eof(firstFile) && eof(secondFile)) {
            if(counterFirstFile >= limit && counterSecondFile >= limit) {
                counterFirstFile = 0;
                counterSecondFile = 0;
                writingChecker = !writingChecker;
            }

            if(counterFirstFile < limit) {
                if(gameFirstFile == null) {
                    gameFirstFile = DataBase.readBytesForGames(firstFile, firstFile.getFilePointer());
                    counterFirstFile++;
                }
            } 

            if(counterSecondFile < limit) {
                if(gameSecondFile == null) {
                    gameSecondFile = DataBase.readBytesForGames(secondFile, secondFile.getFilePointer());
                    counterSecondFile++;
                }
            } 

            if(gameFirstFile != null && gameSecondFile != null) {
                if(gameFirstFile.getApp_id() < gameSecondFile.getApp_id()) {
                    if(writingChecker) {
                        DataBase.create(thirdFile, gameFirstFile);
                    } else {
                        DataBase.create(fourthFile, gameFirstFile);
                    }
                    gameFirstFile = null;
                } else {
                    if(writingChecker) {
                        DataBase.create(thirdFile, gameSecondFile);
                    } else {
                        DataBase.create(fourthFile, gameSecondFile);
                    }
                    gameSecondFile = null;
                }
            } else if (gameFirstFile == null) {
                if(writingChecker) {
                    DataBase.create(thirdFile, gameSecondFile);
                } else {
                    DataBase.create(fourthFile, gameSecondFile);
                }
                gameSecondFile = null;
            } else {
                if(writingChecker) {
                    DataBase.create(thirdFile, gameFirstFile);
                } else {
                    DataBase.create(fourthFile, gameFirstFile);
                }
                gameFirstFile = null;
            }
        }

        // Se ainda tiver Registro no Segundo Arquivo
        while(eof(secondFile)) {
            gameSecondFile = DataBase.readBytesForGames(secondFile, secondFile.getFilePointer());

            if(writingChecker) {
                DataBase.create(thirdFile, gameSecondFile);
            } else {
                DataBase.create(fourthFile, gameSecondFile);
            }
        }

        // Se ainda tiver Registro no Primeiro Arquivo
        while(eof(firstFile)) {
            gameFirstFile = DataBase.readBytesForGames(firstFile, firstFile.getFilePointer());

            if(writingChecker) {
                DataBase.create(thirdFile, gameFirstFile);
            } else {
                DataBase.create(fourthFile, gameFirstFile);
            }
        }
        
        firstFile.setLength(0);
        secondFile.setLength(0);
    }

    // Metodo de Verificacao de Fim de Arquivo
    private static boolean eof(RandomAccessFile file) throws IOException {
        return file.getFilePointer() < file.length();
    }

    private static void mergeToOriginalFile(RandomAccessFile file, RandomAccessFile firstFile, RandomAccessFile secondFile) throws Exception {
        firstFile.seek(0);
        secondFile.seek(0);
        firstFile.skipBytes(4);
        secondFile.skipBytes(4);

        Games gameFirstFile = null, gameSecondFile = null;

        while(eof(firstFile) && eof(secondFile)) {
            if(gameFirstFile == null) {
                gameFirstFile = DataBase.readBytesForGames(firstFile, firstFile.getFilePointer());
            } 

            if(gameSecondFile == null) {
                gameSecondFile = DataBase.readBytesForGames(secondFile, secondFile.getFilePointer());
            } 

            if(gameFirstFile != null && gameSecondFile != null) {
                if(gameFirstFile.getApp_id() < gameSecondFile.getApp_id()) {
                    DataBase.create(file, gameFirstFile);
                    gameFirstFile = null;
                } else {
                    DataBase.create(file, gameFirstFile);
                    gameSecondFile = null;
                }
            } else if (gameFirstFile == null) {
                DataBase.create(file, gameSecondFile);
                gameSecondFile = null;
            } else {
                DataBase.create(file, gameFirstFile);
                gameFirstFile = null;
            }
        }

        // Se ainda tiver Registro no Segundo Arquivo
        while(eof(secondFile)) {
            gameSecondFile = DataBase.readBytesForGames(secondFile, secondFile.getFilePointer());
            DataBase.create(file, gameSecondFile);
        }

        // Se ainda tiver Registro no Primeiro Arquivo
        while(eof(firstFile)) {
            gameFirstFile = DataBase.readBytesForGames(firstFile, firstFile.getFilePointer());
            DataBase.create(file, gameFirstFile);
        }
    }
}