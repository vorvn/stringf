package reconciliationtrade;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {
    private static final String SEPARATOR = System.getProperty("line.separator");

    private final StringBuilder logFileContent = new StringBuilder();
    private final StringBuilder newFileContent = new StringBuilder();
    private final String newFile;

    public ResultWriter(String newFile) {
        this.newFile = newFile;
    }

    public void append(String instruction, String data, int outputIndex) {
        if (data == null || data.isEmpty()) {
            return;
        }
        switch (outputIndex) {
            case 0: 
                logFileContent.append(instruction)
                        .append(SEPARATOR)
                        .append(data)
                        .append(SEPARATOR);
                break;
            case 1:
                newFileContent.append(data)
                        .append("_");
                break;
            default:
                System.out.println(instruction);
                char [] dataArray = data.toCharArray();
                for (int i=0;i<data.length();i++) {
                    switch (dataArray[i]) {
                        case '\r':
                            System.out.print("\\r");
                            break;
                        case '\n':
                            System.out.print("\\n");
                            break;
                        default:
                            System.out.print(dataArray[i]);
                            break;
                    }
                }
                System.out.println();
        }
    }
    
    public void flushContent() throws IOException {
        flushLogFile();
        flushNewFile();
    }

    private void flushLogFile() throws IOException {
        if (logFileContent.length() == 0) {
            return;
        }
        
        // запись в файл  fileName + "_LOG"
        File logFile = new File(newFile.concat("-LOG"));
        try (FileWriter writeFile = new FileWriter(logFile)) {
            writeFile.write(logFileContent.toString());
        }
    }

    private void flushNewFile() {
        if (newFileContent.length() == 0) {
            return;
        }
        if (newFileContent.indexOf("Error",0) > 0) {
            System.out.println(newFileContent.toString());
            return;
        }
        if ((newFileContent.indexOf("\r",0) > 0)||(newFileContent.indexOf("\r",0) > 0)) {
            char [] newFileContentArray = newFileContent.toString().toCharArray();
            for (int i=0;i<newFileContent.length();i++) {
                switch (newFileContentArray[i]) {
                    case '\r':
                        System.out.print("\\r");
                        break;
                    case '\n':
                        System.out.print("\\n");
                        break;
                    default:
                        System.out.print(newFileContentArray[i]);
                        break;
                }
            }
            System.out.println("\n Error: Found '\\r' or '\\n' - a line terminator. Get rid of them.");
            return;
        }
        String newFileName = formatNewFileName(newFileContent, newFile);
        if (newFileName.length() > 0) {
            File fileNewName = new File(newFileName);
            File fileOldName = new File(newFile);
            int copyNumber = 0;
            while (copyNumber < 100)
                if (fileOldName.renameTo(fileNewName)) {
                    System.out.println("  " + fileOldName 
                                       + " to " + newFileName + " Rename Successfully.");
                    copyNumber = 100;
                } else  {
                    copyNumber ++;
                    System.out.println(" Error Rename " + fileOldName + " to " 
                                       + newFileName+ "(" + copyNumber + ")");
                    File fileNewName1 = new File (fileNewName + "(" + copyNumber + ")");
                    fileNewName = fileNewName1;
                }    
        }    
    }
    
    private static String formatNewFileName(StringBuilder newFileContent, String newFile) {
        String fileNameNew = newFileContent.toString() ;
        String tempName = newFileContent.substring(0,fileNameNew.length()-1);
        String beginTempName, endTempName;
        int bslashIdx = newFile.lastIndexOf('\\');
        if (bslashIdx != -1) {
            beginTempName = newFile.substring(0, bslashIdx);
            endTempName = newFile.substring(bslashIdx + 1, newFile.length());
        } else {
            return tempName.concat("." + newFile.replace('.', '-'));
        }
        return beginTempName.concat("\\" + tempName.concat("." + endTempName.replace('.', '-')));
    }
}
