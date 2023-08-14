import java.io.File;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("teste.csv"));
        Game game = new Game();
        SequentialFile sequentialFile = new SequentialFile("sequentialFile");
        IndexedFile indexedFile = new IndexedFile("indexedFile");

        while(sc.hasNext()) {
            game.toRead(sc.nextLine()); 
            indexedFile.create(game.getAppId(), sequentialFile.create(game));                                                  
        }

        game = sequentialFile.readBytesForGame(indexedFile.read(730));
        GamePrinting.show(game);

        game.setName("Brenno Augusto");
        indexedFile.update(game.getAppId(), sequentialFile.update(game));
        
        game = sequentialFile.readBytesForGame(indexedFile.read(730));
        GamePrinting.show(game);

        sequentialFile.delete(730);
        indexedFile.delete(730);
        game = sequentialFile.readBytesForGame(indexedFile.read(730));
        if(game != null) {
            GamePrinting.show(game);
        }
    }
}
