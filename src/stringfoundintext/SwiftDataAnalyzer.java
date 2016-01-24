package reconciliationtrade;
//package stringfoundintext;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static reconciliationtrade.ReconciliationTrade.COUNT_FOUND_FIELDS;

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
        resultWriter.flushContent();
    }

    private void parseAndExecute(String instruction) {
        if (instruction.length() > 0 && instruction.charAt(0) != '#') {
            String[] lineSearch = instruction.split("\\|");
            // lineSearch.length - количество Полей поиска в СтрокеПоиска
            if (lineSearch.length < COUNT_FOUND_FIELDS) {
                System.out.println("Error: COUNT_FOUND_FIELDS in Line < " + COUNT_FOUND_FIELDS + ". " + instruction);
            } else {
                // TODO rename local variables
                String searchMandataryField = lineSearch[0].substring(1, lineSearch[0].length() - 1);
                int countShiftString = Integer.parseInt(lineSearch[1]);
                String searchBeginPosition = lineSearch[2].substring(1, lineSearch[2].length() - 1);
                int shiftAtBeginPosition = Integer.parseInt(lineSearch[3]);
                String searchEndPosition = lineSearch[4].substring(1, lineSearch[4].length() - 1);
                int shiftAtEndPosition = Integer.parseInt(lineSearch[5]);
                String formatDataSearchOut = lineSearch[6];
                String commandOutStream = lineSearch[7];
                
                resultWriter.append(
                        instruction,
                        dataParser.parse(
                                searchMandataryField,
                                countShiftString,
                                searchBeginPosition,
                                shiftAtBeginPosition,
                                searchEndPosition,
                                shiftAtEndPosition,
                                formatDataSearchOut
                        ),
                        getOutputIndex(commandOutStream.charAt(0))
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