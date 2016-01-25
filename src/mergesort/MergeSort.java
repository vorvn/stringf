package mergesort;

import static java.lang.Math.pow;

public class MergeSort {

    public static void main(String[] args) {
                                  // 0  1  2  3  4  5  6  7  8  9
        int[] arrayMain = new int[] {23,32,13,28,65,21,4,10,5,37}; //{13,8,133,15,6,28,68,44}; // {23,32,13,28,65,21,6,44};
        int[] arrayResult = new int [arrayMain.length] ; 

        for (int i = 0; i < arrayMain.length ; i++) {
            arrayResult[i] = i;                    
        }

        int countCircleSort = arrayMain.length - arrayMain.length/2;
        for (int i = 0; i < countCircleSort ; i++) {
            int countElementMerge = (int) pow(2, i);            
            for (int j=0; j <= countCircleSort; j++ ) {
                int currentFirstPointer = j*2*countElementMerge; 
                int currentSecondPointer = currentFirstPointer + 2*(countElementMerge -1);
                if (currentFirstPointer < arrayMain.length) {
                    arrayResult = arrayMergeMain (arrayMain, 
                        arrayResult,
                        countElementMerge,
                        currentFirstPointer);
                }
            }
        }

        System.out.println(" Finish.");                    
        for (int p = 0; p < arrayMain.length ; p++) {
            System.out.print(arrayMain[arrayResult[p]]+" ");                    
        }
        System.out.println();                    

    }

    public static int[] arrayMergeMain (int[] arrayMain, 
        int[] arrayResult,
        int numLengthSort,
        int currentFirstPointer) {

        int currentSecondPointer = currentFirstPointer + numLengthSort;
        if (currentSecondPointer > arrayMain.length-1) {
            return arrayResult;
        }

        int maxFirstArray = currentFirstPointer + numLengthSort;
        int maxSecondArray = currentSecondPointer + numLengthSort;
        if (maxSecondArray>arrayMain.length-1) {
            maxSecondArray = arrayMain.length;
        }
        
        int BeginPos = currentFirstPointer;
        // Demo out print
            for (int p = 0; p < numLengthSort ; p++) {
                System.out.print(arrayMain[arrayResult[currentFirstPointer+p]]+" ");                    
            }
            System.out.println(" First ");                    

            for (int p = 0; p < numLengthSort ; p++) {
                if (currentSecondPointer+p<arrayMain.length)
                    System.out.print(arrayMain[arrayResult[currentSecondPointer+p]]+" ");                    
            }
            System.out.println(" Second ");                    
        // Demo out print
        int[] tempArrayResult = new int [2*numLengthSort] ; 
        int[] copyArrayResult = new int [2*numLengthSort] ; 
        for (int p = 0; p < 2*numLengthSort ; p++) {
            if (currentFirstPointer+p<arrayMain.length)
                copyArrayResult[p] = arrayResult[currentFirstPointer+p];
        }

        int i = 0;
        while ((i<numLengthSort*2)&&(i<arrayMain.length)) {
            //int t1 = arrayMain[arrayResult[currentSecondPointer]];
            //int t2 = arrayMain[arrayResult[currentFirstPointer]];
            if (arrayMain[arrayResult[currentSecondPointer]] <= arrayMain[arrayResult[currentFirstPointer]]) {
                tempArrayResult[i] = currentSecondPointer;
                currentSecondPointer++;
            } else {
                tempArrayResult[i] = currentFirstPointer;
                currentFirstPointer++;
            }
            i++; 
            // ? FirstSortArray finish, added SecondSortAdday 
            if (currentFirstPointer==maxFirstArray){
                for (int k=currentSecondPointer;k<maxSecondArray;k++){
                    tempArrayResult[i] = k;
                    i++;
                }
            } else { // ? SecondSortArray finish, added FirstSortAdday 
                if (currentSecondPointer==maxSecondArray){
                   for (int k=currentFirstPointer;k<maxFirstArray;k++){
                       tempArrayResult[i] = k;
                       i++;
                    }
                }
            }   
        }

        for (int k=0;k<numLengthSort*2;k++) {
            if (BeginPos+k<arrayMain.length)
                arrayResult[BeginPos+k] = copyArrayResult[tempArrayResult[k]-BeginPos];
        }
        // Demo out print
        for (int p = 0; p < arrayMain.length ; p++) {
            System.out.print(arrayResult[p]+"  ");                    
        }
        System.out.println();                    
        // Demo out print
        for (int p = 0; p < arrayMain.length ; p++) {
            System.out.print(arrayMain[arrayResult[p]]+" ");
        }
        System.out.println();                    
        // Demo out print

        return arrayResult;
    }
}
