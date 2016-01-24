/*
 *    Программа Поиска Информации в СВИФТ-Текстовом файле, согластно определеным
 * правилам прописанным в строках файла DAT.
 *    Поиск Инф-ции происходит по 8-ми параметрам из каждой строки файла DAT.
 */
package stringfoundintext;
//package StringFoundInTest;
 
/**
 *
 * @author Swt-5win
 */
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
 
//import org.apache.commons.io.FileUtils;
 
// в этом классе показаны разные способы скопировать содержимое файла в строку
public class StringFoundInText {
    // CAMEL_CASE_STYLE (Java Code Convention)
    static int countFoundFeilds = 8 ; // количество Полей поиска в СтрокеПоиска
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String fileName = "/Users/prologistic/source.txt";
        //String fileName;
        String fileDate = "/Users/prologistic/source.txt";
        if (args.length>1)
        {    
            fileName = args[0];
            File file = new File(fileName);
            if (!(file.exists() && file.isFile())) {
               System.out.println(" Not found FileName. ");
               System.exit(0);
            }
            fileDate = args[1];
            file = new File(fileDate);
            if (!(file.exists() && file.isFile())) {
               System.out.println(" Not found FileDate. ");
               System.exit(0);
            }
        }
        else
        {
            System.out.println(" Not found FileName and FileDate. ");
            System.out.println(" Example: ");
            System.out.println("          ReadFileToString.jar 0707$1MH.300 in.dat");
            System.exit(0);
        }
       
        // читаем файл в строку с помощью класса Files
        String mycontents = readUsingFiles(fileName);

        String aFileName, aLogFile, aViewScreen;
        aFileName = aLogFile = aViewScreen = "";
        BufferedReader reader = new BufferedReader( new FileReader (fileDate));
        String line;
        String ls = System.getProperty("line.separator");
        while(( line = reader.readLine()) != null ) {
            if (line.length()>0 && line.charAt(0)!='#')
            {
                String[] lineSearch = line.split("\\|");
                // lineSearch.length - количество Полей поиска в СтрокеПоиска
                if (lineSearch.length < countFoundFeilds)
                    System.out.println("Error: CountFoundFields in Line < 8. " + line);
                else
                {
                    String aField = lineSearch[0].substring(1,lineSearch[0].length()-1);
                    int aNumStr = Integer.parseInt(lineSearch[1]);
                    String aFirstStr = lineSearch[2].substring(1,lineSearch[2].length()-1);
                    int aFirstShift = Integer.parseInt(lineSearch[3]);
                    String aSecondStr = lineSearch[4].substring(1,lineSearch[4].length()-1);
                    int aSecondShift = Integer.parseInt(lineSearch[5]);
                    String aFormat = lineSearch[6];
                    String aCommand = lineSearch[7];
                    String aRezult = searchString(mycontents, aField, aNumStr,
                            aFirstStr, aFirstShift,
                            aSecondStr, aSecondShift, aFormat) ;
                    switch (aCommand.charAt(0)) {
                        case 'L':
                            aLogFile = aLogFile.concat(line);
                            aLogFile = aLogFile.concat(ls);
                            aLogFile = aLogFile.concat(aRezult);
                            aLogFile = aLogFile.concat(ls);
                            break;
                        case 'F':
                            aFileName = aFileName.concat(aRezult);
                            aFileName = aFileName.concat("_");
                            break;
                        default:
                            aViewScreen = aViewScreen.concat(line);
                            aViewScreen = aViewScreen.concat(ls);
                            aViewScreen = aViewScreen.concat(aRezult);
                            aViewScreen = aViewScreen.concat(ls);
                            System.out.println(aViewScreen);
                            aViewScreen ="";
                            break;
                    }    
                }
            }                  
        }
        // Выделение Имени файла из "Пути+Имя"
        String fileNameNew = fileName ; // Имя для Вых.Файла
        if (aFileName.length()<1) fileNameNew = fileName ;
        else
        {  
            // Вывод на экран Ошибки в "СтрокеСверки"
            if (aFileName.indexOf("Error",0) > 0) System.out.println(aFileName);
            else
            {   // ИмяСтрокиПоиска
                String aTemp = aFileName.substring(0,aFileName.length()-1);
                String aTemp1; // = "";
                int bslashIdx = fileName.lastIndexOf('\\');
                // Выделение "чистое" Имя из Путь+Имя
                if (bslashIdx != -1) aTemp1 = fileName.substring(bslashIdx + 1, fileName.length());
                else aTemp1 = fileName;
                // Формирование из "СтрокеСверки" будушее Имя Выходного Файла
                fileNameNew = aTemp.concat("." + aTemp1.replace('.', '_'));
                File fileNewName = new File(fileNameNew);
                File fileOld = new File(fileName);
                int vyh = 0;
                while (vyh < 100)
                // переименование обработанного файла именем "СтрокиСверки"."СтароеИмя"
        if (fileOld.renameTo(fileNewName))
                {
                    System.out.println("Rename Successfully " + fileOld + " to " + fileNameNew);
                    vyh = 100;
        }
                else
                {
                    System.out.println("Error Rename " + fileOld + " to " + fileNameNew);
                    vyh ++;
                    // переименование обработанного файла именем "СтрокиСверки"."СтароеИмя"+КопияVyh
                    File fileNewName1 = new File (fileNameNew + "(" + vyh + ")");
                    fileNewName = fileNewName1;
        }
            }
        }
        //  Запись"СтрокиСверок" в Имя Выходного Файла: "СтрокиСверки"."СтароеИмя"+"_LOG"
        if (aLogFile.length()>0)
        {    
            // запись в файл  fileName + "_LOG"
            FileWriter writeFile = null;
            try {
                    File logFile = new File(fileNameNew.concat("_LOG"));
                    writeFile = new FileWriter(logFile);
                    writeFile.write(aLogFile);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(writeFile != null) {
                        try {
                                writeFile.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                }
            }
        }    
        if (aViewScreen.length()>0)
        {    
            System.out.println(aViewScreen);
        }
    }
 
