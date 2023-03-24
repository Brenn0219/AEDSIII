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
        // Games gameCrud = readFile.read(440);
        
		// if(gameCrud != null) {
        //     gameCrud.show();
        // }

        // gameCrud.setName("Counter-Strike Nexon: Studio Deathmatch Classic A Story About My Uncle");
        // readFile.update(gameCrud);
        // gameCrud.show();

		// readFile.show();

        // readFile.sort();
        // readFile.show();

        // readFile.flexibleOrdering();

        // Heap heap = new Heap(readFile.getFile());
        // heap.sort();            

        FileSorting.sort(readFile.getFile());

        readFile.deleteFile();
        sc.close();
    }
}
