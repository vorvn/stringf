package transfertrade;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.nio.file.Files;

/**
 * @author Swt-5win
 */
public class TransferTrade {
    String datainstuctionsSource;
    
    public static void main(String[] args) {
        String SEPARATOR = System.getProperty("line.separator");

        String pathDirectorySource = "/Users/prologistic/source.txt";
        String archiveDirectorySource = "/Users/prologistic/source.txt";
        int countInvestmentAtArchive = 0; 

        if (args.length > 1) {
            if (checkPathExistence(args[0], "DirData")) {
                 pathDirectorySource = args[0];
            } else { 
                 pathDirectorySource = args[0];
            }     
            if (checkPathExistence(args[1], "DirArchive")) {
                 archiveDirectorySource = args[1];
            } else {
                 archiveDirectorySource = args[1];
            }     
            if (args.length == 3) {
                countInvestmentAtArchive = (int)(args[2].charAt(0))-48;
            } else countInvestmentAtArchive = 0;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(" Not found DirData and DirArchive. ");
            sb.append("\n Example: ");
            sb.append("\n          TransferTrade.jar DirData DirArchive [NumberFirstFields]");
            System.out.println(sb.toString());
            System.exit(0);
            // RuntimeException re = new RuntimeException(sb.toString());
            // throw re;
        }

        File filesOfDirectorySource = new File(pathDirectorySource);
        String[] arrayNameFiles = filesOfDirectorySource.list();

//        for (int i=0; i<arrayNameFiles.length; i++) {
//            System.out.println(arrayNameFiles[i]);
//        }    
//        System.out.println();
        
        Date d = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy hh:mm");
//        SimpleDateFormat format2 = new SimpleDateFormat("День dd Месяц MM Год yyyy Время hh:mm");
        System.out.println(format1.format(d)+ "\n"); //25.02.2013 09:03
//        System.out.println(format2.format(d)); //День 25 Месяц 02 Год 2013 Время 09:03

        String fieldLogOut = "";
//        String filesNotEq = "";
        int countMoveFiles = 0;
        String currentLineNameFile = arrayNameFiles[0];
        for (int i=1; i<arrayNameFiles.length; i++) {
            String nextLineNameFile = arrayNameFiles[i];
            if (currentLineNameFile.substring(0, currentLineNameFile.indexOf('.'))
               .equals(nextLineNameFile.substring(0, nextLineNameFile.indexOf('.')))) {
                // Move current and next files at ArchiveDirectorySource
                if (moveFilesArchiveDirectory(pathDirectorySource,
                        archiveDirectorySource,
                        currentLineNameFile,
                        nextLineNameFile,
                        countInvestmentAtArchive)) {
                    countMoveFiles = countMoveFiles + 2;
                } else {
                    fieldLogOut = fieldLogOut.concat("    - " + currentLineNameFile)
                        .concat(SEPARATOR)
                        .concat("    - " + nextLineNameFile)
                        .concat(SEPARATOR);
//                    countMoveFiles = countMoveFiles + 2;
                }
                i++;
                if (i<arrayNameFiles.length) {
                    currentLineNameFile = arrayNameFiles[i];
                }
            } else {
                currentLineNameFile = nextLineNameFile;
//                filesNotEq = filesNotEq.concat(currentLineNameFile)
//                        .concat(SEPARATOR);
            }
        }
        System.out.println("\n Files can not move at " + archiveDirectorySource);
        System.out.println(fieldLogOut + "\n" );
        System.out.println("Number of reconsiled deals " + countMoveFiles + "\n");
//        System.out.println('\n' + filesNotEq);

        //try {
        //    new SwiftDataAnalyzer(instuctionsSource, 
        //                          new DataParser(dataMassage), 
        //                          new ResultWriter(dataSource)).execute();
        //} catch (SearcherException ex) {
        //    System.out.println(ex.toString());
        //} catch (IOException ex) {
        //    System.out.println(ex);
        //}
    }
    
    private static Boolean checkPathExistence(String path, String variableName) {
        File file = new File(path);
        return file.exists();
//        if (!file.exists()) { // && file.isFile())) {
//            System.out.println(" Not found " + variableName + ". ");
//            System.exit(0);
//            return false;   
//        } else {
//            return true;
//        }
    }

    private static Boolean moveFilesArchiveDirectory(String workDirectory,
            String archiveDirectory,
            String firstFileName,
            String secondFileName, int countInvestmentArchive) {

        if (!(workDirectory.lastIndexOf("\\") == workDirectory.length()-1)){
            workDirectory = workDirectory.concat("\\") ;
        }
        if (!(archiveDirectory.lastIndexOf("\\") == archiveDirectory.length()-1)){
            archiveDirectory = archiveDirectory.concat("\\") ;
        }
        String pathInvestment = "";
        int beginIndex = 0;
        do {
            int endIndex = firstFileName.indexOf('_', beginIndex);
            if (endIndex < 0) {
//                System.out.println(" Error : At NameFile " + firstFileName 
//                        + " not found countInvestmentArchive " + countInvestmentArchive);
                return false; 
            }
            pathInvestment = pathInvestment
                    .concat(firstFileName.substring(beginIndex, endIndex))
                    .concat("\\");
            beginIndex = endIndex+1; 
            countInvestmentArchive--;
        } while (countInvestmentArchive>0);
        pathInvestment = archiveDirectory.concat(pathInvestment
                .substring(0,pathInvestment.length()-2)); 
        if (!(checkPathExistence(pathInvestment,pathInvestment))) {
            File dir = new File(pathInvestment);// "C:\\MT300\\ARCHIVE\\aaa\\bbb");
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    System.out.println(" Directory create successfully: " + pathInvestment);
                } else {
                    System.out.println(" Directory not create: " + pathInvestment);
                    return false; 
                }
            }    
        }
        Boolean renameSuccuss = false;
        String tempFirstName = firstFileName;
        String tempSecondName = secondFileName;
        int copyNumber = 0;
        while (copyNumber < 100)
    	//    try {
        //        if (fileFirstName.renameTo(fileFirstName)) {
        //        if(afile.renameTo(new File("C:\\folderB\\" + afile.getName()))) {
        //            System.out.println("File is moved successful!");
        //        } else {
        //            System.out.println("File is failed to move!");
        //        }
        //    } catch(Exception e) {
        //        e.printStackTrace();
        //    }   
            if (new File(workDirectory+firstFileName)
                    .renameTo(new File(pathInvestment+"\\"+tempFirstName))) {
                System.out.println("  " + workDirectory + firstFileName + " to " 
                        + pathInvestment + tempFirstName + " Rename Successfully.");
                copyNumber = 100;
                renameSuccuss = true;
            } else  {
                copyNumber ++;
                System.out.println("Error Rename " + workDirectory + firstFileName + " to " 
                        + pathInvestment + tempFirstName + "(" + copyNumber + ")");
                tempFirstName = firstFileName.concat("(" + copyNumber + ")");
            }    
        copyNumber = 0;
        while (copyNumber < 100)
            if (new File(workDirectory+secondFileName)
                    .renameTo(new File(pathInvestment+"\\"+tempSecondName))) {
                System.out.println("  " + workDirectory + secondFileName + " to " 
                        + pathInvestment + tempSecondName + " Rename Successfully.");
                copyNumber = 100;
                renameSuccuss = true;
            } else  {
                copyNumber ++;
                System.out.println("Error Rename " + workDirectory + secondFileName + " to " 
                        + pathInvestment + tempSecondName + "(" + copyNumber + ")");
                tempSecondName = secondFileName.concat("(" + copyNumber + ")");
            }    
        return renameSuccuss;
    }
}