    // считываем содержимое файла в String с помощью BufferedReader
    private static String readUsingBufferedReader(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (fileName));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
 
    // читаем файл в строку с помощью класса Files
    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
 
    // читаем файл с помощью Scanner
    private static String readUsingScanner(String fileName) throws IOException {
        String data;
        //здесь мы можем использовать разделитель, например: "\\A", "\\Z" или "\\z"
        try (Scanner scanner = new Scanner(Paths.get(fileName), StandardCharsets.UTF_8.name())) {
            //здесь мы можем использовать разделитель, например: "\\A", "\\Z" или "\\z"
            data = scanner.useDelimiter("[\n|]").next();
        }
        return data;
    }
   
    // выделение сроки из текта СООБЩЕНИЯ по легенде из DAT файла
    private static String searchString(String data, String s0, int r0, String s1,
                                            int r1, String s2, int r2, String fs ) {
        //здесь мы можем использовать разделитель, например: "\\A", "\\Z" или "\\z"
        String sSearch = " ";
        int k, kk, kkk, kkkk, k4 ;
        //k = kk = kkk = kkkk = 0;
        if (data.indexOf(s0,0) < 0)
            return sSearch = " Error: 1-param not found.";
        k = data.indexOf(s0,0);
        if (r0 < 0) return sSearch = " Error: 2-param < 0.";  
        for (int i = 0; i < r0; i++)
        {
           int akR = data.indexOf("\r",k) ;
           int akN = data.indexOf("\n",akR) ;
           if ( k < akR ) k = akR ;
           if ( k < akN ) k = akN ;
           if ((data.length() - k)<5) sSearch = " Error: 2-param > endOfFile.";  
        }    
        if (data.indexOf(s1,k) < 0) return sSearch = " Error: 3-param not found.";
        k4 = data.indexOf(s1,k);
        if ((k4+r1) < 0) return sSearch = " Error: 4-param < beginOfFile.";
        k = k4 ;
        if (s1.equals("")) kkkk = k + r1; else kkkk = k4;
        if (s2.equals("\n")||s2.equals("\\n"))
        {
           int akR = data.indexOf("\r",k4) ;
           int akN = data.indexOf("\n",akR) ;
           if ( k4 < akR ) k4 = akR ;
           if ( k4 < akN ) k4 = akN ;
           if ((data.length() - k4)<5) sSearch = " Error: 4-param > endOfFile.";  
        }
        else
        {
           if (s2.equals("")) kk = k4;
           else
           {
             if (data.indexOf(s2,kkkk+1)<0) return sSearch = " Error: 5-param not found.";
             kk = data.indexOf(s2,kkkk+1) ;
             if ( k4 < kk ) k4 = kk + 1 ;
           }
           if ( k4 == k ) k4 = k + r1 ;
        };
        if ( k+r1 > k4+r2 ) return sSearch = " Error: Position 6-param < 4-param. Length (object) < 0.";  
        sSearch = data.substring(k+r1, k4+r2);
        char ch = fs.charAt(0);
        if (ch =='N')
        {  
           kk = fs.length()-1;
           k = 0;
           for (int i=1; i<=kk; i++)
           {  
               kkk =1;
               if (i<kk) for (int j=1; j<kk; j++) kkk = 10 * kkk;
               k = k + kkk *(int) (fs.charAt(i)-48);
           }            
           kk = sSearch.length()-1;
           kkk = sSearch.indexOf(".",0) ;
           kkkk = sSearch.indexOf(",",0) ;
           k4 = 0;
           if ( k4 < kkk ) k4 = kkk ;
           if ( k4 < kkkk) k4 = kkkk ;
           if (k4<0) sSearch = sSearch.concat(",");
           for (int i = 0 ; i<(k-(kk-k4));i++ )
               sSearch = sSearch.concat("0");
        }
        return sSearch;
    }
}