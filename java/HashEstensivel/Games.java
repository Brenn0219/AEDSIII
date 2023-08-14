import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DecimalFormat;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

class Games {
    // Atributos Privados
    private int app_id;
    private String name;
    private Date release_date;
    private String owners;
    private int age;
    private double price;
    private int dlcs;
    private String[] languages; 
    private String website;
    private boolean windows;
    private boolean mac;
    private boolean linux;
    private double upvotes;
    private int avg_pt;
    private String developes;
    private String[] genres;
    private boolean fileValue;

    // Construtor sem Parametro
    public Games() {
        app_id = 0;
        name = "";
        release_date = null;
        owners = "";
        age = 0;
        price = 0;
        dlcs = 0;
        languages = null;
        website = "";
        windows = false;
        mac = false;
        linux = false;
        upvotes = 0.0;
        avg_pt = 0;
        developes = "";
        genres = null;
        fileValue = true;
    }

    // Construtor com Parametro
    public Games(int app_id, String name, Date release_date, String owners, int age, double price, int dlcs, String[] languages,  String website, boolean windows, boolean mac, boolean linux, Double upvotes, int avg_pt, String developes,String[] genres, Boolean fileValue) {
        setApp_id(app_id);
        setName(name);
        setRelease_date(release_date);
        setOwners(owners);
        setAge(age);
        setPrice(price);
        setDlcs(dlcs);
        setLanguages(languages);
        setWebsite(website);
        setWindows(windows);
        setMac(mac);
        setLinux(linux); 
        setUpvotes(upvotes);
        setAvg_pt(avg_pt);
        setDevelopes(developes);
        setGenres(genres);
    }

    // Setters
    public void setApp_id(int app_id) { this.app_id = app_id;}
    public void setName(String name) { this.name = name; }
    public void setRelease_date(Date release_date) { this.release_date = release_date;}
    public void setOwners(String owners) { this.owners = owners; }
    public void setAge(int age) { this.age = age; }
    public void setPrice(double price) { this.price = price; }
    public void setDlcs(int dlcs) { this.dlcs = dlcs; }
    public void setLanguages(String[] languages) { this.languages = languages; }
    public void setWebsite(String website) { this.website = website; }
    public void setWindows(boolean windows) {this.windows = windows; }
    public void setMac(boolean mac) { this.mac = mac; }
    public void setLinux(boolean linux) { this.linux = linux; }
    public void setUpvotes(Double upvotes) { this.upvotes = upvotes; }
    public void setAvg_pt(int avg_pt) { this.avg_pt = avg_pt; }
    public void setDevelopes(String developes) { this.developes = developes; }
    public void setGenres(String[] strings) { this.genres = strings; }
    public void setFileValue(boolean fileValue) { this.fileValue = fileValue; }

    // Getters
    public int getApp_id() { return app_id; }
    public String getName() { return name; }
    public Date getRelease_date() { return release_date; }
    public String getOwners() { return owners; }
    public int getAge() { return age; }
    public double getPrice() { return price; }
    public int getDlcs() { return dlcs; }
    public String[] getLanguages() { return languages; }
    public String getWebsite() { return website; }
    public boolean getWindows() { return windows; }
    public boolean getMac() { return mac; }
    public boolean getLinux() { return linux; }
    public Double getUpvotes() { return upvotes; }
    public int getAvg_pt() { return avg_pt; }
    public String getDevelopes() { return developes; }
    public String[] getGenres() { return genres; }
    public Boolean getFileValue() { return fileValue; }

    // Metodo para imprimir Horas e os Minutos corretamente
    private String formatHours(int seconds) {
        int hours = seconds / 60;
        int min = seconds % 60;
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
    private String formatPercentage(Double conta) {
        return ((int)Math.round(conta * 100) + "%");
    }

    // Metodo para imprimir Web se existir se nao, imprimir null
    public static String formatWeb(String web) {
        String webFormat = web;
        
        if(web.equals("")) {
            webFormat = "null";
        }
        
        return webFormat;
    }

    // Metodo para imprimir o Array Languages
    private String formatArray(String[] array) {
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
    private String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(price);
    }

    // Metodo para calcular a Porcentagem do Upvotes
    private double calculatePercentage(String upvotes, String notUpvotes) {
        return (Double.parseDouble(upvotes) / (Double.parseDouble(upvotes) + Double.parseDouble(notUpvotes)));
    }

    // Metodo para transformar Date em String 
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy", Locale.US);
        String dateString = sdf.format(date);
        return dateString;
    }

