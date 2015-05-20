package cz.cuni.mff.mansuroa.sudoku;

/**
 * Model - datova vrstva aplikace.
 * 
 * @author Alexandr Mansurov
 */
public class Sudoku {
    private final int SIZE; //ALF: Naming conventions.
    private final int[][] MATRIX;
    
    /**
     * Vytvori Sudoku dane velikosti.
     * 
     * @param size sudoku bude rozmeru size * size
     */
    public Sudoku(int size) {
        this.SIZE = size;
        this.MATRIX = new int[SIZE][SIZE];
    }
    
    /**
     * Vrati rozmer daneho Sudoku.
     * 
     * @return rozmer mrizky
     */
    public int getSize() {
        return this.SIZE;
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
        if (validPosition(row, col) & (value >= 1) & (value <= SIZE)) {
            MATRIX[col][row] = value;
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
            return MATRIX[col][row];
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
            MATRIX[col][row] = 0;
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
            return MATRIX[col][row] != 0;
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
        return (row >= 0) && (row < SIZE) && (col >= 0) && (col < SIZE);
    }
    
    /**
     * Pristup k datum.
     * 
     * @return pole s ulozenymi daty
     */
    public int[][] getBoard() {
        return MATRIX; //ALF: It would be better to return a copy of the array, so that no one can change the data accidentally 
    }
}