package reconciliationtrade;

public class DataParser {
    
    private final String dataMessage;

    public DataParser(String dataMessage) {
        this.dataMessage = dataMessage;
    }

    public String parse(String searchMandataryField, 
                        int countShiftString, 
                        String searchBeginPosition,
                        int shiftAtBeginPosition, 
                        String searchEndPosition, 
                        int shiftAtEndPosition, 
                        String formatDataSearchOut) {
        String search ;
        int tempPosition;
        int tempPositionEnd;
        if ((tempPosition = getSearchMandataryPosition(searchMandataryField,countShiftString)) < 0) {
            return " Error: 1-param not found.";
        }    
        if ((tempPosition = getSearchPosition(searchBeginPosition,tempPosition,shiftAtBeginPosition)) < 0) {
            return " Error: 3-param not found.";
        }    
        if ((tempPositionEnd = getSearchPosition(searchEndPosition,tempPosition,shiftAtEndPosition)) < 0) {
            return " Error: 5-param not found.";
        }    
        if ( tempPosition < tempPositionEnd ) {
            search = dataMessage.substring(tempPosition,tempPositionEnd) ;
        } else {
            search = dataMessage.substring(tempPosition,tempPositionEnd) ;
        }
        if (formatDataSearchOut.charAt(0)=='N')
        {  
           int lengthDecimal = formatDataSearchOut.length()-1;
           int number = 0;
           for (int i=1; i<=lengthDecimal; i++)
           {  
               int numUnits =1;
               if (i<lengthDecimal) for (int j=1; j<lengthDecimal; j++) numUnits = 10 * numUnits;
               number = number + numUnits *(int) (formatDataSearchOut.charAt(i)-48);
           }            
           int lengthsSearch = search.length()-1;
           int tempCount;
           if (search.lastIndexOf('.')<search.lastIndexOf(',')) {
               tempCount =+ search.lastIndexOf(',');
           } else {
               tempCount =+ search.lastIndexOf('.');
           }
           if (tempCount<0) search = search.concat(",");
           for (int i = 0 ; i<(number-(lengthsSearch-tempCount));i++ )
               search = search.concat("0");
        }

        return search;
    }
    
    private int getSearchMandataryPosition(String searchStringField, int beginSearchPosition) {
        if (dataMessage.indexOf(searchStringField,0) < 0) {
           return -1;
        }   
        int tempPositionBegin = dataMessage.indexOf(searchStringField,0);
        if (beginSearchPosition>=0) {
            for (int i = 0; i < beginSearchPosition; i++)
            {
            int tempPositionBeginEndR = dataMessage.indexOf("\r",tempPositionBegin) ;
            int tempPositionBeginEndN = dataMessage.indexOf("\n",tempPositionBeginEndR) ;
            if ( tempPositionBegin < tempPositionBeginEndR ) tempPositionBegin = tempPositionBeginEndR ;
            if ( tempPositionBegin < tempPositionBeginEndN ) tempPositionBegin = tempPositionBeginEndN ;
            }     
        } else {
            for (int i = 0; i < -beginSearchPosition+1; i++)
            {
                String tempdataSource = dataMessage.substring(0, tempPositionBegin) ;
                int tempPositionBeginEndR = tempdataSource.lastIndexOf('\r') ;
                int tempPositionBeginEndN = tempdataSource.lastIndexOf('\n') ;
                if ( tempPositionBegin > tempPositionBeginEndR ) tempPositionBegin = tempPositionBeginEndR ;
                if ( tempPositionBegin > tempPositionBeginEndN ) tempPositionBegin = tempPositionBeginEndN-1 ;
            }     
        }    
        return tempPositionBegin ;
    }
    
    private int getSearchPosition(String searchStringField, int beginSearchPosition, int shiftPosition) {
        int tempPositionBegin = beginSearchPosition;
        if (searchStringField.equals("\n")||searchStringField.equals("\\n"))
        {
            int tempPositionBeginEndR = dataMessage.indexOf("\r",tempPositionBegin) ;
            int tempPositionBeginEndN = dataMessage.indexOf("\n",tempPositionBeginEndR) ;
            if ( tempPositionBegin < tempPositionBeginEndR ) tempPositionBegin = tempPositionBeginEndR ;
            if ( tempPositionBegin < tempPositionBeginEndN ) tempPositionBegin = tempPositionBeginEndN ;
        } else {
            tempPositionBegin = dataMessage.indexOf(searchStringField,beginSearchPosition);
            if (tempPositionBegin < 0) {
               return tempPositionBegin;
            } 
        }    
        if ((tempPositionBegin + shiftPosition)<0) {
            return 0;
        } else if (((tempPositionBegin + shiftPosition) > dataMessage.length())){
            return dataMessage.length()-1;
        } else {
            return tempPositionBegin + shiftPosition;
        }
    }    
}
