import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class GamePrinting {
    // Metodo para imprimir Horas e os Minutos corretamente
    public static String formatHours(Game game) {
        int avgPt = game.getAvgPt();
        int hours = avgPt / 60;
        int min = avgPt % 60;
        String horary = "";

        if(hours == 0 && min == 0) {
            horary = " null";
        } else if (hours > 0 && min > 0) {
            horary = (hours + "h " + min + "m");
        } else {
            if(hours > 0) {
                horary = (hours + "h");
            }
            if(min > 0) {
                horary = (min + "m");
            }   
        }

        return horary;
    }

    // Metodo para Arredondar a Porcenyagem
    public static String formatPercentage(Game game) {
        return ((int)Math.round(game.getUpvotes() * 100) + "%");
    }

    // Metodo para imprimir os Array
    public static String formatArray(String[] array) {
        String format = "[";
        if(array != null) {
            for(int i = 0; i < array.length; i++) {
                if(i != array.length - 1) {
                    format += (array[i] + ", ");
                } else {
                    format += array[i];
                }
            }
        } else {
            format = "null";
        }
        format += "]";

        return format;
    }

    // Metodo para formatar casas Decimais em Price
    public static String formatPrice(Game game) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(game.getPrice());
    }

    // Metodo para transformar Date em String 
    public static String formatDate(Game game) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy", Locale.US);
        String dateString = sdf.format(game.getReleaseDate());
        return dateString;
    }

    // Metodo de Imprimir
    public static void show(Game game) {
        System.out.println(game.getAppId() + " " + game.getName() + " " + formatDate(game) + " " + game.getOwners() + " " + game.getAge() + " " + formatPrice(game) + " " + game.getDlcs() + " " + formatArray(game.getLanguages()) + " " + game.getWebsite() + " " + game.getWindows() + " " + game.getMac() + " " + game.getLinux() + " " + formatPercentage(game) + " " + formatHours(game) + " " + game.getDevelopes() + " " + formatArray(game.getGenres()));
    }
}
