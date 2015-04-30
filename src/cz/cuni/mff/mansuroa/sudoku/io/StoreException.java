package cz.cuni.mff.mansuroa.sudoku.io;

import java.io.IOException;

/**
 * Chyba pri ukladani dat do souboru.
 * 
 * @author Alexandr Mansurov
 */
public class StoreException extends IOException {
    public StoreException() {}
    public StoreException(Exception e) {
        super(e);
    }
}
