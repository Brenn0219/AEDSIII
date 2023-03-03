import java.io.File;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("teste.csv"));
        Games game = new Games();
        Crud readFile = new Crud("arquivo");

        while(sc.hasNext()) {
            game.toRead(sc.nextLine()); 
            readFile.create(game);
        }

        // Games gameCrud = readFile.select(440);
        
		// if(gameCrud != null) {
        //     gameCrud.show();
        // }

        // gameCrud.setName("Counter-Strike Nexon: Studio Deathmatch Classic A Story About My Uncle");
        // readFile.update(gameCrud);
        // gameCrud.show();

		// readFile.show();

        readFile.sort();

        readFile.deleteFile();
        sc.close();
    }
}
