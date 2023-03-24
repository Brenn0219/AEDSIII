import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileSorting {
    private RandomAccessFile[] file;
    private int totalTempFiles;

    FileSorting(File namFile) throws IOException {
        this(namFile, 2);
    }

    FileSorting(File nameFile, int totalTempFiles) {
        file = new RandomAccessFile[totalTempFiles*2];
    }

    public static void sort() throws IOException {
        
        
    }

    private int stridesCalculation() {
        return (int)(1 + Math.round(log(totalTempFiles, 4173)));
    }

    private double log(int value, int base) {
        return Math.log(value) / Math.log(base);
    }
}