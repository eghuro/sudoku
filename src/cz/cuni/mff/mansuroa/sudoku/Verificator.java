package cz.cuni.mff.mansuroa.sudoku;

/**
 * Verificator kontroluje, zda data tridy Sudoku splnuji prislusna omezeni.
 * @author Alexandr Mansurov <alexander.mansurov@gmail.com>
 */
public class Verificator {
    
    /**
     * Over, zda Sudoku splnuje omezeni
     * @param sudoku data k overeni
     * @return true, pokud data splnuji omezeni
     */
    public static boolean verify(Sudoku sudoku) {
        //projdu po radcich/sloupcich/blocich a overim, ze kazde cislo je prave jednou
        return checkRows(sudoku) && checkCols(sudoku) && checkBlocks(sudoku);
    }
    
    /**
     * Vytvori pole booleanu a nastavi je na false
     * @param size rozmer vysledneho pole
     * @return pole booleanu dane velikosti s hodnotamu nastavenymi na false
     */
    private static boolean[] getValueArray(int size) {
        boolean hasValue[] = new boolean[size];
        for (int i = 0; i < size; i++) {
            hasValue[i] = false;
        }
        return hasValue;
    }
    
    /**
     * Over konkretni policko sudoku
     * @param i souradnice
     * @param j souradnice
     * @param haveValue ktere hodnoty jiz byly zaznamenany
     * @param sudoku kontrolovana data
     * @return zda hodnota na dane pozici splnuje omezeni
     */
    private static boolean checkPosition (int i, int j, boolean[] haveValue, Sudoku sudoku) {
        if(sudoku.isset(i, j)){
            int valIndex = sudoku.getValue(i, j) -1 ;
            if (haveValue[valIndex]) {
                return false;
            } else {
                haveValue[valIndex] = true;
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Kontroluj po radcich.
     * @param sudoku kontrolovana data
     * @return radky splnuji omezeni
     */
    private static boolean checkRows(Sudoku sudoku) {
        int size = sudoku.getSize();
        for (int i = 0; i < size; i++) {
            boolean haveValue[] = getValueArray(size);
            for (int j = 0; j < size; j++) {
                if(!checkPosition(i,j,haveValue,sudoku)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Kontroluj po sloupcich
     * @param sudoku kontrolovana data
     * @return sloupce splnuji omezeni
     */
    private static boolean checkCols(Sudoku sudoku) {
        int size = sudoku.getSize();
        for (int j = 0; j < size; j++) {
            boolean haveValue[] = getValueArray(size);
            for (int i = 0; i < size; i++) {
                if(!checkPosition(i,j, haveValue, sudoku)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Kontroluj po blocich "3x3"
     * @param sudoku kontrolovana data
     * @return bloky splnuji omezeni
     */
    private static boolean checkBlocks(Sudoku sudoku) {
        int size = (int)Math.ceil(Math.sqrt(sudoku.getSize()));
        for(int blockRow = 0; blockRow < size; blockRow++) {
            for(int blockCol = 0; blockCol < size; blockCol++) {
                if(!checkBlock(blockRow, blockCol, sudoku)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Zkontroluj blok "3x3"
     * @param blockRow radek bloku (0, 1, 2)
     * @param blockCol sloupec bloku (0, 1, 2)
     * @param sudoku kontrolovana data
     * @return blok splnuje omezeni
     */
    private static boolean checkBlock(int blockRow, int blockCol, Sudoku sudoku) {
        int size = sudoku.getSize();
        int koef = (int)Math.ceil(Math.sqrt(sudoku.getSize()));
        boolean haveValue[] = getValueArray(size);
        for(int row = blockRow*koef; row < blockRow*(koef+1); row++) {
            for (int col = blockCol*koef; col < blockCol*(koef+1); col++) {
                if(!checkPosition(row,col, haveValue, sudoku)) {
                    return false;
                }
            }
        }
        return true;
    }
}
