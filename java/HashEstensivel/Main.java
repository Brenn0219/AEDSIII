import java.io.File;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("teste.csv"));
        Games game = new Games();
        DataBase readFile = new DataBase("arquivo");

        while(sc.hasNext()) {
            game.toRead(sc.nextLine()); 
            readFile.create(game);                                                  
            // game.show();
        }
        
        game = readFile.read(730);
        if(game != null) game.show();
        
        // readFile.showHash();

        readFile.deleteFile();
        sc.close();
    }
}
