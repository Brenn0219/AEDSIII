import java.io.File;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("games.csv"));
        Games game = new Games();
        DataBase readFile = new DataBase("arquivo");

        while(sc.hasNext()) {
            game.toRead(sc.nextLine()); 
            readFile.create(game);                                                  
        }

        readFile.showTree();

        readFile.deleteFile();
        sc.close();
    }
}
