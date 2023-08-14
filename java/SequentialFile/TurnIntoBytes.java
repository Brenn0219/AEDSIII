import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class TurnIntoBytes {
    // Metodo para transfor os Atributos do Game em um Array de Bytes
    public static byte[] toByteArray(Game game, boolean status) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeBoolean(status); // status do registro no arquivo - true(ativo) / false(excluido)
            dos.writeInt(game.getAppId());
            
            dos.writeInt(game.getName().length());
            dos.writeUTF(game.getName());
            
            dos.writeInt(GamePrinting.formatDate(game).length());
            dos.writeUTF(GamePrinting.formatDate(game));
            
            dos.writeInt(game.getOwners().length());
            dos.writeUTF(game.getOwners());
            
            dos.writeInt(game.getAge());
            dos.writeDouble(game.getPrice());
            dos.writeInt(game.getDlcs());
            
            String[] languages = game.getLanguages();
            dos.writeInt(languages.length);
            for(int i = 0; i < languages.length; i++) {
                dos.writeInt(languages[i].length());
                dos.writeUTF(languages[i]);
            }
            
            dos.writeInt(game.getWebsite().length());
            dos.writeUTF(game.getWebsite());
            
            dos.writeBoolean(game.getWindows());
            dos.writeBoolean(game.getLinux());
            dos.writeBoolean(game.getMac());
            dos.writeDouble(game.getUpvotes());
            dos.writeInt(game.getAvgPt());

            dos.writeInt(game.getDevelopes().length());
            dos.writeUTF(game.getDevelopes());

            String[] genres = game.getGenres();
            dos.writeInt(genres.length);
            for(int i = 0; i < genres.length; i++) {
                dos.writeInt(genres[i].length());
                dos.writeUTF(genres[i]);
            }

            return baos.toByteArray();
        } catch (Exception e) {
            System.err.println("Error converting a Game to a byte arrya - " + e.getMessage());
            return null;
        }
    }
}
