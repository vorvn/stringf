package stringfoundintext;

public class DataParser {
    
    private final String dataSource;

    public DataParser(String dataSource) {
        this.dataSource = dataSource;
    }

//    public String parse(String aField, int aNumStr, String aFirstStr, int aFirstShift, String aSecondStr, int aSecondShift, String aFormat) {
    public String parse(String s0, int r0, String s1,
                                            int r1, String s2, int r2, String fs) {
        String sSearch = " ";
        int k, kk, kkk, kkkk, k4 ;
        //k = kk = kkk = kkkk = 0;
        if (dataSource.indexOf(s0,0) < 0)
            return sSearch = " Error: 1-param not found.";
        k = dataSource.indexOf(s0,0);
        if (r0 < 0) return sSearch = " Error: 2-param < 0.";  
        for (int i = 0; i < r0; i++)
        {
           int akR = dataSource.indexOf("\r",k) ;
           int akN = dataSource.indexOf("\n",akR) ;
           if ( k < akR ) k = akR ;
           if ( k < akN ) k = akN ;
           if ((dataSource.length() - k)<5) sSearch = " Error: 2-param > endOfFile.";  
        }    
        if (dataSource.indexOf(s1,k) < 0) return sSearch = " Error: 3-param not found.";
        k4 = dataSource.indexOf(s1,k);
        if ((k4+r1) < 0) return sSearch = " Error: 4-param < beginOfFile.";
        k = k4 ;
        if (s1.equals("")) kkkk = k + r1; else kkkk = k4;
        if (s2.equals("\n")||s2.equals("\\n"))
        {
           int akR = dataSource.indexOf("\r",k4) ;
           int akN = dataSource.indexOf("\n",akR) ;
           if ( k4 < akR ) k4 = akR ;
           if ( k4 < akN ) k4 = akN ;
           if ((dataSource.length() - k4)<5) sSearch = " Error: 4-param > endOfFile.";  
        }
        else
        {
           if (s2.equals("")) kk = k4;
           else
           {
             if (dataSource.indexOf(s2,kkkk+1)<0) return sSearch = " Error: 5-param not found.";
             kk = dataSource.indexOf(s2,kkkk+1) ;
             if ( k4 < kk ) k4 = kk + 1 ;
           }
           if ( k4 == k ) k4 = k + r1 ;
        };
        if ( k+r1 > k4+r2 ) return sSearch = " Error: Position 6-param < 4-param. Length (object) < 0.";  
        sSearch = dataSource.substring(k+r1, k4+r2);
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
