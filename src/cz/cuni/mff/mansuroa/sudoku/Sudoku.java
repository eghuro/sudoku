package cz.cuni.mff.mansuroa.sudoku;

/**
 * Model - datova vrstva aplikace.
 */
public class Sudoku {
    private final int size;
    private final int[][] matrix;
    
    /**
     * Vytvori Sudoku dane velikosti.
     * 
     * @param size sudoku bude rozmeru size * size
     */
    public Sudoku(int size) {
        this.size = size;
        this.matrix = new int[size][size];
    }
    
    /**
     * Vrati rozmer daneho Sudoku.
     * 
     * @return rozmer mrizky
     */
    public int getSize() {
        return this.size;
    }
 
    /**
     * Nastav hodnotu policka.
     * Do daneho radku a sloupce vloz danou hodnotu.
     *
     * @param row radek, hodnota je &gt;= 0 a &lt; size
     * @param col sloupec, hodnota je &gt;= 0 a &lt; size
     * @param value hodnota je &gt;= 1 a =&lt; size
     * @throws IllegalArgumentException pokud nejsou splnena omezeni
     */
    public void setValue(int row, int col, int value) throws IllegalArgumentException {
        if (validPosition(row, col) & (value >= 1) & (value <= size)) {
            matrix[col][row] = value;
        } else {
            throw new IllegalArgumentException("Set [" + col + "," + row + "] = " + value);
        }
    }
    
    /**
     * Vrat hodnotu sudoku na prislusne pozici.
     * 
     * @param row radek
     * @param col sloupec
     * @return hodnota na dane pozici
     * @throws IllegalArgumentException pokud nejde o platnou pozici
     */
    public int getValue(int row, int col) throws IllegalArgumentException {
        if (validPosition(row, col)) {
            return matrix[col][row];
        } else {
            throw new IllegalArgumentException("Get [" + col + "," + row + "]");
        }
    }
    
    /**
     * Vymaze hodnotu z dane pozice.
     * 
     * @param row radek
     * @param col sloupec
     * @throws IllegalArgumentException neplatna souradnice
     */
    public void unsetValue(int row, int col) throws IllegalArgumentException {
        if (validPosition(row, col)) {
            matrix[col][row] = 0;
        } else {
            throw new IllegalArgumentException("Unset [" + col + "," + row + "]");
        }
    }
    
    /**
     * Je nejaka hodnota na dane souradnici.
     * 
     * @param row radek
     * @param col sloupec
     * @return pokud je na dane souradnici vyplnena hodnota
     * @throws IllegalArgumentException neplatna souradnice
     */
    public boolean isset(int row, int col) throws IllegalArgumentException {
        if (validPosition(row, col)) {
            return matrix[col][row] != 0;
        } else {
            throw new IllegalArgumentException("isset [" + col + "," + row + "]");
        }
    }
    
    /**
     * Test, zda jde o platnou souradnici.
     * 
     * @param row radek
     * @param col sloupec
     * @return radek >= 0, radek < size, sloupec >= 0, sloupec < size
     */
    private boolean validPosition(int row, int col) {
        return (row >= 0) && (row < size) && (col >= 0) && (col < size);
    }
    
    public Sudoku copy() {
        Sudoku copy = new Sudoku(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if(isset(row,col)) {
                    copy.setValue(row,col, getValue(row,col));
                }
            }
        }
        return copy;
    }
}