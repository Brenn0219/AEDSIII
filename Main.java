import java.io.File;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("games.csv"));
        Games game = new Games();
        Crud readFile = new Crud("arquivo");

        while(sc.hasNext()) {
            game.toRead(sc.nextLine()); 
            readFile.create(game);                                                  
        }

        // readFile.sort();
        readFile.flexibleOrdering();

        // Games gameCrud = readFile.select(440);
        
		// if(gameCrud != null) {
        //     gameCrud.show();
        // }

        // gameCrud.setName("Counter-Strike Nexon: Studio Deathmatch Classic A Story About My Uncle");
        // readFile.update(gameCrud);

        // readFile.delete(30);

		// readFile.show();

        readFile.deleteFile();
        sc.close();
    }
}
