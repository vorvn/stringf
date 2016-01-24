package stringfoundintext;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static stringfoundintext.StringFoundInText.countFoundFeilds;

public class SwiftDataAnalyzer {

    private final String instuctionsSource;
    private final DataParser dataParser;
    private final ResultWriter resultWriter;

    public SwiftDataAnalyzer(String instuctionsSource,
            DataParser dataParser,
            ResultWriter resultWriter) {
        this.instuctionsSource = instuctionsSource;
        this.dataParser = dataParser;
        this.resultWriter = resultWriter;
    }

    public void execute() throws SearcherException, IOException {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(instuctionsSource));
        } catch (FileNotFoundException ex) {
            throw new SearcherException("Unable to open file with instructions");
        }

        for (String instruction = br.readLine(); instruction != null; instruction = br.readLine()) {
            parseAndExecute(instruction);
        }
    }

    private void parseAndExecute(String instruction) {
        if (instruction.length() > 0 && instruction.charAt(0) != '#') {
            String[] lineSearch = instruction.split("\\|");
            // lineSearch.length - количество Полей поиска в СтрокеПоиска
            if (lineSearch.length < countFoundFeilds) {
                System.out.println("Error: CountFoundFields in Line < 8. " + instruction);
            } else {
                // TODO rename local variables
                String aField = lineSearch[0].substring(1, lineSearch[0].length() - 1);
                int aNumStr = Integer.parseInt(lineSearch[1]);
                String aFirstStr = lineSearch[2].substring(1, lineSearch[2].length() - 1);
                int aFirstShift = Integer.parseInt(lineSearch[3]);
                String aSecondStr = lineSearch[4].substring(1, lineSearch[4].length() - 1);
                int aSecondShift = Integer.parseInt(lineSearch[5]);
                String aFormat = lineSearch[6];
                String aCommand = lineSearch[7];
                
                resultWriter.append(
                        instruction,
                        dataParser.parse(
                                aField,
                                aNumStr,
                                aFirstStr,
                                aFirstShift,
                                aSecondStr,
                                aSecondShift,
                                aFormat
                        ),
                        getOutputIndex(aCommand.charAt(0))
                );
                
            }
        }
    }

    private int getOutputIndex(char outputTypeMarker) {
        switch (outputTypeMarker){
            case 'L':
                return 0;
            case 'F':
                return 1;
            default:
                return 2;
        }
    }
}