    // Metodo para transformar String em Date 
    static Date convertToDate(String strData) throws Exception {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            date = sdf.parse(strData);
        } catch(ParseException e) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.US);
                date = sdf.parse(strData);
            } catch(ParseException pe) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy", Locale.US);
                date = sdf.parse(strData);
            }
        }
        return date;
    }

    // Metodo para Separar os Atributos e colocar em uma Array
    private String[] separateAttributes(String line) {
        String[] arrayString = new String[17];
        String helperString = "";
        int counter = 0;

        for(int i=0; i<line.length(); i++) {
            if(i == line.length()-1) {
                helperString += line.charAt(i);
                arrayString[counter++] = helperString;
                helperString = "";
            } else {
                if(line.charAt(i) == '"') {
                    int pos = 0 ;
                    for(int j=i+1; j<line.length(); j++) {
                        if(line.charAt(j) != '"') {
                            helperString += line.charAt(j);
                        } else {
                            arrayString[counter++] = helperString;
                            helperString = "";
                            pos = j;
                            j = line.length();
                        }
                    }
                    i = pos+1;
                } else {
                    if(i < line.length()) {
                        if(line.charAt(i) != ',') {
                            helperString += line.charAt(i);
                        } else {
                            arrayString[counter++] = helperString;
                            helperString = "";
                        }
                    } 
                }
            }
        }

        return arrayString;
    }

    // Metodo de ler
    public void toRead(String line) throws Exception {
        String[] arrysAtributos = separateAttributes(line);

        setApp_id(Integer.parseInt(arrysAtributos[0]));
        setName(arrysAtributos[1]);
        setRelease_date(convertToDate(arrysAtributos[2])); 
        setOwners(arrysAtributos[3]);
        setAge(Integer.parseInt(arrysAtributos[4]));
        setPrice(Double.parseDouble(arrysAtributos[5]));
        setDlcs(Integer.parseInt(arrysAtributos[6]));
        setLanguages(arrysAtributos[7].replace(']', ' ').replace('[', ' ').replaceAll(" ", "").replaceAll("'", "").replaceAll("\"", "").split(","));
        setWebsite(arrysAtributos[8]);
        setWindows(Boolean.parseBoolean(arrysAtributos[9]));
        setMac(Boolean.parseBoolean(arrysAtributos[10]));
        setLinux(Boolean.parseBoolean(arrysAtributos[11]));
        setUpvotes(calculatePercentage(arrysAtributos[12], arrysAtributos[13]));
        setAvg_pt(Integer.parseInt(arrysAtributos[14]));
        setDevelopes(arrysAtributos[15].replaceAll("\"", ""));
        setGenres((arrysAtributos[16] != null) ? arrysAtributos[16].replaceAll("\"", "").split(","):null);
	}

    // Metodo de Imprimir
    public void show() {
        System.out.println(getApp_id() + " " + getName() + " " + formatDate(getRelease_date()) + " " + getOwners() + " " + getAge() + " " + formatPrice(getPrice()) + " " + getDlcs() + " " + formatArray(getLanguages()) + " " + formatWeb(getWebsite()) + " " + getWindows() + " " + getMac() + " " + getLinux() + " " + formatPercentage(getUpvotes()) + " " + formatHours(getAvg_pt()) + " " + getDevelopes() + " " + formatArray(getGenres()));
    }

    // Transformando os Atributos do Game em um Array de Bytes
    public byte[] toByteArray() throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeBoolean(getFileValue());
        dos.writeInt(getApp_id());
        
        dos.writeInt(getName().length());
        dos.writeUTF(getName());
        
        dos.writeInt(formatDate(getRelease_date()).length());
        dos.writeUTF(formatDate(getRelease_date()));
        
        dos.writeInt(getOwners().length());
        dos.writeUTF(getOwners());
        
        dos.writeInt(getAge());
        dos.writeDouble(getPrice());
        dos.writeInt(getDlcs());
        
        String[] languages = getLanguages();
        dos.writeInt(languages.length);
        for(int i = 0; i < languages.length; i++) {
            dos.writeInt(languages[i].length());
            dos.writeUTF(languages[i]);
        }
        
        dos.writeInt(formatWeb(getWebsite()).length());
        dos.writeUTF(formatWeb(getWebsite()));
        
        dos.writeBoolean(getWindows());
        dos.writeBoolean(getLinux());
        dos.writeBoolean(getMac());
        dos.writeDouble(getUpvotes());
        dos.writeInt(getAvg_pt());

        dos.writeInt(getDevelopes().length());
        dos.writeUTF(getDevelopes());

        String[] genres = getGenres();
        dos.writeInt(genres.length);
        for(int i = 0; i < genres.length; i++) {
            dos.writeInt(genres[i].length());
            dos.writeUTF(languages[i]);
        }

        return baos.toByteArray();
    }
}