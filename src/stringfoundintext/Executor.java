package stringfoundintext;

import java.io.File;
import java.io.IOException;

/**
 * Passes control to searching engine.
 *
 * @author vvn
 */
public class Executor {

    public static void main(String[] args) {

        String dataSource = "/Users/prologistic/source.txt";
        String instuctionsSource = "/Users/prologistic/source.txt";
        if (args.length == 2) {
            checkFileExistence(args[0], "FileName"); 
            checkFileExistence(args[1], "FileDate");
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

        try {
            new SwiftDataAnalyzer(instuctionsSource, new DataParser(dataSource), new ResultWriter(dataSource)).execute();
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
}
