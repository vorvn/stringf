package reconciliationtrade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Passes control to searching engine.
 *
 * @author vvn
 */
public class ReconciliationTrade {
    static int COUNT_FOUND_FIELDS = 8 ;
    
    public static void main(String[] args) {

        String dataSource = "/Users/prologistic/source.txt";
        String instuctionsSource = "/Users/prologistic/source.txt";
        String dataMessage = null; 
        if (args.length == 2) {
            checkFileExistence(args[0], "FileName"); 
            dataSource = args[0];
            checkFileExistence(args[1], "FileDate");
            instuctionsSource = args[1];
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(" Not found FileName and FileDate. ");
            sb.append("\n Example: ");
            sb.append("\n          ReadFileToString.jar 0707$1MH.300 in.dat");
            System.out.println(sb.toString());
            System.exit(0);
            // RuntimeException re = new RuntimeException(sb.toString());
            // throw re;
        }

        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        System.out.println(format.format(d)); //25.02.2013 09:03

        try {
            dataMessage = readUsingFiles(dataSource);
        } catch (IOException ex) {
            Logger.getLogger(ReconciliationTrade.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            new SwiftDataAnalyzer(instuctionsSource, 
                                  new DataParser(dataMessage), 
                                  new ResultWriter(dataSource)).execute();
        } catch (SearcherException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    private static void checkFileExistence(String path, String variableName) {
        File file = new File(path);
        if (!(file.exists() && file.isFile())) {
            System.out.println(" Not found " + variableName + ". ");
            System.exit(0);
        }
    }

    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
