package stringfoundintext;

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
                        .append(data);
                break;
            case 1:
                newFileContent.append(data)
                        .append("_");
            default:
                System.out.println(
                        instruction.concat(SEPARATOR)
                                .concat(data)
                                .concat(SEPARATOR)
                );
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
        File logFile = new File(newFile.concat("_LOG"));
        try (FileWriter writeFile = new FileWriter(logFile)) {
            writeFile.write(logFileContent.toString());
        }
    }

    private void flushNewFile() {
        String newFileName = formatNewFileName(newFile);
        // TODO rename file
    }
    
    private static String formatNewFileName(String sourceFileName) {
        // TODO write file name creation logic!
        return null;
    }
}
